import React, { useEffect, useState } from 'react';
import gql from 'graphql-tag';
import { useLazyQuery } from '@apollo/client';
import { useHistory, useParams } from 'react-router-dom';
import LoadingComp from '../../components/common/LoadingComp';
import NewTaskPageComp from '../../components/new-task-page/NewTaskPageComp';
import NewTaskSearchElementCont from './NewTaskSearchElementCont';

const SEARCH_TASKS_QUERY = gql`
  query SearchTasks($testId: ID!, $searchText: String, $page: Int!) {
    searchTasks(testId: $testId, searchText: $searchText, page: $page) {
      totalPages
      totalElements
      tasks {
        id
        ...TaskDetails
      }
    }
  }
  ${NewTaskSearchElementCont.fragments.TASK_DETAILS_FRAGMENT}
`;

export default function NewTaskPageCont() {
  const history = useHistory();
  const { testid: testId } = useParams();
  const [selectedLevel, selectLevel] = useState(1);
  const [currentPage, setCurrentPage] = useState(0);
  const [searchText, setSearchText] = useState('');

  const [call, { data, called, fetchMore }] = useLazyQuery(SEARCH_TASKS_QUERY, {
    fetchPolicy: 'no-cache',
  });

  useEffect(() => {
    if (!called) {
      call({
        variables: {
          testId,
          searchText,
          page: currentPage,
        },
      });
    }
    // eslint-disable-next-line
  }, [testId, called]);

  if (!data?.searchTasks) {
    return <LoadingComp />;
  }

  return (
    <NewTaskPageComp
      testId={testId}
      searchData={data.searchTasks}
      selectedLevel={selectedLevel}
      selectLevel={selectLevel}
      searchText={searchText}
      onSearchTextChange={setSearchText}
      onNavigateBack={() => history.goBack()}
      onSearch={() => {
        setCurrentPage(0);
        call({
          variables: {
            testId,
            searchText,
            page: currentPage,
          },
        });
      }}
      isMore={currentPage + 1 < data.searchTasks?.totalPages}
      onLoadMore={() =>
        fetchMore({
          onCompleted: () => setCurrentPage(currentPage + 1),
          variables: {
            searchText,
            page: currentPage + 1,
          },
          updateQuery: (prev, { moreSearchTasks }) => {
            if (!moreSearchTasks?.searchTasks?.tasks) {
              return prev;
            }

            return {
              ...prev,
              tasks: [...prev.tasks, ...moreSearchTasks.searchTasks.tasks],
            };
          },
        })
      }
    />
  );
}
