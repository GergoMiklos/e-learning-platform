import React, { useState } from 'react';
import toast from 'toasted-notes';
import gql from 'graphql-tag';
import { useMutation } from '@apollo/client';
import PropTypes, { number, string } from 'prop-types';
import EditTestElementComp from '../../components/edit-test-page/EditTestElementComp';
import { taskLevels } from '../../constants';

const DELETE_TASK_MUTATION = gql`
  mutation DeleteTaskFromTest($testTaskId: ID!) {
    deleteTaskFromTest(testTaskId: $testTaskId)
  }
`;

const TESTTASK_DETAILS_FRAGMENT = gql`
  fragment TestTaskDetails on TestTask {
    id
    level
    explanation
    correctSolutions
    allSolutions
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
`;

const EDIT_TESTTASK_MUTATION = gql`
  mutation EditTestTasks($testId: ID!, $testTaskInputs: [TestTaskInput!]!) {
    editTestTasks(testId: $testId, testTaskInputs: $testTaskInputs) {
      id
      level
      explanation
    }
  }
`;

export default function EditTestElementCont({
  testId,
  testTask,
  selectedTestTaskId,
}) {
  const [levelState, setLevel] = useState({
    changed: false,
    level: testTask.level,
  });
  const [explanationState, setExplanation] = useState({
    changed: false,
    explanation: testTask.explanation ?? '',
  });

  const [deleteTask] = useMutation(DELETE_TASK_MUTATION, {
    onCompleted: () => toast.notify('Task deleted successfully'),
    onError: () => toast.notify('Error :('),
    update: (cache) => {
      cache.modify({
        id: `Test:${testId}`,
        fields: {
          testTasks(existingTestTaskRefs, { readField }) {
            return existingTestTaskRefs.filter(
              (testTaskRef) => testTask.id !== readField('id', testTaskRef)
            );
          },
        },
      });
    },
  });

  const [editTestTask] = useMutation(EDIT_TESTTASK_MUTATION, {
    onCompleted: () => toast.notify('Task changed successfully'),
    onError: () => toast.notify('Error :('),
  });

  if (!testTask) {
    return <div />;
  }

  return (
    <EditTestElementComp
      testTask={testTask}
      isSelected={testTask.id === selectedTestTaskId}
      levelState={levelState}
      explanationState={explanationState}
      onLevelChange={(value) => setLevel({ changed: true, level: value })}
      onExplanationChange={(value) =>
        setExplanation({ changed: true, explanation: value.slice(0, 500) })
      }
      levels={taskLevels}
      onEdit={() =>
        editTestTask({
          variables: {
            testId,
            testTaskInputs: [
              {
                id: testTask.id,
                level: levelState.level,
                explanation: explanationState.explanation,
              },
            ],
          },
        }).then(() => {
          setExplanation({
            changed: false,
            explanation: testTask.explanation ?? '',
          });
          setLevel({
            changed: false,
            level: testTask.level,
          });
        })
      }
      isEditDisabled={!levelState.changed && !explanationState.changed}
      onDelete={() => deleteTask({ variables: { testTaskId: testTask.id } })}
    />
  );
}

EditTestElementCont.fragments = {
  TESTTASK_DETAILS_FRAGMENT,
};

EditTestElementCont.propTypes = {
  testId: PropTypes.oneOfType([number, string]).isRequired,
  testTask: PropTypes.object.isRequired,
  selectedTestTaskId: PropTypes.oneOfType([number, string]),
};
