package com.thesis.studyapp.service;


import com.thesis.studyapp.dto.TaskSolutionDto;
import com.thesis.studyapp.event.UpdatedStatusEvent;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.exception.InvalidInputException;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.model.UserTestTaskStatus;
import com.thesis.studyapp.repository.UserTestStatusRepository;
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
public class TestSolvingService {
    public static final int MAX_LEVEL = 10;

    private final UserTestStatusRepository userTestStatusRepository;

    private final DateUtil dateUtil;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public TaskSolutionDto checkSolution(Long userId, Long testId, int chosenAnswerNumber) {
        UserTestStatus userStatus = getUserTestStatus(userId, testId, 2);

        if (userStatus.getCurrentTestTask() == null || userStatus.isCurrentTestTaskSolved()) {
            throw new CustomGraphQLException("No current TestTask available");
        }

        int correctAnswerNumber = setNewSolution(userStatus, chosenAnswerNumber);

        boolean statusChanged = setNewStatus(userStatus);

        calculateNextTask(userStatus);

        userStatus = userTestStatusRepository.save(userStatus, 2);

        if (statusChanged) {
            eventPublisher.publishEvent(new UpdatedStatusEvent(this, userStatus.getId()));
        }

        return TaskSolutionDto.builder()
                .chosenAnswerNumber(chosenAnswerNumber)
                .solutionNumber(correctAnswerNumber)
                .correctSolutions(userStatus.getCorrectSolutions())
                .allSolutions(userStatus.getAllSolutions())
                .solvedTasks(userStatus.getUserTestTaskStatuses().size())
                .allTasks(userStatus.getTest().getTestTasks().size())
                .build();
    }

    private int setNewSolution(UserTestStatus userStatus, int chosenAnswerNumber) {
        int correctSolutionNumber = userStatus.getCurrentTestTask().getTask().getSolutionNumber();
        boolean isCorrectSolution = (correctSolutionNumber == chosenAnswerNumber);

        setNewSolution(userStatus, isCorrectSolution);

        return correctSolutionNumber;
    }

    private void setNewSolution(UserTestStatus userStatus, boolean isCorrect) {
        userStatus.setNewSolution(isCorrect);

        userStatus.getCurrentTestTask().setNewSolution(isCorrect);

        Optional<UserTestTaskStatus> taskStatus = userStatus.getUserTestTaskStatuses().stream()
                .filter(status -> status.getTestTask().equals(userStatus.getCurrentTestTask()))
                .findFirst();

        if (taskStatus.isPresent()) {
            taskStatus.get().setNewSolution(isCorrect, dateUtil.getCurrentTime());
        } else {
            userStatus.addUserTestTaskStatus(new UserTestTaskStatus(
                    userStatus.getCurrentTestTask(), isCorrect, dateUtil.getCurrentTime()));
        }
    }


    private boolean setNewStatus(UserTestStatus userStatus) {
//        double currentLevelRatio = userStatus.getUserTestTaskStatuses().stream()
//                .filter(taskStatus -> taskStatus.getTestTask().getLevel() == userStatus.getCurrentLevel())
//                .mapToDouble(UserTestTaskStatus::getRatioInCurrentCycle)
//                .average()
//                .orElse(0);
//        double currentAllRatio = userStatus.getUserTestTaskStatuses().stream()
//                .mapToDouble(UserTestTaskStatus::getRatioInCurrentCycle)
//                .average()
//                .orElse(0);


        double currentLevelRatioDiff = userStatus.getUserTestTaskStatuses().stream()
                .filter(taskStatus -> taskStatus.getTestTask().getLevel() == userStatus.getCurrentLevel())
                .mapToDouble(taskStatus -> taskStatus.getRatioInCurrentCycle() - taskStatus.getTestTask().getRatio())
                .average()
                .orElse(0);
        double currentAllRatioDiff = userStatus.getUserTestTaskStatuses().stream()
                .mapToDouble(taskStatus -> taskStatus.getRatioInCurrentCycle() - taskStatus.getTestTask().getRatio())
                .average()
                .orElse(0);

        boolean statusChanged = false;
        switch (userStatus.getStatus()) {
            case NOT_STARTED:
                userStatus.setStatus(UserTestStatus.Status.IN_PROGRESS);
                statusChanged = true;
                break;
            case IN_PROGRESS:
                if (currentAllRatioDiff < -0.5 || currentLevelRatioDiff < -0.5
                        || userStatus.getWrongSolutionsInRow() >= 2) {
                    userStatus.setStatus(UserTestStatus.Status.PROBLEM);
                    statusChanged = true;
                }
                break;
            case PROBLEM:
                if (currentAllRatioDiff > -0.5 && currentLevelRatioDiff > -0.5
                        && userStatus.getCorrectSolutionsInRow() >= 2) {
                    userStatus.setStatus(UserTestStatus.Status.IN_PROGRESS);
                    statusChanged = true;
                }
                break;
        }
        if (statusChanged) {
            userStatus.setStatusChangedDate(dateUtil.getCurrentTime());
        }
        return statusChanged;
    }

    @Transactional
    public TestTask getNextTask(Long userId, Long testId) {
        UserTestStatus userStatus = getUserTestStatus(userId, testId, 2);

        if (userStatus.getCurrentTestTask() == null || userStatus.isCurrentTestTaskSolved()) {
            calculateNextTask(userStatus);
            userStatus = userTestStatusRepository.save(userStatus, 2);
        }

        return userStatus.getCurrentTestTask();
    }

    public void calculateNextTask(UserTestStatus userStatus) {
        int currentLevel = userStatus.getCurrentLevel();
        int currentCycle = userStatus.getCurrentCycle();

        if (userStatus.getTest().getTestTasks().isEmpty()) {
            return;
        }

        Set<TestTask> allTestTasks = userStatus.getTest().getTestTasks().stream()
                .filter(testTask -> testTask.getLevel() == currentLevel)
                .collect(Collectors.toSet());
        Set<UserTestTaskStatus> solvedTaskStatuses = userStatus.getUserTestTaskStatuses().stream()
                .filter(status -> (status.getTestTask().getLevel() == currentLevel)
                        && allTestTasks.contains(status.getTestTask()))
                .collect(Collectors.toSet());
        Set<TestTask> newTestTasks = allTestTasks.stream()
                .filter(testTask ->
                        solvedTaskStatuses.stream().noneMatch(status -> status.getTestTask().equals(testTask)))
                .collect(Collectors.toSet());

        if (allTestTasks.isEmpty()) {
            increaseLevel(userStatus);
            calculateNextTask(userStatus);
            return;
        }

        if (!newTestTasks.isEmpty()) {
            TestTask nextTask = getEasiestTestTask(newTestTasks);
            userStatus.setNewCurrentTestTask(nextTask);
            return;
        }

        Set<UserTestTaskStatus> newInCurrentCycle = solvedTaskStatuses.stream()
                .filter(taskStatus -> taskStatus.getAllSolutionsInCurrentCycle() == 0)
                .collect(Collectors.toSet());
        if (!newInCurrentCycle.isEmpty()) {
            TestTask nextTask = getCurrentEasiestTestTask(newInCurrentCycle);
            userStatus.setNewCurrentTestTask(nextTask);
            return;
        }

        Set<UserTestTaskStatus> lastAnswerWrong = solvedTaskStatuses.stream()
                .filter(taskStatus -> taskStatus.getWrongSolutionsInRow() > 0)
                .collect(Collectors.toSet());
        if (!lastAnswerWrong.isEmpty()) {
            TestTask nextTask = getCurrentEasiestTestTask(getTaskStatusesWithLessSolution(lastAnswerWrong));
            userStatus.setNewCurrentTestTask(nextTask);
            return;
        }

        if (currentCycle == 1) {
            double averageRatio = solvedTaskStatuses.stream().mapToDouble(UserTestTaskStatus::getRatio).average().orElse(0);
            if (averageRatio < 0.5) {
                TestTask nextTask = getCurrentHardestTestTask(getTaskStatusesWithLessSolution(solvedTaskStatuses));
                userStatus.setNewCurrentTestTask(nextTask);
                return;
            }
        } else {
            double currentAverageRatio = solvedTaskStatuses.stream().mapToDouble(UserTestTaskStatus::getRatioInCurrentCycle).average().orElse(0);
            double prevAverageRatio = solvedTaskStatuses.stream().mapToDouble(UserTestTaskStatus::getRatioInPrevCycle).average().orElse(0);
            if (currentAverageRatio < prevAverageRatio) {
                TestTask nextTask = getCurrentHardestTestTask(getTaskStatusesWithLessSolution(solvedTaskStatuses));
                userStatus.setNewCurrentTestTask(nextTask);
                return;
            }
        }

        Set<UserTestTaskStatus> bigDiffInRatio = solvedTaskStatuses.stream()
                .filter(taskStatus -> {
                    return (taskStatus.getRatioInCurrentCycle() - taskStatus.getTestTask().getRatio() < -0.5)
                            || (taskStatus.getRatioInPrevCycle() - taskStatus.getTestTask().getRatio() < -0.5);
                })
                .collect(Collectors.toSet());
        if (!bigDiffInRatio.isEmpty()) {
            TestTask nextTask = getCurrentEasiestTestTask(getTaskStatusesWithLessSolution(bigDiffInRatio));
            userStatus.setNewCurrentTestTask(nextTask);
            return;
        }

        increaseLevel(userStatus);
        calculateNextTask(userStatus);
    }

    private void increaseLevel(UserTestStatus userStatus) {
        if (userStatus.getCurrentLevel() < MAX_LEVEL) {
            userStatus.increaseLevel();
        } else {
            userStatus.increaseCycle();
            for (UserTestTaskStatus taskStatus : userStatus.getUserTestTaskStatuses()) {
                taskStatus.setNewCycle();
            }
        }
    }

    private Set<UserTestTaskStatus> getTaskStatusesWithLessSolution(Set<UserTestTaskStatus> taskStatuses) {
        int lessSolution = Collections.min(taskStatuses.stream()
                .map(UserTestTaskStatus::getAllSolutionsInCurrentCycle)
                .collect(Collectors.toSet()));
        return taskStatuses.stream().
                filter(taskStatus -> taskStatus.getAllSolutionsInCurrentCycle() == lessSolution)
                .collect(Collectors.toSet());
    }

    private TestTask getCurrentEasiestTestTask(Set<UserTestTaskStatus> taskStatuses) {
        UserTestTaskStatus maxDiff = Collections.max(taskStatuses,
                (status1, status2) -> {
                    double diffRatio1 = status1.getRatioInCurrentCycle() - status1.getTestTask().getRatio();
                    double diffRatio2 = status2.getRatioInCurrentCycle() - status2.getTestTask().getRatio();
                    return (diffRatio1 > diffRatio2) ? 1 : -1;
                });
        return maxDiff.getTestTask();
    }

    private TestTask getCurrentHardestTestTask(Set<UserTestTaskStatus> taskStatuses) {
        UserTestTaskStatus minDiff = Collections.max(taskStatuses,
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

    //todo get userId from auth
    private UserTestStatus getUserTestStatus(Long userId, Long testId, int depth) {
        return userTestStatusRepository
                .findFirstByUserIdAndTestId(userId, testId, depth)
                .orElseThrow(() -> new InvalidInputException("No UserTestStatus available for this Test", "userId"));
    }


}
