import React, {Component} from 'react';
import gql from "graphql-tag";
import client from "../ApolloClient";
import toaster from "toasted-notes";
import AuthenticationService from "../AuthenticationService";

// const state = {
//     NOT_STARTED: "Not started",
//     IN_PROGRESS: "In Progress",
//     PROBLEM: "Problem",
//     WARNING: "Warning",
//     FINISHED: "Finished"
// }

const PARENT_FOLLOWED_STATUSES = gql`
    query getUser($userId: ID!) {
        user(userId: $userId) {
            id
            name
            code
            followedStudents {
                id
                name
                code
                userTestStatuses {
                    status
                    statusChangedTime
                    correctAnswers
                    allAnswers
                    test {
                        name
                        description
                    }

                }
            }
        }
    }`


const ADD_FOLLOWED_STUDENT = gql`
    mutation AddStudentFromCodeToParent($parentId: ID!, $studentCode: String!) {
        addStudentFromCodeToParent(parentId: $parentId, studentCode: $studentCode) {
            name
        }
    }`;

const DELETE_FOLLOWED_STUDENT = gql`
    mutation DeleteStudentFromParent($parentId: ID!, $studentId: ID!) {
        deleteStudentFromParent(parentId: $parentId, studentId: $studentId)
    }`;


class ParentPageComp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            test: null,
            addStudentCode: '',
        };
    }

    componentDidMount() {
        this.loadData();
    }

    handleInputChange = (event) => {
        this.setState({addStudentCode: event.target.value.toUpperCase().slice(0, 8),});
    }

    addFollowedStudent = () => {
        if (!this.state.addStudentCode) {
            return;
        }
        let userId = AuthenticationService.getUserId();
        client.mutate({
            mutation: ADD_FOLLOWED_STUDENT,
            variables: {parentId: userId, studentCode: this.state.addStudentCode}
        })
            .then(result => {
                if (result.data.addStudentFromCodeToParent) {
                    this.setState({addStudentCode: ''});
                    this.showNotification({
                        text: `User followed: ${result.data.addStudentFromCodeToParent.name}`,
                        type: 'success'
                    })
                    this.loadData();
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: `No group with code: ${this.state.addStudentCode}`, type: 'danger'})
            })
    }

    deleteFollowed = (studentId) => {
        let userId = AuthenticationService.getUserId();
        client.mutate({
            mutation: DELETE_FOLLOWED_STUDENT,
            variables: {parentId: userId, studentId: studentId}
        })
            .then(() => {
                this.showNotification({text: 'Student deleted successfully', type: 'success'});
                this.loadData();
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'});
            })
    }

    loadData = () => {
        let userId = AuthenticationService.getUserId();
        client
            .query({
                query: PARENT_FOLLOWED_STATUSES,
                variables: {userId: userId},
                fetchPolicy: 'no-cache',
            })
            .then(result => {
                if (result.data) {
                    this.setState({user: result.data.user});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'})

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
        if (!this.state.user) {
            return (<div/>);
        }
        return (
            <div className="container">

                <div className="row input-group mx-0 my-3">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Follow student:</span>
                    </div>
                    <input type="text" className="form-control" placeholder="USER CODE"
                           value={this.state.addStudentCode} onChange={this.handleInputChange}/>
                    <div className="input-group-append">
                        <button className="btn btn-outline-primary" onClick={() => this.addFollowedStudent()}>
                            Add
                        </button>
                    </div>
                </div>

                <div className="btn-group btn-block">
                    <div className="btn btn-info disabled">Not Started</div>
                    <div className="btn btn-success disabled">In Progress</div>
                    <div className="btn btn-warning disabled">Inactive</div>
                    <div className="btn btn-danger disabled">Problem</div>
                </div>

                {this.state.user.followedStudents.map(student =>
                    <div key={student.id} className="row my-3">
                        <div className="col-12 rounded shadow my-3 p-3 d-flex justify-content-between">
                            <h1>{student.name}</h1>
                            <button className="btn btn-danger" onClick={() => this.deleteFollowed(student.id)}>
                                Delete
                            </button>
                        </div>

                        {(student.userTestStatuses.length !== 0) &&
                        <table className="col-12 table table-striped rounded shadow">
                            <thead>
                            <tr>
                                <th>Test</th>
                                <th className="text-center">Correct / All</th>
                                <th className="text-center">Status</th>
                            </tr>
                            </thead>

                            <tbody>
                            {student.userTestStatuses.map(uts =>
                                <tr key={uts.id}>
                                    <td className="font-weight-bold">
                                        {uts.test.name}
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
                        }
                    </div>
                )}
            </div>
        );
    }

}

export default ParentPageComp;