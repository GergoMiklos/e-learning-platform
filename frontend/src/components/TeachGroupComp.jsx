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
                text
                sinceRefreshHours
            }
            liveTests {
                id
                sinceCreatedDays
                test {
                    name
                }
            }
            tests {
                id
                name
            }
        }
    }`;

const CHANGENEWS = gql`
    mutation ChangeNews($groupId: ID!, $text: String!) {
        changeNews(groupId: $groupId, text: $text) {
            id
            text
        }
    }`;

class TeachGroupComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            group: null,
            changeNewsText: null
        };
    }

    componentDidMount() {
        this.refreshGroup();
    }

    refreshGroup = () => {
        client
            .query({
                query: GROUP,
                variables: {id: this.props.match.params.groupid},
                fetchPolicy: 'network-only'
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

    liveTestClicked = (id) => {
        this.props.history.push(`/teach/group/${this.state.group.id}/test/${id}`)
    }

    changeNews = () => {
        if(!this.state.changeNewsText)
            return
        client.mutate({
                mutation: CHANGENEWS,
                variables: {groupId: this.state.group.id, text: this.state.changeNewsText}
            })
            .then(result => {
                console.log(result);
                this.refreshGroup();
            })
            .catch(errors => {
                console.log(errors);
            });
    }

    handleNewsChange = (event) => {
        this.setState({changeNewsText: event.target.value});
    }

    getNewsRefreshTime = (sinceHours) => {
        if(sinceHours <= 24)
            return `${sinceHours} hours ago`
        else
            return `${parseInt(sinceHours/24)} days ago`
    }

    newLiveTest = () => {
        this.props.history.push(`/teach/group/${this.state.group.id}/livetest/new`)
    }

    editGroup = () => {
        this.props.history.push(`/teach/group/${this.state.group.id}/edit`)
    }

    newTest = () => {
        this.props.history.push(`/teach/group/${this.state.group.id}/test/new`)
    }

    testClicked = (id) => {
        this.props.history.push(`/teach/group/${this.state.group.id}/test/${id}/edit`)
    }

    navigateBack = () => {
        this.props.history.push(`/teach`)
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
                    <button className="col-auto btn btn-outline-warning" onClick={() => this.editGroup()}>
                        Edit
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

                <div className="row input-group mb-3">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Change news</span>
                    </div>
                    <input type="text" className="form-control" placeholder="news" onChange={this.handleNewsChange}/>
                    <div className="input-group-append">
                        <button className="btn btn-outline-primary" onClick={() => this.changeNews()}>
                            Change
                        </button>
                    </div>
                </div>

                {this.state.group.news &&
                <div className="row alert alert-warning my-3">
                    <span className="badge badge-primary text-center mr-2 mb-1 py-1">
                        {this.getNewsRefreshTime(this.state.group.news.sinceRefreshHours)}
                    </span>
                    <i>{this.state.group.news.text}</i>
                </div> }

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Online Tests</h1>
                    <button className="col-2 btn btn-primary" onClick={() => this.newLiveTest()}>
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
                                    <tr key={liveTest.id} onClick={() => this.liveTestClicked(liveTest.id)}>
                                        <td>
                                            <strong>{liveTest.test.name}</strong>
                                        </td>
                                        <td className="text-sm-right">
                                            <i>{liveTest.sinceCreatedDays} days ago</i>
                                        </td>
                                    </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div> }

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Offline Tests</h1>
                    <button className="col-2 btn btn-primary" onClick={() => this.newTest()}>
                        New
                    </button>
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
                </div> }
            </div>
        );
    }


}

export default TeachGroupComp;