import React, { Component } from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";

const TASKS = gql`{
    tasks {
        id
        question
        answers
    }
}`;

const ADDTASK = gql`
    mutation AddTaskToTest($testId: ID!, $taskId: ID!, $level: Int) {
        addTaskToTest(testId: $testId, taskId: $taskId, level: $level) {
            id
        }
    }`;

class TaskListComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            selected: null,
            tasks: null,
            error: null,
            success: null
        };
    }

    taskClicked = (id) => {
        if(this.state.selected === id) {
            this.setState({selected: null});
        } else {
            this.setState({selected: id});
        }
    }

    newTask= () => {
        this.props.history.push(`/teach/test/${this.props.match.params.testid}/tasks/new`)
    }

    addTask = (taskId) => {
        client.mutate({
            mutation: ADDTASK,
            variables: {testId: this.props.match.params.testid, taskId: taskId, level: 0}
        })
            .then(result => {
                console.log(result);
                this.setState({success: 'Task added!', error: null});
                this.refreshTasks();
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: 'Error!', success: null});
            })
    }

    navigateBack = () => {
        this.props.history.push(`/teach/test/${this.props.match.params.testid}/edit`)
    }

    componentDidMount() {
        this.refreshTasks();
    }

    refreshTasks = () => {
        client
            .query({
                query: TASKS
            })
            .then(result => {
                console.log(result);
                if(!result.data.tasks) {
                    console.log("GraphQL query no result");
                } else {
                    this.setState({tasks: result.data.tasks});
                }
            })
            .catch(errors => {
                console.log(errors);
            });
    }


    render() {
        if(!this.state.tasks) {
            return (<div></div>)
        }
        return (
            <div className="container">
                <button className="row btn btn-secondary mt-1" onClick={() => this.navigateBack()}>
                    Back
                </button>

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Add Tasks</h1>
                    <button className="col-2 btn btn-primary" onClick={() => this.newTask()}>
                        New
                    </button>
                </div>

                {this.state.error && <div className="alert alert-danger">{this.state.error}</div>}
                {this.state.success && <div className="alert alert-success">{this.state.success}</div>}

                <div className="row my-3">
                    <table className="col-12 table table-striped rounded shadow">
                        <tbody>
                        {
                            this.state.tasks.map(
                                task =>
                                    <tr key={task.id} onClick={() => this.taskClicked(task.id)}>
                                        <td>
                                            <div className="container">
                                                <div className="row">
                                                    <strong className="col-10">{task.question}</strong>
                                                    {(this.state.selected === task.id) &&
                                                    <button className="col-2 btn btn-primary btn-sm" onClick={() => this.addTask(task.id)}>
                                                        Add
                                                    </button>}
                                                </div>
                                                {(this.state.selected === task.id) && task.answers.map(
                                                    (answer, i) =>
                                                        <div key={i} className="row">{answer}</div>
                                                )}
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

export default TaskListComp;