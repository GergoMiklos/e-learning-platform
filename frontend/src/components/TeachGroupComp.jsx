import React, {Component} from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import NewTestDialogComp from "./NewTestDialogComp";
import toaster from "toasted-notes";

const TEACHER_GROUP = gql`
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
                description
            }
        }
    }`;

const CHANGE_NEWS = gql`
    mutation ChangeNews($groupId: ID!, $text: String!) {
        changeGroupNews(groupId: $groupId, text: $text) {
            id
            name
            code
            description
            news
            newsChangedDate
            tests {
                id
                name
                description
            }
        }
    }`;

class TeachGroupComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            group: null,
            changeNewsText: null,
            showNewTestDialog: false,
            selectedTestId: null,
        };
    }

    componentDidMount() {
        this.loadData();
    }

    loadData = () => {
        client
            .query({
                query: TEACHER_GROUP,
                variables: {groupId: this.props.match.params.groupid},
                fetchPolicy: 'network-only',
            })
            .then(result => {
                if (result.data) {
                    this.setState({group: result.data.group});
                }
            })
            .catch(errors => {
                console.log(errors);
            });
    }

    changeNews = () => {
        if (!this.state.changeNewsText)
            return
        client.mutate({
            mutation: CHANGE_NEWS,
            variables: {groupId: this.state.group.id, text: this.state.changeNewsText},
        })
            .then(result => {
                if (result.data) {
                    this.setState({group: result.data.changeGroupNews});
                    this.showNotification({text: 'News changed successfully', type: 'success'})
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'})
            });
    }

    handleNewsChange = (event) => {
        this.setState({changeNewsText: event.target.value.slice(0, 500)});
    }

    editGroup = () => {
        this.props.history.push(`/teach/group/${this.state.group.id}/edit`)
    }

    newTest = () => {
        this.setState({showNewTestDialog: true});
    }

    onNewTestDialogHide = () => {
        this.loadData();
        this.setState({showNewTestDialog: false});
    }

    editTest = (id) => {
        this.props.history.push(`/teach/group/${this.state.group.id}/test/${id}/edit`)
    }

    testStatuses = (id) => {
        this.props.history.push(`/teach/group/${this.state.group.id}/test/${id}`)
    }

    selectTest = (id) => {
        if (this.state.selectedTestId === id) {
            this.setState({selectedTestId: null});
        } else {
            this.setState({selectedTestId: id});
        }

    }

    navigateBack = () => {
        this.props.history.push(`/teach`)
    }

    formatDate = (date) => {
        const options = {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: 'numeric',
            minute: 'numeric'
        };
        return new Date(date).toLocaleString(/*navigator.language*/ 'en', options); //todo
    }

    showNotification = ({text, type}) => {
        toaster.notify(() => (
            <div className={`alert alert-${type}`}>
                {text}
            </div>
        ))
    }

    render() {
        if (!this.state.group) {
            return (<div/>)
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
                        <span className="input-group-text">
                            Change news
                        </span>
                    </div>
                    <input type="text" className="form-control" placeholder="news" onChange={this.handleNewsChange}/>
                    <div className="input-group-append">
                        <button className="btn btn-outline-primary" onClick={() => this.changeNews()}
                                disabled={!this.state.changeNewsText}>
                            Change
                        </button>
                    </div>
                </div>

                {this.state.group.news &&
                <div className="row alert alert-warning my-3">
                    <div className="badge badge-primary text-center mr-2 mb-1 py-1">
                        {this.formatDate(new Date(this.state.group.newsChangedDate))}
                    </div>
                    <div className="col-12 ">
                        {this.state.group.news}
                    </div>
                </div>}

                <div className="row rounded shadow my-3 p-3 d-flex justify-content-between">
                    <h1>Tests</h1>
                    <button className="btn btn-primary" onClick={() => this.newTest()}>
                        New
                    </button>
                </div>

                <NewTestDialogComp
                    show={this.state.showNewTestDialog}
                    onHide={this.onNewTestDialogHide}
                    groupId={this.state.group.id}
                />

                {this.state.group.tests &&
                <div className="row my-3">
                    <ul className="col-12 list-group">
                        {this.state.group.tests.map(test =>
                            <li
                                className="list-group-item list-group-item-action"
                                key={test.id}
                                onClick={() => this.selectTest(test.id)}>
                                <div className="d-flex justify-content-between">
                                    <strong>{test.name}</strong>
                                    {this.state.selectedTestId === test.id &&
                                    <span>
                                        <button className="btn btn-primary btn-sm"
                                                onClick={() => this.testStatuses(test.id)}>
                                            Statuses
                                        </button>
                                        <button className="btn btn-outline-warning btn-sm"
                                                onClick={() => this.editTest(test.id)}>
                                            Edit
                                        </button>
                                    </span>}
                                </div>
                            </li>
                        )}
                    </ul>
                </div>}
            </div>
        );
    }


}

export default TeachGroupComp;