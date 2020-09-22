package com.thesis.studyapp.service;


import com.thesis.studyapp.dto.TaskSolutionDto;
import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.dto.UserTestStatusDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.repository.UserTestStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
public class UserTestStatusService {

    private final UserTestStatusRepository userTestStatusRepository;

    public UserTestStatusDto getUserTestStatus(Long userTestStatusId) {
        return convertToDto(getUserTestStatusById(userTestStatusId, 1));
    }

    public List<UserTestStatusDto> getUserTestStatusesByIds(List<Long> ids) {
        return convertToDto(userTestStatusRepository.findByIdIn(ids, 1));
    }

    //TODO ilyenkor testId check? vagy pl get Test with depth 1, filter userId == userId && deprecated == false
    // vagy ez ne is legyen itt mert csak objectresolverből hívjuk?
    // (FONTOS: objectresolvernél ha errort dobunk egy filednél, attól még a többi meglehet (külön folyamat))
    public List<UserTestStatusDto> getUserTestStatusesForTest(Long testId) {
        //todo check test?
        return convertToDto(userTestStatusRepository.findByTestIdOrderByUserName(testId, 1));
    }

    public List<UserTestStatusDto> getUserTestStatusesForUser(Long userId) {
        //todo check user?
        return convertToDto(userTestStatusRepository.findByUserIdOrderByTestName(userId, 1));
    }

    private UserTestStatus getUserTestStatusById(Long userTestStatusId, int depth) {
        return userTestStatusRepository.findById(userTestStatusId, max(1, depth))
                .orElseThrow(() -> new CustomGraphQLException("No UserTestStatus with id: " + userTestStatusId));
    }

    private UserTestStatusDto convertToDto(UserTestStatus userTestStatus) {
        return UserTestStatusDto.build(userTestStatus);
    }

    private List<UserTestStatusDto> convertToDto(List<UserTestStatus> userTestStatuses) {
        return userTestStatuses.stream()
                .map(UserTestStatusDto::build)
                .collect(Collectors.toList());
    }

    //TODO set TesTask correct / all
    public TaskSolutionDto checkSolution(Long userId, Long testId, int chosenAnswerNumber) {
        UserTestStatus userTestStatus = userTestStatusRepository
                .findFirstByUserIdAndTestId(userId, testId, 2)
                .orElseThrow(() -> new CustomGraphQLException("No UserTestStatus available"));
        if (userTestStatus.getCurrentTestTask() == null) {
            throw new CustomGraphQLException("No current task available");
        }
        int correctSolutionNumber = userTestStatus.getCurrentTestTask().getTask().getSolutionNumber();
        if (correctSolutionNumber == chosenAnswerNumber) {
            setCorrectSolution(userTestStatus);
        } else {
            setWrongSolution(userTestStatus);
        }
        setStatus(userTestStatus);
        userTestStatus.setCurrentTestTask(null);
        userTestStatus = userTestStatusRepository.save(userTestStatus, 2);
        return TaskSolutionDto.builder()
                .chosenAnswerNumber(chosenAnswerNumber)
                .solutionNumber(correctSolutionNumber)
                .correctAnswers(userTestStatus.getCorrectAnswers())
                .allAnswers(userTestStatus.getAllAnswers())
                .answeredTasks(userTestStatus.getTaskStatuses().size())
                .allTasks(userTestStatus.getTest().getTestTasks().size())
                .build();
    }

    //todo mehetnek ki függvénybe a dolgok
    private void setCorrectSolution(UserTestStatus uts) {
        uts.setCorrectAnswers(uts.getCorrectAnswers() + 1);
        uts.setAllAnswers(uts.getAllAnswers() + 1);
        uts.setCorrectAnswersInRow(uts.getCorrectAnswersInRow() + 1);
        uts.setWrongAnswersInRow(0);

        Optional<UserTestStatus.TaskStatus> taskStatusData = uts.getTaskStatuses().stream()
                .filter(tsd -> tsd.getTestTask().getId().equals(uts.getCurrentTestTask().getId()))
                .findFirst();
        if (taskStatusData.isPresent()) {
            //Todo extract to func.
            taskStatusData.get().setCorrectAnswers(uts.getCorrectAnswers() + 1);
            taskStatusData.get().setAllAnswers(uts.getAllAnswers() + 1);
            taskStatusData.get().setCorrectAnswersInRow(uts.getCorrectAnswersInRow() + 1);
            taskStatusData.get().setWrongAnswersInRow(0);
        } else {
            uts.addTaskStatusData(UserTestStatus.TaskStatus.builder()
                    .testTask(uts.getCurrentTestTask())
                    .allAnswers(1)
                    .correctAnswers(1)
                    .correctAnswersInRow(1)
                    .wrongAnswersInRow(0)
                    .build());
        }
    }

    private void setWrongSolution(UserTestStatus uts) {
        uts.setAllAnswers(uts.getAllAnswers() + 1);
        uts.setCorrectAnswersInRow(0);
        uts.setWrongAnswersInRow(uts.getWrongAnswersInRow() + 1);

        Optional<UserTestStatus.TaskStatus> taskStatusData = uts.getTaskStatuses().stream()
                .filter(tsd -> tsd.getTestTask().getId().equals(uts.getCurrentTestTask().getId()))
                .findFirst();
        if (taskStatusData.isPresent()) {
            taskStatusData.get().setAllAnswers(uts.getAllAnswers() + 1);
            taskStatusData.get().setCorrectAnswersInRow(0);
            taskStatusData.get().setWrongAnswersInRow(0);
        } else {
            uts.addTaskStatusData(UserTestStatus.TaskStatus.builder()
                    .testTask(uts.getCurrentTestTask())
                    .allAnswers(1)
                    .correctAnswers(0)
                    .correctAnswersInRow(0)
                    .wrongAnswersInRow(1)
                    .build());
        }
    }

    private void setStatus(UserTestStatus uts) {
        double ratio = calculateRatio(uts.getCorrectAnswers(), uts.getAllAnswers());
        switch (uts.getStatus()) {
            case NOT_STARTED:
                uts.setStatus(UserTestStatus.Status.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                if (ratio < 0.5 || uts.getWrongAnswersInRow() >= 2) {
                    uts.setStatus(UserTestStatus.Status.PROBLEM);
                }
                break;
            case PROBLEM:
                if (ratio > 0.5 && uts.getCorrectAnswersInRow() >= 2) {
                    uts.setStatus(UserTestStatus.Status.IN_PROGRESS);
                }
                break;
        }
    }


    public TestTaskDto getNextTask(Long userId, Long testId) {
        UserTestStatus userTestStatus = userTestStatusRepository
                .findFirstByUserIdAndTestId(userId, testId, 2)
                .orElseThrow(() -> new CustomGraphQLException("No UserTestStatus available")); //TODO, ez csak query, kell-e?

        calculateNextTask(userTestStatus);

        userTestStatus = userTestStatusRepository.save(userTestStatus, 2);
        return TestTaskDto.build(userTestStatus.getCurrentTestTask());
    }

    public void calculateNextTask(UserTestStatus uts) {
        if (uts.getTest().getTestTasks().isEmpty()) {
            return;
        }
        int currentLevel = uts.getCurrentLevel();
        int currentCycle = uts.getCurrentCycle();
        Set<TestTask> allTestTasks = uts.getTest().getTestTasks().stream()
                .filter(testTask -> testTask.getLevel() == currentLevel)
                .collect(Collectors.toSet());
        Set<UserTestStatus.TaskStatus> solvedTaskStatuses = uts.getTaskStatuses().stream()
                .filter(status -> status.getTestTask().getLevel() == currentLevel && allTestTasks.contains(status.getTestTask()))
                .collect(Collectors.toSet());
        Set<TestTask> solvedTestTasks = solvedTaskStatuses.stream()
                .map(UserTestStatus.TaskStatus::getTestTask)
                .collect(Collectors.toSet());
        Set<TestTask> newTestTasks = allTestTasks.stream()
                .filter(testTask -> !solvedTestTasks.contains(testTask))
                .collect(Collectors.toSet());
        if (!allTestTasks.isEmpty()) {
            if (!newTestTasks.isEmpty()) {
                TestTask nextTask = Collections.max(newTestTasks, new BestRatioComparator());
                uts.setCurrentTestTask(nextTask);
                return;
            }

            double weight = max(1, 0.75 + 0.05 * currentCycle);
            if (solvedTaskStatuses.stream().anyMatch(status -> status.getCorrectAnswers() < currentCycle)
                    || weight * calculateAverageRatio(solvedTaskStatuses) + (1 - weight) * calculatePreviousAverageRatio(uts.getUser()) < (weight - 0.05)) {
                //Todo ez biztos nem mindig ugyanazt adja?
                TestTask nextTask = Collections.min(solvedTestTasks, new BestRatioComparator());
                uts.setCurrentTestTask(nextTask);
                return;
            }

            Set<TestTask> wrongTestTasks = solvedTaskStatuses.stream()
                    .filter(status -> status.getWrongAnswersInRow() > 0)
                    .map(UserTestStatus.TaskStatus::getTestTask)
                    .collect(Collectors.toSet());
            if (!wrongTestTasks.isEmpty()) {
                TestTask nextTask = Collections.max(wrongTestTasks, new BestRatioComparator());
                uts.setCurrentTestTask(nextTask);
                return;
            }
        }
        if (currentLevel < 10) {
            uts.setCurrentLevel(currentLevel + 1);
        } else {
            uts.setCurrentLevel(1);
            uts.setCurrentCycle(currentCycle + 1);
            calculateNextTask(uts);
        }
    }

    public static double calculatePreviousAverageRatio(User user) {
        return user.getUserTestStatuses().stream()
                .mapToDouble(uts -> calculateRatio(uts.getCorrectAnswers(), uts.getAllAnswers()))
                .average()
                .orElse(1);
    }

    public static double calculateAverageRatio(Set<UserTestStatus.TaskStatus> taskStatus) {
        return taskStatus.stream()
                .mapToDouble(status -> calculateRatio(status.getCorrectAnswers(), status.getAllAnswers()))
                .average()
                .orElse(1);
    }

    public static double calculateRatio(int correct, int all) {
        if (all != 0) {
            return (double) correct / (double) all;
        } else {
            return 1;
        }
    }

    public static class BestRatioComparator implements Comparator<TestTask> {

        @Override public int compare(TestTask tt1, TestTask tt2) {
            double ratio1 = (double) tt1.getCorrectAnswers() / (double) tt1.getAllAnswers();
            double ratio2 = (double) tt2.getCorrectAnswers() / (double) tt2.getAllAnswers();
            if (ratio1 > ratio2) {
                return 1;
            } else {
                return -1;
            }
        }
    }

}
