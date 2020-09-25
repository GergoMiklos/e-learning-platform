package com.thesis.studyapp.service;


import com.thesis.studyapp.dto.TaskSolutionDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.model.UserTestTaskStatus;
import com.thesis.studyapp.repository.UserTestStatusRepository;
import com.thesis.studyapp.repository.UserTestTaskStatusRepository;
import com.thesis.studyapp.util.DateUtil;
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
    private final UserTestTaskStatusRepository userTestTaskStatusRepository;

    private final DateUtil dateUtil;

    public UserTestStatus getUserTestStatus(Long userTestStatusId) {
        return getUserTestStatusById(userTestStatusId, 1);
    }

    public List<UserTestStatus> getUserTestStatusesByIds(List<Long> ids) {
        return userTestStatusRepository.findByIdIn(ids, 1);
    }

    public List<UserTestTaskStatus> getUserTestTaskStatusesByIds(List<Long> ids) {
        return userTestTaskStatusRepository.findByIdIn(ids, 1);
    }

    //TODO ilyenkor testId check? vagy pl get Test with depth 1, filter userId == userId && deprecated == false
    // vagy ez ne is legyen itt mert csak objectresolverből hívjuk?
    // (FONTOS: objectresolvernél ha errort dobunk egy filednél, attól még a többi meglehet (külön folyamat))
    public List<UserTestStatus> getUserTestStatusesForTest(Long testId) {
        //todo check test?
        return userTestStatusRepository.findByTestIdOrderByUserName(testId, 1);
    }

    public List<UserTestStatus> getUserTestStatusesForUser(Long userId) {
        //todo check user?
        return userTestStatusRepository.findByUserIdOrderByTestName(userId, 1);
    }

    private UserTestStatus getUserTestStatusById(Long userTestStatusId, int depth) {
        return userTestStatusRepository.findById(userTestStatusId, max(1, depth))
                .orElseThrow(() -> new CustomGraphQLException("No UserTestStatus with id: " + userTestStatusId));
    }

    //todo dto?
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
                .correctSolutions(userTestStatus.getCorrectSolutions())
                .allSolutions(userTestStatus.getAllSolutions())
                .solvedTasks(userTestStatus.getUserTestTaskStatuses().size())
                .allTasks(userTestStatus.getTest().getTestTasks().size())
                .build();
    }

    //todo mehetnek ki függvénybe a dolgok
    private void setCorrectSolution(UserTestStatus uts) {
        uts.setCorrectSolutions(uts.getCorrectSolutions() + 1);
        uts.setAllSolutions(uts.getAllSolutions() + 1);
        uts.setCorrectSolutionsInRow(uts.getCorrectSolutionsInRow() + 1);
        uts.setWrongSolutionsInRow(0);

        Optional<UserTestTaskStatus> taskStatusData = uts.getUserTestTaskStatuses().stream()
                .filter(tsd -> tsd.getTestTask().getId().equals(uts.getCurrentTestTask().getId()))
                .findFirst();
        if (taskStatusData.isPresent()) {
            //Todo extract to func.
            taskStatusData.get().setCorrectSolutions(uts.getCorrectSolutions() + 1);
            taskStatusData.get().setAllSolutions(uts.getAllSolutions() + 1);
            taskStatusData.get().setCorrectSolutionsInRow(uts.getCorrectSolutionsInRow() + 1);
            taskStatusData.get().setWrongSolutionsInRow(0);
        } else {
            uts.addUserTestTaskStatus(UserTestTaskStatus.builder()
                    .testTask(uts.getCurrentTestTask())
                    .lastSolutionTime(dateUtil.getCurrentTime())
                    .allSolutions(1)
                    .correctSolutions(1)
                    .correctSolutionsInRow(1)
                    .wrongSolutionsInRow(0)
                    .build());
        }
    }

    private void setWrongSolution(UserTestStatus uts) {
        uts.setAllSolutions(uts.getAllSolutions() + 1);
        uts.setCorrectSolutionsInRow(0);
        uts.setWrongSolutionsInRow(uts.getWrongSolutionsInRow() + 1);

        Optional<UserTestTaskStatus> taskStatusData = uts.getUserTestTaskStatuses().stream()
                .filter(tsd -> tsd.getTestTask().getId().equals(uts.getCurrentTestTask().getId()))
                .findFirst();
        if (taskStatusData.isPresent()) {
            taskStatusData.get().setAllSolutions(uts.getAllSolutions() + 1);
            taskStatusData.get().setCorrectSolutionsInRow(0);
            taskStatusData.get().setWrongSolutionsInRow(0);
            taskStatusData.get().setLastSolutionTime(dateUtil.getCurrentTime());
        } else {
            uts.addUserTestTaskStatus(UserTestTaskStatus.builder()
                    .testTask(uts.getCurrentTestTask())
                    .lastSolutionTime(dateUtil.getCurrentTime())
                    .allSolutions(1)
                    .correctSolutions(0)
                    .correctSolutionsInRow(0)
                    .wrongSolutionsInRow(1)
                    .build());
        }
    }

    private void setStatus(UserTestStatus uts) {
        double ratio = calculateRatio(uts.getCorrectSolutions(), uts.getAllSolutions());
        switch (uts.getStatus()) {
            case NOT_STARTED:
                uts.setStatus(UserTestStatus.Status.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                if (ratio < 0.5 || uts.getWrongSolutionsInRow() >= 2) {
                    uts.setStatus(UserTestStatus.Status.PROBLEM);
                }
                break;
            case PROBLEM:
                if (ratio > 0.5 && uts.getCorrectSolutionsInRow() >= 2) {
                    uts.setStatus(UserTestStatus.Status.IN_PROGRESS);
                }
                break;
        }
    }


    public TestTask getNextTask(Long userId, Long testId) {
        UserTestStatus userTestStatus = userTestStatusRepository
                .findFirstByUserIdAndTestId(userId, testId, 2)
                .orElseThrow(() -> new CustomGraphQLException("No UserTestStatus available")); //TODO, ez csak query, kell-e?

        calculateNextTask(userTestStatus);

        userTestStatus = userTestStatusRepository.save(userTestStatus, 2);
        return userTestStatus.getCurrentTestTask();
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
        Set<UserTestTaskStatus> solvedUserTestTaskStatuses = uts.getUserTestTaskStatuses().stream()
                .filter(status -> status.getTestTask().getLevel() == currentLevel && allTestTasks.contains(status.getTestTask()))
                .collect(Collectors.toSet());
        Set<TestTask> solvedTestTasks = solvedUserTestTaskStatuses.stream()
                .map(UserTestTaskStatus::getTestTask)
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
            if (solvedUserTestTaskStatuses.stream().anyMatch(status -> status.getCorrectSolutions() < currentCycle)
                    || weight * calculateAverageRatio(solvedUserTestTaskStatuses) + (1 - weight) * calculatePreviousAverageRatio(uts.getUser()) < (weight - 0.05)) {
                //Todo ez biztos nem mindig ugyanazt adja?
                TestTask nextTask = Collections.min(solvedTestTasks, new BestRatioComparator());
                uts.setCurrentTestTask(nextTask);
                return;
            }

            Set<TestTask> wrongTestTasks = solvedUserTestTaskStatuses.stream()
                    .filter(status -> status.getWrongSolutionsInRow() > 0)
                    .map(UserTestTaskStatus::getTestTask)
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
                .mapToDouble(uts -> calculateRatio(uts.getCorrectSolutions(), uts.getAllSolutions()))
                .average()
                .orElse(1);
    }

    public static double calculateAverageRatio(Set<UserTestTaskStatus> userTestTaskStatuses) {
        return userTestTaskStatuses.stream()
                .mapToDouble(status -> calculateRatio(status.getCorrectSolutions(), status.getAllSolutions()))
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
            double ratio1 = calculateRatio(tt1.getCorrectSolutions(), tt1.getAllSolutions());
            double ratio2 = calculateRatio(tt2.getCorrectSolutions(), tt2.getAllSolutions());
            if (ratio1 > ratio2) {
                return 1;
            } else {
                return -1;
            }
        }
    }

}
