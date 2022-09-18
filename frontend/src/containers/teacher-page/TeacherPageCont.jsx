import React from 'react';
import gql from 'graphql-tag';
import { useQuery } from '@apollo/client';
import { useAuthentication } from '../../AuthService';
import LoadingComp from '../../components/common/LoadingComp';
import TeacherPageComp from '../../components/teacher-page/TeacherPageComp';
import GroupListElementCont from '../common/GroupListElementCont';

const TEACHER_GROUPS_QUERY = gql`
  query User($userId: ID!) {
    user(userId: $userId) {
      id
      name
      code
      teacherGroups {
        ...GroupDetails
      }
    }
  }
  ${GroupListElementCont.fragments.GROUP_DETAILS_FRAGMENT}
`;

export default function TeacherPageCont() {
  const { userId } = useAuthentication();

  const { data } = useQuery(TEACHER_GROUPS_QUERY, {
    variables: { userId },
  });

  if (!data?.user) {
    return <LoadingComp />;
  }

  return <TeacherPageComp user={data.user} />;
}
