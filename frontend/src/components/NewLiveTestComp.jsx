import React, { Component } from 'react'
import gql from "graphql-tag";
import AuthenticationService from "../AuthenticationService";
import client from "../ApolloClient";

const TESTS = gql`
    query User($id: ID!) {
        user(id: $id) {
            createdTests {
                id
                name
            }
        }
}`;

const ADDTEST = gql`
    mutation createLiveTest($groupId: ID!, $testId: ID!) {
        createLiveTest(groupId: $groupId, testId: $testId) {
            id
            test {
                name
            }
        }
    }`;

class NewLiveTestComp extends Component {
    constructor(props){
        super(props)
        this.state = {
            tests: null,
            error: null,
            success: null
        }
    }

    navigateBack = () => {
        this.props.history.push(`/teach/group/${this.props.match.params.groupid}`)
    }

    addTest = (testId) => {
        client.mutate({
            mutation: ADDTEST,
            variables: {groupId: this.props.match.params.groupid, testId: testId}
        })
            .then(result => {
                console.log(result);
                result.data.createLiveTest && this.setState({success: `Test added: ${result.data.createLiveTest.test.name}`, error: null});
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: 'Error!', success: null});
            })
    }

    componentDidMount() {
        this.refreshTests();
    }

    refreshTests = () => {
        let userId = AuthenticationService.getUserId();
        client
            .query({
                query: TESTS,
                variables: {id: userId}
            })
            .then(result => {
                console.log(result);
                if(!result.data.user) {
                    console.log("GraphQL query no result");
                } else if(result.data.user.createdTests) {
                    this.setState({tests: result.data.user.createdTests});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: "Error!"});
            });
    }

    render() {
        if(!this.state.tests) {
            return (
                <div>
                    <button className="btn btn-secondary my-3" onClick={() => this.navigateBack()}>
                        Back
                    </button>
                    <div className="alert alert-warning">
                        No tests available. Create one!
                    </div>

                </div>
            )
        }
        return (
            <div className="container">
                <button className="row btn btn-secondary my-1" onClick={() => this.navigateBack()}>
                    Back
                </button>

                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-10">Add Online Test</h1>
                </div>

                {this.state.error && <div className="alert alert-danger">{this.state.error}</div>}
                {this.state.success && <div className="alert alert-success">{this.state.success}</div>}

                <div className="row my-3">
                    <table className="col-12 table table-striped rounded shadow">
                        <tbody>
                        {
                            this.state.tests.map(
                                test =>
                                    <tr key={test.id}>
                                        <td>
                                            <div className="container">
                                                <div className="row">
                                                    <strong className="col-10">{test.name}</strong>
                                                    <button className="col-2 btn btn-primary btn-sm" onClick={() => this.addTest(test.id)}>
                                                        Add
                                                    </button>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }

}

export default NewLiveTestComp;