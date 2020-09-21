import React, {Component} from 'react';
import gql from "graphql-tag";
import client from "../ApolloClient";
import toaster from "toasted-notes";

// const state = {
//     NOT_STARTED: "Not started",
//     IN_PROGRESS: "In Progress",
//     PROBLEM: "Problem",
//     WARNING: "Warning",
//     FINISHED: "Finished"
// }

const USERTESTSTATUSES = gql`
    query getTest($testId: ID!) {
        test(testId: $testId) {
            id
            name
            description
            userTestStatuses {
                status
                statusChangedTime
                correctAnswers
                allAnswers
                user {
                    name
                    code
                }

            }
        }
    }`

class TeacherLiveTestPageComp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            test: null,
        };
    }

    navigateBack = () => {
        this.props.history.push(`/teach/group/${this.props.match.params.groupid}`);
    }

    componentDidMount() {
        this.loadData();
    }

    loadData = () => {
        client
            .query({
                query: USERTESTSTATUSES,
                variables: {testId: this.props.match.params.testid},
                fetchPolicy: 'network-only',
            })
            .then(result => {
                if (result.data) {
                    this.setState({test: result.data.test});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'});
            });
    }

    isStatusInactive = (statusChangedTime) => {
        const now = new Date();
        const fiveMins = 1000 * 60 * 5;
        return (now.getTime() - new Date(statusChangedTime).getTime()) > fiveMins;
    }

    calculateStatusColor = (userTestStatus) => {
        if (userTestStatus.status === 'NOT_STARTED') {
            return 'info';
        } else if (this.isStatusInactive(userTestStatus.statusChangedTime)) {
            return 'warning';
        } else if (userTestStatus.status === 'IN_PROGRESS') {
            return 'success';
        } else if (userTestStatus.status === 'PROBLEM') {
            return 'danger';
        } else {
            return 'secondary'
        }
    }

    calculateStatusTime = (statusChangedTime) => {
        const now = new Date();
        const diff = now.getTime() - new Date(statusChangedTime).getTime();
        if (diff < 1000 * 60 * 60) {
            return `${Math.round(diff / (1000 * 60))} mins ago`
        } else if (diff < 1000 * 60 * 60 * 24) {
            return `${Math.round(diff / (1000 * 60 * 60))} hours ago`
        } else {
            return `${Math.round(diff / (1000 * 60 * 60 * 24))} days ago`
        }
    }

    showNotification = ({text, type}) => {
        toaster.notify(() => (
            <div className={`alert alert-${type}`}>
                {text}
            </div>
        ))
    }

    render() {
        if (!this.state.test) {
            return (<div/>);
        }
        return (
            <div className="container">
                <button className="row btn btn-secondary mt-1" onClick={() => this.navigateBack()}>
                    Back
                </button>

                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-10">{this.state.test.name}</h1>
                </div>

                <div className="btn-group btn-block">
                    <div className="btn btn-info disabled">Not Started</div>
                    <div className="btn btn-success disabled">In Progress</div>
                    <div className="btn btn-warning disabled">Inactive</div>
                    <div className="btn btn-danger disabled">Problem</div>
                </div>

                <div className="row my-3">
                    <table className="col-12 table table-striped rounded shadow">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th className="text-center">Code</th>
                            <th className="text-center">Correct / All</th>
                            <th className="text-center">Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        {this.state.test.userTestStatuses.map(uts =>
                            <tr key={uts.id}>
                                <td className="font-weight-bold">
                                    {uts.user.name}
                                </td>
                                <td className="text-center">
                                    {uts.user.code}
                                </td>
                                <td className="text-center">
                                    {uts.correctAnswers} / {uts.allAnswers}
                                </td>
                                <td className='text-center'>
                                    <strong
                                        className={`btn-${this.calculateStatusColor(uts)} btn rounded-pill disabled w-100`}>
                                        {uts.status === 'NOT_STARTED' ? 'NOT STARTED' : this.calculateStatusTime(uts.statusChangedTime)}
                                    </strong>
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

export default TeacherLiveTestPageComp;