package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TaskSolutionDto;
import com.thesis.studyapp.event.UpdatedStatusEvent;
import com.thesis.studyapp.exception.NotFoundException;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.model.StudentTaskStatus;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.repository.StudentStatusRepository;
import com.thesis.studyapp.util.AuthenticationUtil;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultTestSolvingService implements TestSolvingService {
    public static final int MAX_LEVEL = 10;

    private final StudentStatusRepository studentStatusRepository;

    private final DateUtil dateUtil;
    private final AuthenticationUtil authenticationUtil;
    private final ApplicationEventPublisher eventPublisher;

    @Override @Transactional
    public TaskSolutionDto checkSolution(Long testId, int chosenAnswerNumber) {
        StudentStatus userStatus = getUserTestStatus(testId, 2);
        if (userStatus.getCurrentTestTask() == null || userStatus.isCurrentTestTaskSolved()) {
            throw new NotFoundException("No current TestTask available");
        }

        int correctAnswerNumber = setNewSolution(userStatus, chosenAnswerNumber);
        boolean statusChanged = setNewStatus(userStatus);

        userStatus = studentStatusRepository.save(userStatus, 2);

        if (statusChanged) {
            eventPublisher.publishEvent(new UpdatedStatusEvent(this, userStatus.getId()));
        }

        return TaskSolutionDto.builder()
                .chosenAnswerNumber(chosenAnswerNumber)
                .solutionNumber(correctAnswerNumber)
                .correctSolutions(userStatus.getCorrectSolutions())
                .allSolutions(userStatus.getAllSolutions())
                .solvedTasks(userStatus.getStudentTaskStatuses().size())
                .allTasks(userStatus.getTest().getTestTasks().size())
                .build();
    }

    @Override @Transactional
    public StudentStatus calculateNextTask(Long testId) {
        StudentStatus studentStatus = getUserTestStatus(testId, 2);

        if (studentStatus.getCurrentTestTask() == null || studentStatus.isCurrentTestTaskSolved()) {
            setNextTask(studentStatus);
            studentStatus = studentStatusRepository.save(studentStatus, 2);
        }

        return studentStatus;
    }

    private int setNewSolution(StudentStatus userStatus, int chosenAnswerNumber) {
        int correctSolutionNumber = userStatus.getCurrentTestTask().getTask().getSolutionNumber();
        boolean isCorrectSolution = (correctSolutionNumber == chosenAnswerNumber);

        setNewSolution(userStatus, isCorrectSolution);

        return correctSolutionNumber;
    }

    private void setNewSolution(StudentStatus userStatus, boolean isCorrect) {
        userStatus.setNewSolution(isCorrect, dateUtil.getCurrentTime());

        userStatus.getCurrentTestTask().setNewSolution(isCorrect);

        Optional<StudentTaskStatus> taskStatus = userStatus.getStudentTaskStatuses().stream()
                .filter(status -> status.getTestTask().equals(userStatus.getCurrentTestTask()))
                .findFirst();

        if (taskStatus.isPresent()) {
            taskStatus.get().setNewSolution(isCorrect, dateUtil.getCurrentTime());
        } else {
            userStatus.addUserTestTaskStatus(new StudentTaskStatus(
                    userStatus.getCurrentTestTask(), isCorrect, dateUtil.getCurrentTime()));
        }
    }

    private boolean setNewStatus(StudentStatus userStatus) {
        double currentLevelRatioDiff = getCurrentRatioDiffForLevel(userStatus.getStudentTaskStatuses(), userStatus.getCurrentLevel());
        double currentAllRatioDiff = getCurrentRatioDiff(userStatus.getStudentTaskStatuses());

        boolean statusChanged = false;
        switch (userStatus.getStatus()) {
            case NOT_STARTED:
                userStatus.setStatus(StudentStatus.Status.IN_PROGRESS);
                statusChanged = true;
                break;
            case IN_PROGRESS:
                if (currentAllRatioDiff < -0.5 || currentLevelRatioDiff < -0.5
                        || userStatus.getWrongSolutionsInRow() >= 2) {
                    userStatus.setStatus(StudentStatus.Status.PROBLEM);
                    statusChanged = true;
                }
                break;
            case PROBLEM:
                if (currentAllRatioDiff > -0.5 && currentLevelRatioDiff > -0.5
                        && userStatus.getCorrectSolutionsInRow() >= 2) {
                    userStatus.setStatus(StudentStatus.Status.IN_PROGRESS);
                    statusChanged = true;
                }
                break;
        }
        if (statusChanged) {
            userStatus.setStatusChangedDate(dateUtil.getCurrentTime());
        }
        return statusChanged;
    }

    private double getCurrentRatioDiffForLevel(Set<StudentTaskStatus> taskStatuses, int level) {
        return taskStatuses.stream()
                .filter(taskStatus -> taskStatus.getTestTask().getLevel() == level)
                .mapToDouble(taskStatus -> taskStatus.getRatioInCurrentCycle() - taskStatus.getTestTask().getRatio())
                .average()
                .orElse(0);
    }

    private double getCurrentRatioDiff(Set<StudentTaskStatus> taskStatuses) {
        return taskStatuses.stream()
                .mapToDouble(taskStatus -> taskStatus.getRatioInCurrentCycle() - taskStatus.getTestTask().getRatio())
                .average()
                .orElse(0);
    }

    private void setNextTask(StudentStatus userStatus) {
        int currentLevel = userStatus.getCurrentLevel();
        int currentCycle = userStatus.getCurrentCycle();

        if (userStatus.getTest().getTestTasks().isEmpty()) {
            return;
        }

        Set<TestTask> allTestTasks = userStatus.getTest().getTestTasks().stream()
                .filter(testTask -> testTask.getLevel() == currentLevel)
                .collect(Collectors.toSet());
        Set<StudentTaskStatus> solvedTaskStatuses = userStatus.getStudentTaskStatuses().stream()
                .filter(status -> (status.getTestTask().getLevel() == currentLevel)
                        && allTestTasks.contains(status.getTestTask()))
                .collect(Collectors.toSet());
        Set<TestTask> newTestTasks = allTestTasks.stream()
                .filter(testTask ->
                        solvedTaskStatuses.stream().noneMatch(status -> status.getTestTask().equals(testTask)))
                .collect(Collectors.toSet());

        if (allTestTasks.isEmpty()) {
            increaseLevel(userStatus);
            setNextTask(userStatus);
            return;
        }

        if (!newTestTasks.isEmpty()) {
            userStatus.setNewCurrentTestTask(getEasiestTestTask(newTestTasks));
            return;
        }

        Optional<TestTask> nextTask = calculateNextTaskFromSolved(solvedTaskStatuses, currentCycle);
        if (nextTask.isPresent()) {
            userStatus.setNewCurrentTestTask(nextTask.get());
        } else {
            increaseLevel(userStatus);
            setNextTask(userStatus);
        }
    }

    private void increaseLevel(StudentStatus userStatus) {
        if (userStatus.getCurrentLevel() < MAX_LEVEL) {
            userStatus.increaseLevel();
        } else {
            userStatus.increaseCycle();
            for (StudentTaskStatus taskStatus : userStatus.getStudentTaskStatuses()) {
                taskStatus.setNewCycle();
            }
        }
    }

    private Optional<TestTask> calculateNextTaskFromSolved(Set<StudentTaskStatus> solvedTaskStatuses, int currentCycle) {
        Optional<TestTask> nextTask = getTaskWhenNewInCurrentCycle(solvedTaskStatuses);
        if (nextTask.isEmpty()) {
            nextTask = getTaskWhenLastAnswerWrong(solvedTaskStatuses);
        }
        if (nextTask.isEmpty()) {
            nextTask = getTaskWhenSmallAverageRatio(solvedTaskStatuses, currentCycle);
        }
        if (nextTask.isEmpty()) {
            nextTask = getTaskWhenBigDiffInRatio(solvedTaskStatuses, currentCycle);
        }
        return nextTask;
    }

    private Optional<TestTask> getTaskWhenNewInCurrentCycle(Set<StudentTaskStatus> solvedTaskStatuses) {
        Set<StudentTaskStatus> newInCurrentCycle = solvedTaskStatuses.stream()
                .filter(taskStatus -> taskStatus.getAllSolutionsInCurrentCycle() == 0)
                .collect(Collectors.toSet());
        if (!newInCurrentCycle.isEmpty()) {
            return Optional.of(getAvgEasiestTestTask(newInCurrentCycle));
        } else {
            return Optional.empty();
        }
    }

    private Optional<TestTask> getTaskWhenLastAnswerWrong(Set<StudentTaskStatus> solvedTaskStatuses) {
        Set<StudentTaskStatus> lastAnswerWrong = solvedTaskStatuses.stream()
                .filter(taskStatus -> taskStatus.getWrongSolutionsInRow() > 0)
                .collect(Collectors.toSet());
        if (!lastAnswerWrong.isEmpty()) {
            return Optional.of(getCurrentEasiestTestTask(getTaskStatusesWithLessSolution(lastAnswerWrong)));
        } else {
            return Optional.empty();
        }
    }

    private Optional<TestTask> getTaskWhenBigDiffInRatio(Set<StudentTaskStatus> solvedTaskStatuses, int currentCycle) {
        Set<StudentTaskStatus> bigDiffInRatio = solvedTaskStatuses.stream()
                .filter(taskStatus -> (taskStatus.getRatioInCurrentCycle() - taskStatus.getTestTask().getRatio() < -0.5))
                .collect(Collectors.toSet());
        if (!bigDiffInRatio.isEmpty()) {
            return Optional.of(getCurrentEasiestTestTask(getTaskStatusesWithLessSolution(bigDiffInRatio)));
        } else {
            return Optional.empty();
        }
    }

    private Optional<TestTask> getTaskWhenSmallAverageRatio(Set<StudentTaskStatus> solvedTaskStatuses, int currentCycle) {
        if (currentCycle == 1) {
            double averageRatio = solvedTaskStatuses.stream().mapToDouble(StudentTaskStatus::getRatio).average().orElse(0);
            if (averageRatio < 0.5) {
                return Optional.of(getCurrentHardestTestTask(getTaskStatusesWithLessSolution(solvedTaskStatuses)));
            }
        } else {
            double currentAverageRatio = solvedTaskStatuses.stream().mapToDouble(StudentTaskStatus::getRatioInCurrentCycle).average().orElse(0);
            double prevAverageRatio = solvedTaskStatuses.stream().mapToDouble(StudentTaskStatus::getRatioInPrevCycle).average().orElse(0);
            if (currentAverageRatio < prevAverageRatio) {
                return Optional.of(getCurrentHardestTestTask(getTaskStatusesWithLessSolution(solvedTaskStatuses)));
            }
        }
        return Optional.empty();
    }

    private Set<StudentTaskStatus> getTaskStatusesWithLessSolution(Set<StudentTaskStatus> taskStatuses) {
        int lessSolution = Collections.min(taskStatuses.stream()
                .map(StudentTaskStatus::getAllSolutionsInCurrentCycle)
                .collect(Collectors.toSet()));
        return taskStatuses.stream().
                filter(taskStatus -> taskStatus.getAllSolutionsInCurrentCycle() == lessSolution)
                .collect(Collectors.toSet());
    }

    private TestTask getAvgEasiestTestTask(Set<StudentTaskStatus> taskStatuses) {
        StudentTaskStatus maxDiff = Collections.max(taskStatuses,
                (status1, status2) -> {
                    double diffRatio1 = status1.getRatio() - status1.getTestTask().getRatio();
                    double diffRatio2 = status2.getRatio() - status2.getTestTask().getRatio();
                    return (diffRatio1 > diffRatio2) ? 1 : -1;
                });
        return maxDiff.getTestTask();
    }

    private TestTask getCurrentEasiestTestTask(Set<StudentTaskStatus> taskStatuses) {
        StudentTaskStatus maxDiff = Collections.max(taskStatuses,
                (status1, status2) -> {
                    double diffRatio1 = status1.getRatioInCurrentCycle() - status1.getTestTask().getRatio();
                    double diffRatio2 = status2.getRatioInCurrentCycle() - status2.getTestTask().getRatio();
                    return (diffRatio1 > diffRatio2) ? 1 : -1;
                });
        return maxDiff.getTestTask();
    }

    private TestTask getCurrentHardestTestTask(Set<StudentTaskStatus> taskStatuses) {
        StudentTaskStatus minDiff = Collections.max(taskStatuses,
                (status1, status2) -> {
                    double diffRatio1 = status1.getRatioInCurrentCycle() - status1.getTestTask().getRatio();
                    double diffRatio2 = status2.getRatioInCurrentCycle() - status2.getTestTask().getRatio();
                    return (diffRatio1 < diffRatio2) ? 1 : -1;
                });
        return minDiff.getTestTask();
    }

    private TestTask getEasiestTestTask(Set<TestTask> testTasks) {
        return Collections.max(testTasks, Comparator.comparing(TestTask::getRatio));
    }

    private StudentStatus getUserTestStatus(Long testId, int depth) {
        Long userId = authenticationUtil.getPrincipals().getUserId();
        return studentStatusRepository
                .findFirstByActiveTrueAndUserIdAndTestId(userId, testId, depth)
                .orElseThrow(() -> new NotFoundException("No StudentStatus available for this Test"));
    }

}
