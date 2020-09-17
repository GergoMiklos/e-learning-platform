import React, {Component} from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import EditGroupDetailsComp from "./EditGroupDetailsComp";
import toaster from "toasted-notes";

const GROUP = gql`
    query getGroup($groupId: ID!) {
        group(groupId: $groupId) {
            id
            name
            description
            teachers {
                id
                name
                code
            }
            students {
                id
                name
                code
            }
        }
    }`;

const DELETE_STUDENT = gql`
    mutation DeleteStudentFromGroup($userId: ID!, $groupId: ID!) {
        deleteStudentFromGroup(userId: $userId, groupId: $groupId)
    }`;

const DELETE_TEACHER = gql`
    mutation DeleteTeacherFromGroup($userId: ID!, $groupId: ID!) {
        deleteTeacherFromGroup(userId: $userId, groupId: $groupId)
    }`;

const ADD_TEACHER = gql`
    mutation AddTeacherFromCodeToGroup($groupId: ID!, $userCode: String!) {
        addTeacherFromCodeToGroup(groupId: $groupId, userCode: $userCode)  {
            name
        }
    }`;

const ADD_STUDENT = gql`
    mutation AddStudentFromCodeToGroup($groupId: ID!, $userCode: String!) {
        addStudentFromCodeToGroup(groupId: $groupId, userCode: $userCode)  {
            name
        }
    }`;

class EditGroupComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            group: null,
            addCode: '',
            selectedUserId: null,
        };
    }

    componentDidMount() {
        this.loadData();
    }

    navigateBack = () => {
        this.props.history.push(`/teach/group/${this.props.match.params.groupid}`)
    }

    selectUser = (id) => {
        this.setState({selectedUserId: id})
    }

    deleteStudent = (userId) => {
        client.mutate({
            mutation: DELETE_STUDENT,
            variables: {userId: userId, groupId: this.state.group.id}
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

    deleteTeacher = (userId) => {
        client.mutate({
            mutation: DELETE_TEACHER,
            variables: {userId: userId, groupId: this.state.group.id},
        })
            .then(() => {
                this.showNotification({text: 'Teacher deleted successfully', type: 'success'});
                this.loadData();
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'});
            })
    }

    handleInputChange = (event) => {
        this.setState(
            {addCode: event.target.value.toUpperCase().slice(0, 8)}
        );
    }

    addStudent = () => {
        if (!this.state.addCode)
            return;
        client.mutate({
            mutation: ADD_STUDENT,
            variables: {groupId: this.state.group.id, userCode: this.state.addCode},
        })
            .then(result => {
                if (result.data) {
                    this.setState({addCode: ''});
                    this.showNotification({
                        text: `Student added: ${result.data.addStudentFromCodeToGroup.name}`,
                        type: 'success'
                    });
                    this.loadData();
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: `No user with code: ${this.state.addCode}`, type: 'danger'});
            })
    }

    addTeacher = () => {
        if (!this.state.addCode)
            return;
        client.mutate({
            mutation: ADD_TEACHER,
            variables: {groupId: this.state.group.id, userCode: this.state.addCode},
        })
            .then(result => {
                if (result.data) {
                    this.setState({addCode: ''});
                    this.showNotification({
                        text: `Teacher added: ${result.data.addTeacherFromCodeToGroup.name}`,
                        type: 'success',
                    });
                    this.loadData();
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: `No user with code: ${this.state.addCode}`, type: 'danger'});
            })
    }

    loadData = () => {
        client
            .query({
                query: GROUP,
                variables: {groupId: this.props.match.params.groupid},
                fetchPolicy: 'network-only',
            })
            .then(result => {
                if (result.data) {
                    this.setState({group: result.data.group});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'});
            });
    }

    showNotification = ({text, type}) => {
        toaster.notify(() => (
            <div className={`alert alert-${type}`}>
                {text}
            </div>
        ));
    }

    render() {
        if (!this.state.group) {
            return (<div/>)
        }
        return (
            <div className="container">

                <button className="row btn btn-secondary mt-1" onClick={() => this.navigateBack()}>
                    Back
                </button>

                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-auto">Edit Group</h1>
                </div>

                <EditGroupDetailsComp className="row"
                                      groupId={this.state.group.id}
                                      name={this.state.group.name}
                                      description={this.state.group.description}
                />

                <div className="row input-group mt-5 p-3">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Add: </span>
                    </div>
                    <input
                        type="text"
                        className="form-control"
                        placeholder="USER CODE"
                        value={this.state.addCode}
                        onChange={this.handleInputChange}
                    />
                    <div className="input-group-append">
                        <button className="btn btn-primary" onClick={() => this.addStudent()}>
                            User
                        </button>
                        <button className="btn btn-outline-secondary" onClick={() => this.addTeacher()}>
                            Admin
                        </button>
                    </div>
                </div>

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">Teachers</h1>
                </div>

                {this.state.group.teachers &&
                <div className="row my-3">
                    <ul className="col-12 list-group">
                        {this.state.group.teachers.map(user =>
                            <li
                                className="list-group-item list-group-item-action"
                                key={user.id}
                                onClick={() => this.selectUser(user.id)}
                            >
                                <UserListElementComp
                                    user={user}
                                    selectedUserId={this.state.selectedUserId}
                                    deleteUser={(id) => this.deleteTeacher(id)}
                                />
                            </li>
                        )}
                    </ul>
                </div>}

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">Students</h1>
                </div>

                {this.state.group.students &&
                <div className="row my-3">
                    <ul className="col-12 list-group">
                        {this.state.group.students.map(user =>
                            <li
                                className="list-group-item list-group-item-action"
                                key={user.id}
                                onClick={() => this.selectUser(user.id)}
                            >
                                <UserListElementComp
                                    user={user}
                                    selectedUserId={this.state.selectedUserId}
                                    deleteUser={(id) => this.deleteStudent(id)}
                                />
                            </li>
                        )}
                    </ul>
                </div>}
            </div>
        );
    }

}

function UserListElementComp(props) {
    return (
        <div className="d-flex justify-content-between">
            <strong>{props.user.name}</strong>
            <div>{props.user.code}</div>
            {props.selectedUserId === props.user.id &&
            <button className="btn btn-danger btn-sm"
                    onClick={() => props.deleteUser(props.user.id)}>
                Delete
            </button>}
        </div>
    );
}

export default EditGroupComp;