package com.thesis.studyapp.service;


import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.dto.UserTestStatusDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.repository.UserTestStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    //TODO ilyenkor testId check? pl get Test with depth 1, filter userId == userId && deprecated == false
    // vagy ez ne is legyen itt mert csak objectresolverből hívjuk?
    // (FONTOS: objectresolvernél ha errort dobunk egy filednél, attól még a többi meglehet (külön folyamat))
    public List<UserTestStatusDto> getUserTestStatusesForTest(Long testId) {
        //todo check test?
        return convertToDto(userTestStatusRepository.findByDeprecatedIsFalseAndTestIdIs(testId, 1));
    }

    public List<UserTestStatusDto> getUserTestStatusesForUser(Long userId) {
        //todo check user?
        return convertToDto(userTestStatusRepository.findByDeprecatedIsFalseAndUserIdIs(userId, 1));
    }

    private UserTestStatus getUserTestStatusById(Long userTestStatusId, int depth) {
        return userTestStatusRepository.findById(userTestStatusId, Math.max(1, depth))
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


    //todo visszatérést kilehetne egészíteni plusz infókkal?
    public int checkSolution(Long userId, Long testId, Long chosenAnswerNumber) {
        UserTestStatus userTestStatus = userTestStatusRepository
                .findFirstByUserIdAndTestIdAndDeprecatedIsFalse(userId, testId, 1)
                .orElseThrow(() -> new CustomGraphQLException("No UserTestStatus available"));
        int correctSolutionNumber = userTestStatus.getCurrentTask().getSolutionNumber();
        if (correctSolutionNumber == chosenAnswerNumber) {
            userTestStatus.setCorrectAnswers(userTestStatus.getCorrectAnswers() + 1);
            userTestStatus.setCorrectAnswersInRow(userTestStatus.getCorrectAnswersInRow() + 1);
            userTestStatus.setWrongAnswersInRow(0);
        } else {
            userTestStatus.setWrongAnswers(userTestStatus.getWrongAnswers() + 1);
            userTestStatus.setWrongAnswersInRow(userTestStatus.getWrongAnswersInRow() + 1);
            userTestStatus.setCorrectAnswersInRow(0);
        }
        return correctSolutionNumber;
    }

    //TODO todo todo !!!!!!!! Ez itt exception lesz, merrt a taskanswerek nem jönnek!
    //todo ezt hova? kilehetne egészíteni plusz infókkal?
    public TaskDto getNextTask(Long userId, Long testId) {
        UserTestStatus userTestStatus = userTestStatusRepository
                .findFirstByUserIdAndTestIdAndDeprecatedIsFalse(userId, testId, 2)
                .orElseThrow(() -> new CustomGraphQLException("No UserTestStatus available")); //TODO, ez csak query, kell-e?
        return TaskDto.build(userTestStatus.getCurrentTask());
    }

}
