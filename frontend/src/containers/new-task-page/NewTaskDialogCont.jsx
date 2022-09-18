import React from 'react';
import gql from 'graphql-tag';
import toast from 'toasted-notes';
import { useMutation } from '@apollo/client';
import { useHistory } from 'react-router-dom';
import NewTaskDialogComp from '../../components/new-task-page/NewTaskDialogComp';

const CREATE_TASK_MUTATION = gql`
  mutation CreateTask($taskInput: TaskInput!) {
    createTask(taskInput: $taskInput) {
      question
    }
  }
`;

export default function NewTaskDialogCont() {
  const history = useHistory();

  const [createTask] = useMutation(CREATE_TASK_MUTATION, {
    onCompleted: (data) =>
      toast.notify(`Task created: ${data.createTask?.question}`),
    onError: () => toast.notify('Error :('),
  });

  return (
    <NewTaskDialogComp
      onNavigateBack={() => history.goBack()}
      onValidate={validate}
      onSubmit={(values) =>
        createTask({
          variables: {
            taskInput: {
              question: values.question,
              correctAnswer: values.correct,
              incorrectAnswers: [values.bad1, values.bad2, values.bad3],
            },
          },
        }).then(() => history.goBack())
      }
    />
  );
}

const validate = (values) => {
  const errors = {};
  [
    values.question,
    values.correct,
    values.bad1,
    values.bad2,
    values.bad3,
  ].forEach((field) => {
    if (
      !field ||
      field.toString().length > 250 ||
      field.toString().length < 1
    ) {
      errors.question =
        'All fields should be between min 1 and max 250 characters!';
    }
  });
  return errors;
};
