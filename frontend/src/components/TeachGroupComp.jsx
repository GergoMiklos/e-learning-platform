import React, { Component } from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";

const GROUP = gql`
    query Group($id: ID!) {
        group(id: $id) {
            id
            name
            code
            description,
            news {
                id
                text
            }
            liveTests {
                id
                test {
                    name
                }
            }
        }
    }`;

class TeachGroupComp extends Component {
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
        console.log("Test " + id + " clicked!");
    }

    newTest = () => {
        this.props.history.push(`/teach/group/${this.state.group.id}/test/new`)
    }

    editGroup = () => {
        this.props.history.push(`/teach/group/${this.state.group.id}/edit`)
    }

    render() {
        if(!this.state.group) {
            return (<div></div>)
        }
        return (
            <div className="container">
                <div className="row rounded shadow my-3 p-3">
                    <h3 className="col-12">{this.state.group.name}</h3>
                    <h5 className="col-10">({this.state.group.code})</h5>
                    <button className="col-2 btn btn-warning btn"  onClick={() => this.editGroup()}>
                        Edit
                    </button>
                    {this.state.group.description}
                </div>

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">News</h1>
                </div>

                <div className="row input-group mb-3">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Change news</span>
                    </div>
                    <input type="text" className="form-control" placeholder="news"/>
                    <div className="input-group-append">
                        <button className="btn btn-outline-secondary">Change</button>
                    </div>
                </div>

                {this.state.group.news &&
                <div className="row alert alert-warning my-3">
                    {this.state.group.news.text}
                </div> }

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Tests</h1>
                    <button className="col-2 btn btn-warning btn" onClick={() => this.newTest()}>
                        New
                    </button>
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

export default TeachGroupComp;