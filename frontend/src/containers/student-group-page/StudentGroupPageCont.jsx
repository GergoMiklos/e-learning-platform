import React from 'react';
import gql from 'graphql-tag';
import toast from 'toasted-notes';
import { useMutation, useQuery } from '@apollo/client';
import { useHistory, useParams } from 'react-router-dom';
import { useAuthentication } from '../../AuthService';
import LoadingComp from '../../components/common/LoadingComp';
import StudentGroupPageComp from '../../components/student-group-page/StudentGroupPageComp';
import StudentGroupElementCont from './StudentGroupElementCont';

const STUDENT_GROUP_QUERY = gql`
  query getGroup($groupId: ID!) {
    group(groupId: $groupId) {
      id
      name
      code
      description
      news
      newsChangedDate
      tests(active: true) {
        id
        ...TestDetials
      }
    }
  }
  ${StudentGroupElementCont.fragments.TEST_DETAILS_FRAGMENT}
`;

const LEAVE_GROUP_MUTATION = gql`
  mutation DeleteUserFromGroup($userId: ID!, $groupId: ID!) {
    deleteStudentFromGroup(userId: $userId, groupId: $groupId)
  }
`;

export default function StudentGroupPageCont() {
  const history = useHistory();
  const { groupid: groupId } = useParams();
  const { userId } = useAuthentication();

  const { data } = useQuery(STUDENT_GROUP_QUERY, {
    variables: { groupId },
  });

  const [leaveGroup] = useMutation(LEAVE_GROUP_MUTATION, {
    onCompleted: () => toast.notify('Group left successfully'),
    onError: () => toast.notify('Error'),
  });

  if (!data?.group) {
    return <LoadingComp />;
  }

  return (
    <StudentGroupPageComp
      group={data.group}
      onLeaveGroup={() =>
        leaveGroup({
          variables: {
            userId,
            groupId,
          },
        }).then(() => history.push('/student'))
      }
      onNavigateBack={() => history.goBack()}
    />
  );
}
