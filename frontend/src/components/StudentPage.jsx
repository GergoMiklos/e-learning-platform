import React, {Component, useState} from 'react'
import client from "../ApolloClient";
import gql from 'graphql-tag';
import AuthenticationService from "../AuthenticationService";
import toaster from 'toasted-notes';
import NofitcationComp from './NotificationComp'
import NotificationComp from "./NotificationComp";
import {Spinner} from "react-bootstrap";
import {useQuery} from "@apollo/client";

//todo itt lehtne loadolni egy fájlból is
const studentPageGql = {
    STUDENT_GROUPS: gql`
        query getUser($userId: ID!) {
            user(userId: $userId) {
                id
                name
                code
                studentGroups {
                    id
                    name
                    news
                    newsChangedDate
                }
            }
        }`,
}

const JOIN_GROUP = gql`
    mutation AddStudentToGroupFromCode($userId: ID!, $groupCode: String!) {
        addStudentToGroupFromCode(userId: $userId, groupCode: $groupCode)  {
            name
        }
    }`;

class StudentPage extends Component {
    constructor(props) {
        super(props)
        this.state = {
            user: null,
            joinGroupCode: '',
        }
    }

    componentDidMount() {
        this.loadData();
    }

    loadData = () => {
        let userId = AuthenticationService.getUserId();
        client
            .query({
                query: STUDENT_GROUPS,
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

    groupClicked = (id) => {
        this.props.history.push(`/learn/group/${id}`);
    }

    formatDate = (date) => {
        const options = {
            weekday: 'long',
            hour: 'numeric',
            minute: 'numeric'
        }
        return new Date(date).toLocaleString(/*navigator.language*/ 'en', options); //todo
    }

    isFresh = (date) => {
        const now = new Date();
        const sixDays = 1000 * 60 * 60 * 24 * 6;
        return (now.getTime() - date.getTime()) < sixDays;
    }

    joinGroup = () => {
        if (!this.state.joinGroupCode) {
            return;
        }
        let userId = AuthenticationService.getUserId();
        client.mutate({
            mutation: JOIN_GROUP,
            variables: {userId: userId, groupCode: this.state.joinGroupCode}
        })
            .then(result => {
                if (result.data.addStudentToGroupFromCode) {
                    this.setState({joinGroupCode: ''});
                    this.showNotification({
                        text: `Joined to group: ${result.data.addStudentToGroupFromCode.name}`,
                        type: 'success'
                    })
                    this.loadData();
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: `No group with code: ${this.state.joinGroupCode}`, type: 'danger'})
            })
    }

    handleInputChange = (event) => {
        this.setState({joinGroupCode: event.target.value.toUpperCase().slice(0, 8),});
    }

    showNotification = ({text, type}) => {
        toaster.notify(() => (
            <div className={`alert alert-${type}`}>
                {text}
            </div>
        ));
    }

    render() {
        const [joinGroupCode, setJoinGroupCode] = useState('');
        const {loading, error, data} = useQuery(studentPageGql.STUDENT_GROUPS, {
            variables: {userId: AuthenticationService.getUserId(), groupCode: this.state.joinGroupCode}
        });

        if (!this.state.user) {
            return (<div/>);
        }
        return (
            <div className="container">

                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-auto">{this.state.user.name}</h1>
                    <h1 className="col-auto rounded-pill bg-warning px-3">{this.state.user.code}</h1>
                </div>

                <div className="row rounded shadow bg-light my-3 p-3">
                    <h1 className="col-12">Student Groups</h1>
                </div>

                <div className="row input-group mb-3">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Join group</span>
                    </div>
                    <input type="text" className="form-control" placeholder="GROUP CODE"
                           value={this.state.joinGroupCode} onChange={this.handleInputChange}/>
                    <div className="input-group-append">
                        <button className="btn btn-outline-primary" onClick={() => this.joinGroup()}>
                            Join
                        </button>
                    </div>
                </div>

                {this.state.user.studentGroups &&
                <div className="row my-3">
                    <ul className="col-12 list-group">
                        {this.state.user.studentGroups.map(group =>
                            <li
                                className="list-group-item list-group-item-action"
                                key={group.id} onClick={() =>
                                this.groupClicked(group.id)}>

                                <strong className="mx-2"> {group.name}</strong>

                                {group.news && this.isFresh(new Date(group.newsChangedDate)) &&
                                <span className="badge badge-pill bg-warning text-light mx-2 px-2 py-1">
                                    {this.formatDate(new Date(group.newsChangedDate))}
                                </span>}
                            </li>
                        )}
                    </ul>
                </div>}
            </div>
        );
    }

}

export default StudentPage;