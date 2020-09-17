import React, {Component} from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import toaster from "toasted-notes";
import EditTestDetailsComp from "./EditTestDetailsComp";

const TEST = gql`
    query getTest($testId: ID!) {
        test(testId: $testId) {
            id
            name
            description
            testTasks {
                id
                level
                task {
                    id
                    question
                    answers {
                        number
                        answer
                    }
                    solutionNumber
                }
            }
        }
    }`;

const DELETE_TASK = gql`
    mutation DeleteTaskFromTest($testTaskId: ID!) {
        deleteTaskFromTest(testTaskId: $testTaskId)
    }`;

class EditTestComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            test: null,
            selectedTestTaskId: null,
            error: null,
            success: null
        };
    }

    navigateBack = () => {
        this.props.history.push(`/teach/group/${this.props.match.params.groupid}`);
    }

    selectTestTask = (id) => {
        if (this.state.selectedTestTaskId === id) {
            this.setState({selectedTestTaskId: null});
        } else {
            this.setState({selectedTestTaskId: id});
        }
    }

    newTask = () => {
        this.props.history.push(`/teach/test/${this.state.test.id}/tasks`);
    }

    componentDidMount() {
        this.loadData();
    }

    loadData = () => {
        client
            .query({
                query: TEST,
                variables: {testId: this.props.match.params.testid},
                fetchPolicy: 'network-only',
            })
            .then(result => {
                if (result.data) {
                    this.setState({test: result.data.test});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'});
            });
    }

    showNotification = ({text, type}) => {
        toaster.notify(() => (
            <div className={`alert alert-${type}`}>
                {text}
            </div>
        ))
    }

    render() {
        if (!this.state.test) {
            return (<div/>)
        }
        return (
            <div className="container">
                <button className="row btn btn-secondary mt-1" onClick={() => this.navigateBack()}>
                    Back
                </button>

                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-auto">Edit Test</h1>
                </div>

                <EditTestDetailsComp className="row"
                                     testId={this.state.test.id}
                                     name={this.state.test.name}
                                     description={this.state.test.description}
                />

                <div className="row rounded shadow my-3 p-3 d-flex justify-content-between">
                    <h1>Tasks</h1>
                    <button className="btn btn-primary btn" onClick={() => this.newTask()}>Add New</button>
                </div>

                {this.state.test.testTasks &&
                <div className="row my-3">
                    <ul className="col-12 list-group">
                        {
                            this.state.test.testTasks.map(testTask =>
                                <li
                                    className="list-group-item list-group-item-action"
                                    key={testTask.id}
                                    onClick={() => this.selectTestTask(testTask.id)}
                                >
                                    <EditTestElementComp
                                        testTask={testTask}
                                        selectedTestTaskId={this.state.selectedTestTaskId}
                                        onDelete={() => this.loadData()}
                                    />
                                </li>
                            )}
                    </ul>
                </div>}
            </div>
        );
    }

}


class EditTestElementComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            selectedLevel: 1,
        };
    }

    deleteTask = (testTaskId) => {
        client.mutate({
            mutation: DELETE_TASK,
            variables: {testTaskId: testTaskId}
        })
            .then(() => {
                this.showNotification({text: 'Task deleted successfully', type: 'success'});
                this.props.onDelete()
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'});
            })
    }

    showNotification = ({text, type}) => {
        toaster.notify(() => (
            <div className={`alert alert-${type}`}>
                {text}
            </div>
        ))
    }

    render() {
        return (
            <div>
                <div className="d-flex justify-content-between">

                    <strong>{this.props.testTask.task.question}</strong>

                    {(this.props.selectedTestTaskId === this.props.testTask.id) &&
                    <button className="btn btn-danger btn-sm" onClick={() => this.deleteTask(this.props.testTask.id)}>
                        Delete
                    </button>}
                </div>

                {(this.props.selectedTestTaskId === this.props.testTask.id) && this.props.testTask.task.answers.map(
                    (answer, i) =>
                        <div className="collapse show">
                            <div key={answer.number}
                                 className={(answer.number === this.props.testTask.task.solutionNumber) ? 'font-weight-bold' : 'font-weight-light'}>
                                {`${i + 1}. ${answer.answer}`}
                            </div>
                        </div>
                )}
            </div>
        );
    }


}

export default EditTestComp;