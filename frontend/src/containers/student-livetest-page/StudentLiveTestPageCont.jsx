import React, { useEffect, useState } from 'react';
import gql from 'graphql-tag';
import toast from 'toasted-notes';
import { useMutation } from '@apollo/client';
import { useHistory, useParams } from 'react-router-dom';
import { useAuthentication } from '../../AuthService';
import LoadingComp from '../../components/common/LoadingComp';
import StudentLiveTestPageComp from '../../components/student-livetest-page/StudentLiveTestPageComp';

const NEXT_TASK_MUTATION = gql`
  mutation CalculateNextTask($testId: ID!) {
    calculateNextTask(testId: $testId) {
      id
      currentTestTask {
        id
        explanation
        task {
          id
          question
          answers {
            id
            number
            answer
          }
          solutionNumber
        }
      }
    }
  }
`;

const CHECK_TASK_SOLUTION_MUTATION = gql`
  mutation CheckSolution($testId: ID!, $solutionNumber: Int!) {
    checkSolution(testId: $testId, solutionNumber: $solutionNumber) {
      solutionNumber
      allSolutions
      correctSolutions
      solvedTasks
      allTasks
    }
  }
`;

export default function StudentLiveTestPageCont() {
  const history = useHistory();
  const { testid: testId } = useParams();
  const { userId } = useAuthentication();
  const [chosenAnswerNumber, chooseAnswerNumber] = useState(null);

  const [nextTask, { data: nextTaskData }] = useMutation(NEXT_TASK_MUTATION, {
    errorPolicy: 'ignore',
  });

  const [checkSolution, { data: solutionData }] = useMutation(
    CHECK_TASK_SOLUTION_MUTATION,
    {
      onError: () => toast.notify('Error :('),
    }
  );

  useEffect(() => {
    nextTask({ variables: { testId } });
    // eslint-disable-next-line
  }, [testId]);

  if (!nextTaskData?.calculateNextTask?.currentTestTask) {
    return (
      <div onClick={() => history.goBack()}>
        <LoadingComp text="No task available" />
      </div>
    );
  }

  return (
    <StudentLiveTestPageComp
      chosenAnswerNumber={chosenAnswerNumber}
      testTask={nextTaskData.calculateNextTask.currentTestTask}
      solution={solutionData?.checkSolution}
      isAnswered={chosenAnswerNumber !== null}
      onNavigateBack={() => history.goBack()}
      onSolution={(answerNumber) => {
        checkSolution({
          variables: { testId, solutionNumber: answerNumber },
        });
        chooseAnswerNumber(answerNumber);
      }}
      onNextTask={() => {
        nextTask({
          variables: { userId, testId },
        });
        chooseAnswerNumber(null);
      }}
    />
  );
}
