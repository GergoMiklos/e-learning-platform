import React from 'react'
import gql from "graphql-tag";
import {useQuery} from "@apollo/client";
import {useHistory, useParams, useRouteMatch} from "react-router-dom";
import LoadingComp from "../../components/common/LoadingComp";
import EditTestDetailsCont from "./EditTestDetailsCont";
import EditTestElementCont from "./EditTestElementCont";
import EditTestPageComp from "../../components/edit-test-page/EditTestPageComp";
import {taskLevels} from "../../constants";

const TEST_QUERY = gql`
    query getTest($testId: ID!) {
        test(testId: $testId) {
            id
            ...TestDetails
            testTasks {
                id
                ...TestTaskDetails
            }
        }
    }
    ${EditTestElementCont.fragments.TESTTASK_DETAILS_FRAGMENT}
${EditTestDetailsCont.fragments.TEST_DETAILS_FRAGMENT}`;


export default function EditTestPageCont() {
    let history = useHistory();
    let match = useRouteMatch();
    const {testid: testId} = useParams();

    const {data} = useQuery(TEST_QUERY, {
        variables: {testId: testId},
        fetchPolicy: 'cache-first',
    });

    if (!data?.test) {
        return (<LoadingComp/>);
    }

    return (
        <EditTestPageComp
            test={data.test}
            testTasks={!data.test.testTasks ? [] :
                calculateTestTasksGroupedByLevel(data.test.testTasks, taskLevels)}
            onNavigateBack={() => history.goBack()}
            newTaskPath={`${match.url}/tasks`}


        />
    );
}

const calculateTestTasksGroupedByLevel = (testTasks, levels) => {
    const result = new Map();
    levels.forEach((level) => {
            let levelGroup = testTasks.filter(testTask => testTask.level === level);
            if (levelGroup.length) {
                result.set(level, levelGroup);
            }
        }
    )
    return result;
}