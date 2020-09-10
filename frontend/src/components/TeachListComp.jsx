import React, { Component } from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import AuthenticationService from "../AuthenticationService";

const USERTEACH = gql`
    query User($id: ID!) {
        user(id: $id) {
            id
            managedGroups {
                id
                name
            }
        }
    }`;

class TeachListComp extends Component {
    constructor(props){
        super(props)
        this.state = {
            user: null
        };
    }

    newGroup = () => {
        this.props.history.push(`/teach/group/new`)
    }

    groupClicked = (id) => {
        this.props.history.push(`/teach/group/${id}`)
    }

    componentDidMount() {
        this.refreshGroups();
    }

    refreshGroups = () => {
        let userId = AuthenticationService.getUserId();
        client
            .query({
                query: USERTEACH,
                variables: {id: userId},
                fetchPolicy: 'network-only'
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

    render() {
        if(!this.state.user) {
            return (<div></div>)
        }
        return (
            <div className="container">
                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">My Groups</h1>
                    <button className="col-2 btn btn-primary" onClick={() => this.newGroup()}>
                        New
                    </button>
                </div>

                {this.state.user.managedGroups &&
                <div className="row my-3">
                    <table className="col-12 table table-striped table-hover rounded shadow">
                        <tbody>
                        {
                            this.state.user.managedGroups.map(
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
                </div> }

            </div>
        );
    }

}

export default TeachListComp;