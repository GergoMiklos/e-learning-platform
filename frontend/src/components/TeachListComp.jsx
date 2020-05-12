import React, { Component } from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";

const USER = gql`    {
    user {
        id
        managedGroups {
            id
            name
        }
        createdTests {
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

    componentDidMount() {
        this.refreshGroups();
    }

    refreshGroups = () => {
        client
            .query({
                query: USER
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

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">My Tests</h1>
                    <button className="col-2 btn btn-primary" onClick={() => this.newTest()}>
                        New
                    </button>
                </div>

                {this.state.user.createdTests &&
                <div className="row my-3">
                    <table className="col-12 table table-striped table-hover rounded shadow">
                        <tbody>
                        {
                            this.state.user.createdTests.map(
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
                </div> }
            </div>
        );
    }

    newGroup = () => {
        this.props.history.push(`/teach/group/new`)
    }

    newTest = () => {
        this.props.history.push(`/teach/test/new`)
    }

    groupClicked = (id) => {
        this.props.history.push(`/teach/group/${id}`)
    }

    testClicked = (id) => {
        this.props.history.push(`/teach/test/${id}/edit`)
    }


}

export default TeachListComp;