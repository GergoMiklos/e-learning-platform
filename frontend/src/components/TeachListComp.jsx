import React, {Component} from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import AuthenticationService from "../AuthenticationService";
import toaster from 'toasted-notes';
import {Modal} from 'react-bootstrap';
import NewGroupDialogComp from "./NewGroupDialogComp";


const TEACHER_GROUPS = gql`
    query User($userId: ID!) {
        user(userId: $userId) {
            id
            teacherGroups {
                id
                name
            }
        }
    }`;

class TeachListComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            user: null,
            showNewGroupDialog: false,
        };
    }

    newGroup = () => {
        //this.props.history.push(`/teach/group/new`);
        this.setState({showNewGroupDialog: true});
    }

    onNewGroupDialogHide = (groupCreated) => {
        this.loadData();
        this.setState({showNewGroupDialog: false});
    }

    groupClicked = (id) => {
        this.props.history.push(`/teach/group/${id}`);
    }

    componentDidMount() {
        this.loadData();
    }

    loadData = () => {
        let userId = AuthenticationService.getUserId();
        client
            .query({
                query: TEACHER_GROUPS,
                variables: {userId: userId},
                fetchPolicy: 'network-only',
            })
            .then(result => {
                console.log(result);
                if (result.data) {
                    this.setState({user: result.data.user});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'error'})
            });
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
            return (<div/>)
        }
        return (
            <div className="container">
                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Teacher Groups</h1>
                    <button className="col-2 btn btn-primary" onClick={() => this.newGroup()}>
                        New
                    </button>
                </div>

                <NewGroupDialogComp
                    show={this.state.showNewGroupDialog}
                    onHide={this.onNewGroupDialogHide}
                />

                {this.state.user.teacherGroups &&
                <div className="row my-3">
                    <table className="col-12 table table-striped table-hover rounded shadow">
                        <tbody>
                        {
                            this.state.user.teacherGroups.map(
                                group =>
                                    <tr key={group.id} onClick={() => this.groupClicked(group.id)}>
                                        <td>
                                            <strong>{group.name}</strong>
                                            <div>{group.description}</div>
                                        </td>
                                    </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div>}

            </div>
        );
    }

}

export default TeachListComp;