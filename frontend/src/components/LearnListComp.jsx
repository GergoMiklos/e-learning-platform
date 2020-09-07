import React, { Component } from 'react'
import client from "../ApolloClient";
import gql from 'graphql-tag';
import AuthenticationService from "../AuthenticationService";

const USERLEARN = gql`
    query User($id: ID!) {
        user(id: $id) {
            id
            name
            code
            groups {
                id
                name
                news {
                    sinceRefreshHours
                }
            }
        }
    }`;

const JOINGROUP = gql`
    mutation AddUserToGroupFromCode($userId: ID!, $groupCode: String!) {
        addUserToGroupFromCode(userId: $userId, groupCode: $groupCode)  {
            name
        }
    }`;

class LearnListComp extends Component {
    constructor(props){
        super(props)
        this.state = {
            user: null,
            error: null,
            success: null,
            joinGroupCode: ''
        }
    }

    componentDidMount() {
        this.refreshGroups();
    }

    refreshGroups = () => {
        let userId = AuthenticationService.getUserId();
        client
            .query({
                query: USERLEARN,
                variables: {id: userId},
                fetchPolicy: 'no-cache'
            })
            .then(result => {
                console.log(result);
                if(!result.data.user) {
                    console.log("GraphQL query no result");
                } else {
                    this.setState({user: result.data.user});
                }
            })
            .catch(errors => {
                console.log(errors);
            });
    }

    groupClicked = (id) => {
        this.props.history.push(`/learn/group/${id}`)
    }

    getNewsRefreshTime = (sinceHours) => {
        if(sinceHours <= 24)
            return `${sinceHours} hours ago`
        else
            return `${parseInt(sinceHours/24)} days ago`
    }

    joinGroup = () => {
        if(!this.state.joinGroupCode)
            return;
        let userId = AuthenticationService.getUserId();
        client.mutate({
            mutation: JOINGROUP,
            variables: {userId: userId, groupCode: this.state.joinGroupCode}
        })
            .then(result => {
                console.log(result);
                if(result.data.addUserToGroupFromCode) {
                    this.setState({success: 'Joined to group: ' + result.data.addUserToGroupFromCode.name, error: null, joinGroupCode: ''});
                    this.refreshGroups();
                }
                else {
                    this.setState({error: 'No group with code: ' + this.state.joinGroupCode, success: null});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: 'Error', success: null});
            })
    }

    handleInputChange = (event) => {
        this.setState(
            {
                joinGroupCode : event.target.value.toUpperCase().slice(0, 8)
            }
        );
    }

    render() {
        if(!this.state.user) {
            return (<div>Loading...</div>)
        }
        return (
            <div className="container">
                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-auto">{this.state.user.name}</h1>
                    <h1 className="col-auto rounded-pill bg-warning px-3">{this.state.user.code}</h1>
                </div>

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">Groups</h1>
                </div>

                {this.state.error && <div className="alert alert-danger">{this.state.error}</div>}
                {this.state.success && <div className="alert alert-success">{this.state.success}</div>}

                <div className="row input-group mb-3">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Join group</span>
                    </div>
                    <input type="text" className="form-control" placeholder="GROUP CODE" value={this.state.joinGroupCode} onChange={this.handleInputChange}/>
                    <div className="input-group-append">
                        <button className="btn btn-outline-primary" onClick={() => this.joinGroup()}>
                            Join
                        </button>
                    </div>
                </div>

                {this.state.user.groups &&
                <div className="row my-3">
                    <table className="col-12 table table-striped table-hover rounded shadow">
                        <tbody>
                        {
                            this.state.user.groups.map(
                                group =>
                                    <tr key={group.id} onClick={() => this.groupClicked(group.id)}>
                                        <td>
                                            <div className="container">
                                                <div>
                                                    <strong className="col-auto">{group.name}</strong>
                                                    {group.news &&
                                                    <span className="col-auto badge badge-pill bg-warning mx-2 p-1">
                                                        {this.getNewsRefreshTime(group.news.sinceRefreshHours)}
                                                    </span>
                                                    }
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div> }
            </div>
        );
    }

}

export default LearnListComp;