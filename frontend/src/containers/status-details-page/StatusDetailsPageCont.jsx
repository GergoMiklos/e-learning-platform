import React from 'react';
import gql from 'graphql-tag';
import { useQuery } from '@apollo/client';
import { useHistory, useParams } from 'react-router-dom';
import LoadingComp from '../../components/common/LoadingComp';
import StatusDetailsModalComp from '../../components/status-details-page/StatusDetailsModalComp';

const STUDENTSTATUS_QUERY = gql`
  query getStudentStatus($studentStatusId: ID!) {
    studentStatus(studentStatusId: $studentStatusId) {
      id
      status
      statusChangedTime
      correctSolutions
      allSolutions
      solvedTasks
      user {
        id
        name
        code
      }
      test {
        id
        name
        allTasks
        group {
          id
          name
        }
      }
      currentTestTask {
        id
      }
      studentTaskStatuses {
        correctSolutions
        allSolutions
        wrongSolutionsInRow
        testTask {
          id
          allSolutions
          correctSolutions
          task {
            id
            question
          }
        }
      }
    }
  }
`;

export default function TeacherLiveTestPageCont() {
  const history = useHistory();
  const { studentstatusid: studentStatusId } = useParams();

  const { data } = useQuery(STUDENTSTATUS_QUERY, {
    variables: { studentStatusId },
    pollInterval: 60 * 1000,
  });

  if (!data?.studentStatus) {
    return <LoadingComp />;
  }

  return (
    <StatusDetailsModalComp
      studentStatus={data.studentStatus}
      onHide={() => history.goBack()}
    />
  );
}
