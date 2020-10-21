import gql from 'graphql-tag';
import { useMutation } from '@apollo/client';
import toast from 'toasted-notes';
import React from 'react';
import PropTypes, { number, string } from 'prop-types';
import NewTaskSearchElementComp from '../../components/new-task-page/NewTaskSearchElementComp';
import { taskLevels } from '../../constants';

const TASK_DETAILS_FRAGMENT = gql`
  fragment TaskDetails on Task {
    id
    usage
    question
    solutionNumber
    answers {
      id
      number
      answer
    }
  }
`;

const ADD_TASK_MUTATION = gql`
  mutation AddTaskToTest($testId: ID!, $taskId: ID!, $level: Int) {
    addTaskToTest(testId: $testId, taskId: $taskId, level: $level) {
      id
      level
      task {
        id
        ...TaskDetails
      }
    }
  }
  ${TASK_DETAILS_FRAGMENT}
`;

export default function NewTaskSearchElementCont({
  testId,
  task,
  selectedTaskId,
  selectedLevel,
  onSelectLevel,
}) {
  const [addTask] = useMutation(ADD_TASK_MUTATION, {
    onCompleted: () => toast.notify('Task added successfully'),
    onError: () => toast.notify('Error :('),
    update: (cache) => {
      cache.modify({
        id: `Test:${testId}`,
        fields: {
          testTasks(existingTestTaskRefs, { INVALIDATE }) {
            return INVALIDATE;
          },
        },
      });
    },
  });

  if (!task) {
    return <div />;
  }

  return (
    <NewTaskSearchElementComp
      task={task}
      isSelected={selectedTaskId === task.id}
      selectedLevel={selectedLevel}
      levels={taskLevels}
      onSelectLevel={onSelectLevel}
      onAddTask={() =>
        addTask({
          variables: {
            testId,
            taskId: task.id,
            level: selectedLevel,
          },
        })
      }
    />
  );
}

NewTaskSearchElementCont.fragments = {
  TASK_DETAILS_FRAGMENT,
};

NewTaskSearchElementCont.propTypes = {
  testId: PropTypes.oneOfType([number, string]).isRequired,
  task: PropTypes.object.isRequired,
  selectedTaskId: PropTypes.oneOfType([number, string]),
  selectedLevel: PropTypes.number.isRequired,
  onSelectLevel: PropTypes.func.isRequired,
};
