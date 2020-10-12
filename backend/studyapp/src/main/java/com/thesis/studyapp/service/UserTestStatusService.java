package com.thesis.studyapp.service;


import com.thesis.studyapp.dto.TaskSolutionDto;
import com.thesis.studyapp.event.UpdatedStatusEvent;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.model.UserTestTaskStatus;
import com.thesis.studyapp.repository.UserTestStatusRepository;
import com.thesis.studyapp.repository.UserTestTaskStatusRepository;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    public UserTestStatus getUserTestStatus(Long userTestStatusId) {
        return getUserTestStatusById(userTestStatusId, 1);
    }

    public List<UserTestStatus> getUserTestStatusesByIds(List<Long> ids) {
        return userTestStatusRepository.findByIdIn(ids, 1);
    }

    public List<UserTestTaskStatus> getUserTestTaskStatusesByIds(List<Long> ids) {
        return userTestTaskStatusRepository.findByIdIn(ids, 1);
    }

    //todo (FONTOS: objectresolvernél ha errort dobunk egy filednél, attól még a többi meglehet (külön folyamat))
    public List<UserTestStatus> getUserTestStatusesForTest(Long testId) {
        return userTestStatusRepository.findByTestIdOrderByUserName(testId, 1);
    }

    public List<UserTestStatus> getUserTestStatusesForUser(Long userId) {
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
        boolean statusChanged = setStatus(userTestStatus);
        userTestStatus.setCurrentTestTask(null);
        userTestStatus = userTestStatusRepository.save(userTestStatus, 2);
        if(statusChanged) {
            eventPublisher.publishEvent(new UpdatedStatusEvent(this, userTestStatus.getId()));
        }
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

    private boolean setStatus(UserTestStatus uts) {
        double ratio = uts.getRatio();
        boolean statusChanged = false;
        switch (uts.getStatus()) {
            case NOT_STARTED:
                uts.setStatus(UserTestStatus.Status.IN_PROGRESS);
                statusChanged = true;
                break;
            case IN_PROGRESS:
                if (ratio < 0.5 || uts.getWrongSolutionsInRow() >= 2) {
                    uts.setStatus(UserTestStatus.Status.PROBLEM);
                    statusChanged = true;
                }
                break;
            case PROBLEM:
                if (ratio > 0.5 && uts.getCorrectSolutionsInRow() >= 2) {
                    uts.setStatus(UserTestStatus.Status.IN_PROGRESS);
                    statusChanged = true;
                }
                break;
        }
        if(statusChanged) {
            uts.setStatusChangedDate(dateUtil.getCurrentTime());
        }
        return statusChanged;
    }


    public TestTask getNextTask(Long userId, Long testId) {
        UserTestStatus userTestStatus = userTestStatusRepository
                .findFirstByUserIdAndTestId(userId, testId, 2)
                .orElseThrow(() -> new CustomGraphQLException("No UserTestStatus available"));

        calculateNextTask(userTestStatus);

        userTestStatus = userTestStatusRepository.save(userTestStatus, 2);
        return userTestStatus.getCurrentTestTask();
    }

    public void calculateNextTask(UserTestStatus userStatus) {
        if (userStatus.getTest().getTestTasks().isEmpty()) {
            return;
        }
        int currentLevel = userStatus.getCurrentLevel();
        int currentCycle = userStatus.getCurrentCycle();
        Set<TestTask> allTestTasks = userStatus.getTest().getTestTasks().stream()
                .filter(testTask -> testTask.getLevel() == currentLevel)
                .collect(Collectors.toSet());
        if (allTestTasks.isEmpty()) {
            return;
        }
        Set<UserTestTaskStatus> solvedUserTestTaskStatuses = userStatus.getUserTestTaskStatuses().stream()
                .filter(status -> status.getTestTask().getLevel() == currentLevel && allTestTasks.contains(status.getTestTask()))
                .collect(Collectors.toSet());
        Set<TestTask> solvedTestTasks = solvedUserTestTaskStatuses.stream()
                .map(UserTestTaskStatus::getTestTask)
                .collect(Collectors.toSet());
        Set<TestTask> newTestTasks = allTestTasks.stream()
                .filter(testTask -> !solvedTestTasks.contains(testTask))
                .collect(Collectors.toSet());

        if (!newTestTasks.isEmpty()) {
            TestTask nextTask = Collections.max(newTestTasks, Comparator.comparing(TestTask::getRatio));
            userStatus.setCurrentTestTask(nextTask);
            return;
        }

        double weight = max(1, 0.75 + 0.05 * currentCycle);
        if (solvedUserTestTaskStatuses.stream().anyMatch(status -> status.getCorrectSolutions() < currentCycle)
                || weight * calculateAverageRatio(solvedUserTestTaskStatuses) + (1 - weight) * calculatePreviousAverageRatio(userStatus.getUser()) < (weight - 0.05)) {
            //Todo ez biztos nem mindig ugyanazt adja?
            TestTask nextTask = Collections.max(solvedUserTestTaskStatuses, Comparator.comparing(UserTestTaskStatus::getRatio)).getTestTask();
            userStatus.setCurrentTestTask(nextTask);
            return;
        }

        Set<TestTask> wrongTestTasks = solvedUserTestTaskStatuses.stream()
                .filter(status -> status.getWrongSolutionsInRow() > 0)
                .map(UserTestTaskStatus::getTestTask)
                .collect(Collectors.toSet());
        if (!wrongTestTasks.isEmpty()) {
            TestTask nextTask = Collections.max(wrongTestTasks, Comparator.comparing(TestTask::getRatio));
            userStatus.setCurrentTestTask(nextTask);
            return;
        }

        if (currentLevel < 10) {
            userStatus.setCurrentLevel(currentLevel + 1);
        } else {
            userStatus.setCurrentLevel(1);
            userStatus.setCurrentCycle(currentCycle + 1);
            calculateNextTask(userStatus);
        }
    }

    public static double calculatePreviousAverageRatio(User user) {
        return user.getUserTestStatuses().stream()
                .mapToDouble(UserTestStatus::getRatio)
                .average()
                .orElse(0);
    }

    public static double calculateAverageRatio(Set<UserTestTaskStatus> userTestTaskStatuses) {
        return userTestTaskStatuses.stream()
                .mapToDouble(UserTestTaskStatus::getRatio)
                .average()
                .orElse(0);
    }

//    @Data
//    @Builder
//    private static class SolvingData {
//        private int currentLevel;
//        private int currentCycle;
//        private UserTestStatus userTestStatus;
//        private Set<TestTask> allTestTasks;
//        private Set<UserTestTaskStatus> solvedUserTestTaskStatuses;
//        private Set<TestTask> newTestTasks;
//
//        public SolvingData build(UserTestStatus uts) {
//            Set<TestTask> allTestTasks = uts.getTest().getTestTasks().stream()
//                    .filter(testTask -> testTask.getLevel() == uts.getCurrentLevel())
//                    .collect(Collectors.toSet());
//            Set<UserTestTaskStatus> solvedUserTestTaskStatuses = uts.getUserTestTaskStatuses().stream()
//                    .filter(status -> status.getTestTask().getLevel() == uts.getCurrentLevel() && allTestTasks.contains(status.getTestTask()))
//                    .collect(Collectors.toSet());
//            Set<TestTask> solvedTestTasks = solvedUserTestTaskStatuses.stream()
//                    .map(UserTestTaskStatus::getTestTask)
//                    .collect(Collectors.toSet());
//            Set<TestTask> newTestTasks = allTestTasks.stream()
//                    .filter(testTask -> !solvedTestTasks.contains(testTask))
//                    .collect(Collectors.toSet());
//            SolvingData.builder()
//                    .currentLevel
//                    .allTestTasks()
//                    .solvedUserTestTaskStatuses()
//                    .solvedTestTasks()
//                    .newTestTasks();
//        }
//
//    }

}
