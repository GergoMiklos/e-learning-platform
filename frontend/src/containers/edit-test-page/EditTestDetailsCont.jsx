import React from 'react';
import gql from 'graphql-tag';
import toast from 'toasted-notes';
import { useMutation } from '@apollo/client';
import PropTypes from 'prop-types';
import FormCont from '../common/FormCont';

const TEST_DETAILS_FRAGMENT = gql`
  fragment TestDetails on Test {
    id
    name
    description
  }
`;

const EDIT_TEST_MUTATION = gql`
  mutation EditTest($testId: ID!, $input: NameDescInput!) {
    editTest(testId: $testId, input: $input) {
      ...TestDetails
    }
  }
  ${TEST_DETAILS_FRAGMENT}
`;

export default function EditTestDetailsCont({ test }) {
  const [editTest] = useMutation(EDIT_TEST_MUTATION, {
    onCompleted: () => toast.notify('Test details edited successfully'),
    onError: () => toast.notify('Error'),
  });

  if (!test) {
    return <div />;
  }

  return (
    <FormCont
      initialName={test.name}
      initialDescription={test.description}
      onSubmit={(values) => {
        editTest({
          variables: {
            input: {
              description: values.description,
              name: values.name,
            },
            testId: test.id,
          },
        });
      }}
    />
  );
}

EditTestDetailsCont.fragments = {
  TEST_DETAILS_FRAGMENT,
};

EditTestDetailsCont.propTypes = {
  test: PropTypes.object.isRequired,
};
