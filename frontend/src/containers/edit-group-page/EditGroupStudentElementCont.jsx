import gql from 'graphql-tag';
import { useMutation } from '@apollo/client';
import toast from 'toasted-notes';
import React from 'react';
import PropTypes, { number, string } from 'prop-types';
import EditGroupUserElementComp from '../../components/edit-group-page/EditGroupUserElementComp';

const USER_DETAILS_FRAGMENT = gql`
  fragment StudentDetails on User {
    id
    name
    code
  }
`;

const DELETE_STUDENT_MUTATION = gql`
  mutation DeleteStudentFromGroup($userId: ID!, $groupId: ID!) {
    deleteStudentFromGroup(userId: $userId, groupId: $groupId)
  }
`;

export default function EditGroupStudentElementCont({
  user,
  groupId,
  selectedUserId,
}) {
  const [deleteStudent] = useMutation(DELETE_STUDENT_MUTATION, {
    onCompleted: () => toast.notify('Student deleted successfully'),
    onError: () => toast.notify('Error'),
    update: (cache) =>
      cache.modify({
        id: `Group:${groupId}`,
        fields: {
          students(existingUserRefs, { readField }) {
            return existingUserRefs.filter(
              (userRef) => user.id !== readField('id', userRef)
            );
          },
        },
      }),
  });

  if (!user) {
    return <div />;
  }

  return (
    <EditGroupUserElementComp
      user={user}
      isSelected={selectedUserId === user.id}
      onDelete={() =>
        deleteStudent({ variables: { userId: user.id, groupId } })
      }
    />
  );
}

EditGroupStudentElementCont.fragments = {
  USER_DETAILS_FRAGMENT,
};

EditGroupStudentElementCont.propTypes = {
  user: PropTypes.object.isRequired,
  groupId: PropTypes.oneOfType([number, string]).isRequired,
  selectedUserId: PropTypes.oneOfType([number, string]),
};
