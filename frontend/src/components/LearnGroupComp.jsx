import React, { Component } from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import { useQuery } from "@apollo/react-hooks";
import AuthenticationService from "../AuthenticationService";

const GROUP = gql`
    query Group($id: ID!) {
        group(id: $id) {
            id
            name
            code
            description,
            news {
                text
                sinceRefreshHours
            }
            liveTests {
                id
                test {
                    name
                }
            }
        }
    }`;

const LEAVEGROUP = gql`
    mutation DeleteUserFromGroup($userId: ID!, $groupId: ID!) {
        deleteUserFromGroup(userId: $userId, groupId: $groupId)
    }`;

class LearnGroupComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            group: null
        };
    }

    componentDidMount() {
        this.refreshGroup();
    }

    refreshGroup = () => {
        client
            .query({
                query: GROUP,
                variables: {id: this.props.match.params.groupid}
            })
            .then(result => {
                console.log(result);
                if(!result.data.group) {
                    console.log("GraphQL query no result");
                } else {
                    this.setState({group: result.data.group});
                }
            })
            .catch(errors => {
                console.log(errors);
            });
    }

    testClicked = (id) => {
        this.props.history.push(`/learn/group/${this.props.match.params.groupid}/test/${id}`)
    }

    getNewsRefreshTime = (sinceHours) => {
        if(sinceHours <= 24)
            return `${sinceHours} hours ago`
        else
            return `${parseInt(sinceHours/24)} days ago`
    }

    leaveGroup = () => {
        let userId = AuthenticationService.getUserId();
        client.mutate({
            mutation: LEAVEGROUP,
            variables: {userId: userId, groupId: this.state.group.id}
        })
            .then(result => {
                console.log(result);
                this.props.history.push(`/learn`)
            })
            .catch(errors => {
                console.log(errors);
            })
    }

    navigateBack = () => {
        this.props.history.push(`/learn`)
    }

    render() {
        if(!this.state.group) {
            return (<div></div>)
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
                        {this.getNewsRefreshTime(this.state.group.news.sinceRefreshHours)}
                    </span>
                    <i>{this.state.group.news.text}</i>
                </div> }

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">Online Tests</h1>
                </div>

                {this.state.group.liveTests &&
                <div className="row my-3">
                    <table className="col-12 table table-striped table-hover rounded shadow">
                        <tbody>
                        {
                            this.state.group.liveTests.filter(lt => lt.test).map(
                                liveTest =>
                                    <tr key={liveTest.id} onClick={() => this.testClicked(liveTest.id)}>
                                        <td>
                                            <strong>{liveTest.test.name}</strong>
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

export default LearnGroupComp;