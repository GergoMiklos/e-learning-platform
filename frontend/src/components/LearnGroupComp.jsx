import React, {Component} from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import toaster from 'toasted-notes';
import AuthenticationService from "../AuthenticationService";

const STUDENT_GROUP = gql`
    query getGroup($groupId: ID!) {
        group(groupId: $groupId) {
            id
            name
            code
            description
            news
            newsChangedDate
            tests {
                id
                name
            }
        }
    }`;

const LEAVE_GROUP = gql`
    mutation DeleteUserFromGroup($userId: ID!, $groupId: ID!) {
        deleteStudentFromGroup(userId: $userId, groupId: $groupId)
    }`;

class LearnGroupComp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            group: null,
        };
    }

    componentDidMount() {
        this.loadData();
    }

    loadData = () => {
        client
            .query({
                query: STUDENT_GROUP,
                variables: {groupId: this.props.match.params.groupid},
            })
            .then(result => {
                console.log(result);
                if (result.data) {
                    this.setState({group: result.data.group});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'error'});
            });
    }

    testClicked = (id) => {
        this.props.history.push(`/learn/group/${this.props.match.params.groupid}/test/${id}`);
    }

    formatDate = (date) => {
        const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric' };
        return new Date(date).toLocaleString(/*navigator.language*/ 'en', options); //todo
    }

    leaveGroup = () => {
        let userId = AuthenticationService.getUserId();
        client.mutate({
            mutation: LEAVE_GROUP,
            variables: {userId: userId, groupId: this.state.group.id}
        })
            .then(result => {
                this.showNotification({text: 'Group left successfully', type: 'success'});
                this.props.history.push(`/learn`);
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'error'});
            })
    }

    navigateBack = () => {
        this.props.history.push(`/learn`);
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
            return (<div/>);
        }
        return (
            <div className="container">
                <div className="row justify-content-between my-1">
                    <button className="col-auto btn btn-secondary" onClick={() => this.navigateBack()}>
                        Back
                    </button>
                    <button className="col-auto btn btn-outline-danger" onClick={() => this.leaveGroup()}>
                        Leave
                    </button>
                </div>

                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-auto">{this.state.group.name}</h1>
                    <h1 className="col-auto rounded-pill bg-warning px-3">{this.state.group.code}</h1>
                    <i className="col-12 mt-3">{this.state.group.description}</i>
                </div>

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">News</h1>
                </div>

                {this.state.group.news &&
                <div className="row alert alert-warning my-3">
                    <span className="badge badge-primary text-center mr-2 mb-1 py-1">
                        {this.formatDate(new Date(this.state.group.newsChangedDate))}
                    </span>
                    <i>{this.state.group.news}</i>
                </div>}

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">Online Tests</h1>
                </div>

                {this.state.group.tests &&
                <div className="row my-3">
                    <table className="col-12 table table-striped table-hover rounded shadow">
                        <tbody>
                        {
                            this.state.group.tests.map(
                                test =>
                                    <tr key={test.id} onClick={() => this.testClicked(test.id)}>
                                        <td>
                                            <strong>{test.name}</strong>
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

export default LearnGroupComp;