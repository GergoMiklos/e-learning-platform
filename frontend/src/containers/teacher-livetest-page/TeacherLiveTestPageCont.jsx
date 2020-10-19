import React from 'react';
import gql from "graphql-tag";
import {useQuery} from "@apollo/client";
import {useHistory, useParams} from "react-router-dom"
import LoadingComp from "../../components/common/LoadingComp";
import TeacherLiveTestPageComp from "../../components/teacher-livetest-page/TeacherLiveTestPageComp";

const STUDENTSTATUS_DETAILS_FRAGMENT = gql`
    fragment StudentStatusDetials on StudentStatus {
        id
        status
        statusChangedTime
        correctSolutions
        allSolutions
        solvedTasks
        user {
            name
            code
        }
    }`;

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
${STUDENTSTATUS_DETAILS_FRAGMENT}`;

const STATUS_CHANGE_SUBSCRIPTION = gql`
    subscription onStatusChange($testId: ID!) {
        testStatusChanges(testId: $testId) {
            id
            ...StudentStatusDetials
        }
    }
${STUDENTSTATUS_DETAILS_FRAGMENT}`;


export default function TeacherLiveTestPageCont() {
    let history = useHistory();
    const {testid: testId} = useParams();

    const {data, subscribeToMore} = useQuery(
        STUDENTSTATUSES_QUERY, {
            variables: {testId: testId},
            pollInterval: 60 * 1000,
        });

    if (!data?.test) {
        return (<LoadingComp/>);
    }

    return (
        <TeacherLiveTestPageComp
            test={data.test}
            onNavigateBack={() => history.goBack()}

            onSubscribe={() => subscribeToMore({
                document: STATUS_CHANGE_SUBSCRIPTION,
                variables: {testId: testId},
                updateQuery: (prev, {subscriptionData}) => {
                    if (!subscriptionData.data || !prev) {
                        return prev;
                    }

                    const newUserTestStatus = subscriptionData.data.testStatusChanges;
                    if (prev.test.userTestStatuses.some(uts => uts.id !== newUserTestStatus.id)) {
                        return Object.assign({}, prev, {
                            test: {
                                userTestStatuses: [newUserTestStatus, ...prev.test.userTestStatuses]
                            }
                        });
                    }
                }
            })}
        />
    );
}

TeacherLiveTestPageCont.fragments = {
    STUDENTSTATUS_DETAILS_FRAGMENT: STUDENTSTATUS_DETAILS_FRAGMENT,
};

