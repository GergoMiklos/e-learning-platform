import React from 'react';
import gql from 'graphql-tag';
import { useQuery } from '@apollo/client';
import { useHistory, useParams, useRouteMatch } from 'react-router-dom';
import LoadingComp from '../../components/common/LoadingComp';
import TeacherGroupElementCont from './TeacherGroupElementCont';
import TeacherGroupPageComp from '../../components/teacher-group-page/TeacherGroupPageComp';

const TEACHER_GROUP_QUERY = gql`
  query getGroup($groupId: ID!) {
    group(groupId: $groupId) {
      id
      name
      code
      description
      news
      newsChangedDate
      tests {
        id
        ...TestDetails
      }
    }
  }
  ${TeacherGroupElementCont.fragments.TEST_DETAILS_FRAGMENT}
`;

export default function TeacherGroupPageCont() {
  const match = useRouteMatch();
  const history = useHistory();
  const { groupid: groupId } = useParams();

  const { data } = useQuery(TEACHER_GROUP_QUERY, {
    variables: { groupId },
  });

  if (!data?.group) {
    return <LoadingComp />;
  }

  return (
    <TeacherGroupPageComp
      group={data.group}
      editPath={`${match.url}/edit`}
      onNavigateBack={() => history.goBack()}
    />
  );
}
