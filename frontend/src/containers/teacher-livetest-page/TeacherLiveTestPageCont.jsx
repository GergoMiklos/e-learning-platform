import React from 'react';
import gql from 'graphql-tag';
import { useQuery } from '@apollo/client';
import { useHistory, useParams } from 'react-router-dom';
import LoadingComp from '../../components/common/LoadingComp';
import TeacherLiveTestPageComp from '../../components/teacher-livetest-page/TeacherLiveTestPageComp';

const STUDENTSTATUS_DETAILS_FRAGMENT = gql`
  fragment StudentStatusDetials on StudentStatus {
    id
    status
    statusChangedTime
    lastSolutionTime
    correctSolutions
    allSolutions
    solvedTasks
    user {
      name
      code
    }
  }
`;

const STUDENTSTATUSES_QUERY = gql`
  query getTest($testId: ID!) {
    test(testId: $testId) {
      id
      name
      description
      allTasks
      studentStatuses {
        id
        ...StudentStatusDetials
      }
    }
  }
  ${STUDENTSTATUS_DETAILS_FRAGMENT}
`;

const STATUS_CHANGE_SUBSCRIPTION = gql`
  subscription onStatusChange($testId: ID!) {
    testStatusChangesByTest(testId: $testId) {
      id
      ...StudentStatusDetials
    }
  }
  ${STUDENTSTATUS_DETAILS_FRAGMENT}
`;

export default function TeacherLiveTestPageCont() {
  const history = useHistory();
  const { testid: testId } = useParams();

  const { data, subscribeToMore } = useQuery(STUDENTSTATUSES_QUERY, {
    variables: { testId },
    pollInterval: 60 * 1000,
  });

  if (!data?.test) {
    return <LoadingComp />;
  }

  return (
    <TeacherLiveTestPageComp
      test={data.test}
      onNavigateBack={() => history.goBack()}
      onSubscribe={() =>
        subscribeToMore({
          document: STATUS_CHANGE_SUBSCRIPTION,
          variables: { testId },
          // eslint-disable-next-line consistent-return
          updateQuery: (prev, { subscriptionData }) => {
            if (!subscriptionData.data?.testStatusChangesByTest || !prev) {
              return prev;
            }

            const newUserTestStatus = subscriptionData.data.testStatusChangesByTest;
            if (
              prev.test.studentStatuses.some(
                (uts) => uts.id !== newUserTestStatus.id
              )
            ) {
              return {
                ...prev,
                test: {
                  studentStatuses: [
                    newUserTestStatus,
                    ...prev.test.studentStatuses,
                  ],
                },
              };
            }
          },
        })
      }
    />
  );
}

TeacherLiveTestPageCont.fragments = {
  STUDENTSTATUS_DETAILS_FRAGMENT,
};
