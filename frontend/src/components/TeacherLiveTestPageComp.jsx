import React, {Component} from 'react';
import gql from "graphql-tag";
import {useQuery} from "@apollo/client";
import {Link} from "react-router-dom";
import StatusElementComp from "./StatusElementComp";

const USERTESTSTATUS_DETAILS_FRAGMENT = gql`
    fragment UserTestStatusDetials on UserTestStatus {
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

const USERTESTSTATUSES_QUERY = gql`
    query getTest($testId: ID!) {
        test(testId: $testId) {
            id
            name
            description
            allTasks
            userTestStatuses {
                ...UserTestStatusDetials
            }
        }
    }
${USERTESTSTATUS_DETAILS_FRAGMENT}`;

const STATUS_CHANGE_SUBSCRIPTION = gql`
    subscription onStatusChange($testId: ID!) {
        testStatusChanges(testId: $testId) {
            ...UserTestStatusDetials
        }
    }
${USERTESTSTATUS_DETAILS_FRAGMENT}`;

class TeacherLiveTestPageComp extends Component {

    componentDidMount() {
        //Todo nem kell class és hoc, ha egy useEffect(..,[])-be rakom!
        this.props.subscribe()
    }

    render() {
        return (
            <div className="container">
                <Link to={`/teach/group/${this.props.match.params.groupid}`} className="row btn btn-secondary mt-1">
                    Back
                </Link>

                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-10">{this.props.data.test.name}</h1>
                </div>

                <div className="row btn-group btn-block m-1">
                    <div className="col-3 btn btn-info disabled">Not Started</div>
                    <div className="col-3 btn btn-success disabled">In Progress</div>
                    <div className="col-3 btn btn-warning disabled">Inactive</div>
                    <div className="col-3 btn btn-danger disabled">Problem</div>
                </div>

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
                        {this.props.data.test.userTestStatuses && this.props.data.test.userTestStatuses.map(uts =>
                            <tr key={uts.id}>
                                <td className="font-weight-bold">
                                    {uts.user.name}
                                </td>
                                <td className="text-center">
                                    {uts.user.code}
                                </td>
                                <td className="text-center">
                                    {uts.solvedTasks}/{this.props.data.test.allTasks}
                                </td>
                                <td className="text-center">
                                    {uts.correctSolutions}/{uts.allSolutions}
                                </td>
                                <td className="text-center">
                                    <StatusElementComp userTestStatus={uts} />
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default function TeacherLiveTestPageCont(props) {
    const {loading, error, data, subscribeToMore} = useQuery(
        USERTESTSTATUSES_QUERY, {
            variables: {testId: props.match.params.testid},
            pollInterval: 60 * 1000,
        });

    if (!data) {
        return (<div/>);
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
    USERTESTSTATUS_DETAILS_FRAGMENT: USERTESTSTATUS_DETAILS_FRAGMENT,
};

