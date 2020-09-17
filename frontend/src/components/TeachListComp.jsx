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
            name
            code
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
        this.setState({showNewGroupDialog: true});
    }

    onNewGroupDialogHide = () => {
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
                this.showNotification({text: 'Something went wrong', type: 'danger'})
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

                <div className="row bg-warning text-light rounded shadow my-3 p-3">
                    <h1 className="col-auto">{this.state.user.name}</h1>
                    <h1 className="col-auto rounded-pill bg-primary px-3">{this.state.user.code}</h1>
                </div>

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
                    <ul className="col-12 list-group">
                        {this.state.user.teacherGroups.map(group =>
                            <li
                                className="list-group-item list-group-item-action"
                                key={group.id}
                                onClick={() => this.groupClicked(group.id)}>
                                <strong>{group.name}</strong>
                            </li>
                        )}
                    </ul>
                </div>}

            </div>
        );
    }

}

export default TeachListComp;