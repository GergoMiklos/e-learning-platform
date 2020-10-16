import React, {useEffect} from 'react';
import gql from "graphql-tag";
import {useQuery} from "@apollo/client";
import {Link} from "react-router-dom";
import StatusElementComp from "./common/StatusElementComp";
import {useHistory} from "react-router-dom";
import LoadingComp from "./common/LoadingComp";

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
                ...StudentStatusDetials
            }
        }
    }
${STUDENTSTATUS_DETAILS_FRAGMENT}`;

const STATUS_CHANGE_SUBSCRIPTION = gql`
    subscription onStatusChange($testId: ID!) {
        testStatusChanges(testId: $testId) {
            ...StudentStatusDetials
        }
    }
${STUDENTSTATUS_DETAILS_FRAGMENT}`;

function TeacherLiveTestPageComp(props) {

    useEffect(() => {
        props.subscribe()
    }, [])

    return (
        <div className="container">
            <div
                onClick={() => props.history.goBack()}
                className="row btn btn-secondary mt-1"
            >
                Back
            </div>

            <div className="row bg-primary text-light rounded shadow my-3 p-3">
                <h1 className="col-10">{props.data.test.name}</h1>
            </div>

            <div className="row btn-group btn-block m-1">
                <div className="col-3 btn btn-info disabled">Not Started</div>
                <div className="col-3 btn btn-success disabled">In Progress</div>
                <div className="col-3 btn btn-warning disabled">Inactive</div>
                <div className="col-3 btn btn-danger disabled">Problem</div>
            </div>

            {props.data.test.studentStatuses?.length === 0 ? "No Statuses" :
                <div className="my-3 table-responsive-sm">
                    <table className="table table-striped bg-light">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th className="text-center">Code</th>
                            <th className="text-center">Tasks (Solved/All)</th>
                            <th className="text-center">Answers (Correct/All)</th>
                            <th className="text-center">Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        {props.data.test.studentStatuses.map(status =>
                            <tr key={status.id}>
                                <td className="font-weight-bold">
                                    {status.user.name}
                                </td>
                                <td className="text-center">
                                    {status.user.code}
                                </td>
                                <td className="text-center">
                                    {status.solvedTasks}/{props.data.test.allTasks}
                                </td>
                                <td className="text-center">
                                    {status.correctSolutions}/{status.allSolutions}
                                </td>
                                <td className="text-center">
                                    <StatusElementComp studentStatus={status}/>
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            }
        </div>
    );
}

export default function TeacherLiveTestPageCont(props) {
    const {loading, error, data, subscribeToMore} = useQuery(
        STUDENTSTATUSES_QUERY, {
            variables: {testId: props.match.params.testid},
            pollInterval: 60 * 1000,
        });

    if (!data?.test) {
        return (<LoadingComp/>);
    }

    return (
        <TeacherLiveTestPageComp
            {...props}
            data={data}
            subscribe={() => subscribeToMore({
                document: STATUS_CHANGE_SUBSCRIPTION,
                variables: {testId: props.match.params.testid},
                updateQuery: (prev, {subscriptionData}) => {
                    if (!subscriptionData.data || !prev) {
                        return prev;
                    }
                    const newUserTestStatus = subscriptionData.data.testStatusChanges;
                    //TODO immutable?
                    if (prev.test.userTestStatuses.some(uts => uts.id !== newUserTestStatus.id)) {
                        return Object.assign({}, prev, {
                            test: {
                                userTestStatuses: [newUserTestStatus, ...prev.test.userTestStatuses]
                            }
                        });
                    } //todo else magától megváltoztatja mert csak egy meglévő elem jött be! (lsd mutation)
                }
            })}
        />
    );
}

TeacherLiveTestPageCont.fragments = {
    STUDENTSTATUS_DETAILS_FRAGMENT: STUDENTSTATUS_DETAILS_FRAGMENT,
};

