import React, {Component} from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import NewTaskDialogComp from "./NewTaskDialogComp";
import toaster from "toasted-notes";

const TASKS = gql`
    query SearchTasks($searchText: String, $page: Int!) {
        searchTasks(searchText: $searchText, page: $page) {
            totalPages
            totalElements
            tasks {
                id
                usage
                question
                answers {
                    number
                    answer
                }
                solutionNumber
            }
        }
    }`;

const ADD_TASK = gql`
    mutation AddTaskToTest($testId: ID!, $taskId: ID!, $level: Int) {
        addTaskToTest(testId: $testId, taskId: $taskId, level: $level) {
            id
        }
    }`;

class NewTaskPageComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            selectedTaskId: null,
            currentPage: 0,
            totalPages: 0,
            totalElements: 0,
            tasks: [],
            searchText: '',
            showNewTaskDialog: false,
            selectedLevel: 1,
            levels: Array.from({length: 10}, (v, k) => k + 1),
        };
    }

    selectTask = (id) => {
        this.setState({selectedTaskId: id});
    }

    selectLevel = (level) => {
        this.setState({selectedLevel: level});
    }

    newTask = () => {
        this.setState({showNewTaskDialog: true});
    }

    onNewTaskDialogHide = () => {
        this.setState({showNewTaskDialog: false});
    }


    navigateBack = () => {
        this.props.history.push(`/teach/group/${this.props.match.params.groupid}/test/${this.props.match.params.testid}/edit`);
    }

    componentDidMount() {
        this.loadData(0);
    }

    handleInputChange = (event) => {
        this.setState({searchText: event.target.value.slice(0, 150)});
    }

    loadData = (page) => {
        client
            .query({
                query: TASKS,
                variables: {searchText: this.state.searchText, page: page}
            })
            .then(result => {
                console.log(result);
                if (result.data && result.data.searchTasks) {
                    this.setState({
                        tasks: (page === this.state.currentPage) ? result.data.searchTasks.tasks : this.state.tasks.concat(result.data.searchTasks.tasks),
                        totalPages: result.data.searchTasks.totalPages,
                        totalElements: result.data.searchTasks.totalElements,
                        currentPage: page,
                    });
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
        if (!this.state.tasks) {
            return (<div/>)
        }
        return (
            <div className="container">
                <button className="row btn btn-secondary mt-1" onClick={() => this.navigateBack()}>
                    Back
                </button>

                <div className="row justify-content-between rounded shadow my-3 p-3">
                    <h1>Add Tasks</h1>
                    <button className="btn btn-primary" onClick={() => this.newTask()}>
                        New
                    </button>
                </div>

                <NewTaskDialogComp
                    show={this.state.showNewTaskDialog}
                    onHide={() => this.onNewTaskDialogHide()}
                    testId={this.props.match.params.testid}
                    levels={this.state.levels}
                    selectedLevel={this.state.selectedLevel}
                    selectLevel={(level) => this.selectLevel(level)}
                />

                <div className="row input-group mb-3">
                    <input type="text" className="form-control" placeholder="Search for tasks"
                           value={this.state.searchText} onChange={this.handleInputChange}/>
                    <div className="input-group-append">
                        <button className="btn btn-primary" onClick={() => this.loadData(0)}>
                            Search
                        </button>
                    </div>
                </div>

                <i className="row m-1">
                    {`${this.state.totalElements} results:`}
                </i>

                <div className="row my-3">
                    <ul className="col-12 list-group">
                        {this.state.tasks.map(task =>
                            <li
                                className="list-group-item list-group-item-action"
                                key={task.id}
                                onClick={() => this.selectTask(task.id)}
                            >
                                <NewTaskSearchElementComp
                                    testId={this.props.match.params.testid}
                                    task={task}
                                    selectedTaskId={this.state.selectedTaskId}
                                    levels={this.state.levels}
                                    selectedLevel={this.state.selectedLevel}
                                    selectLevel={(level) => this.selectLevel(level)}
                                />
                            </li>
                        )}
                    </ul>
                </div>

                {(this.state.currentPage + 1 < this.state.totalPages) &&
                <button className="row btn btn-secondary btn-block"
                        onClick={() => this.loadData(this.state.currentPage + 1)}>
                    Load More
                </button>
                }
            </div>
        );
    }

}

class NewTaskSearchElementComp extends Component {
    constructor(props) {
        super(props)
    }

    addTask = (taskId) => {
        client.mutate({
            mutation: ADD_TASK,
            variables: {testId: this.props.testId, taskId: taskId, level: this.props.selectedLevel}
        })
            .then(() => {
                this.showNotification({text: 'Task added successfully', type: 'success'});
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
            <div className="container">
                <div className="row justify-content-between">
                    <strong className="col-10">{this.props.task.question}</strong>
                    <span className="col-auto">{this.props.task.usage}</span>
                </div>

                {(this.props.selectedTaskId === this.props.task.id) &&
                <div className="row"> {this.props.task.answers.map(
                    (answer, i) =>
                        <div key={answer.number}
                             className={(answer.number === this.props.task.solutionNumber) ? 'font-weight-bold col-12' : 'font-weight-light col-12'}>
                            {`${i + 1}. ${answer.answer}`}
                        </div>
                )}
                </div>
                }

                {(this.props.selectedTaskId === this.props.task.id) &&
                <div className="row justify-content-end">
                    <select value={this.props.selectedLevel}
                            onChange={(event) => this.props.selectLevel(event.target.value)}>
                        {this.props.levels.map((level) =>
                            <option value={level} key={level}>
                                Level: {level}
                            </option>
                        )}
                    </select>
                    <button className="btn btn-primary btn-sm"
                            onClick={() => this.addTask(this.props.task.id)}>
                        Add
                    </button>
                </div>
                }
            </div>
        );
    }


}

export default NewTaskPageComp;