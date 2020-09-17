import React, {Component} from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import NewTaskDialogComp from "./NewTaskDialogComp";

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

class NewTaskSearchComp extends Component {
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
        };
    }

    selectTask = (id) => {
        if (this.state.selectedTaskId === id) {
            this.setState({selectedTaskId: null});
        } else {
            this.setState({selectedTaskId: id});
        }
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
            });
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

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Add Tasks</h1>
                    <button className="col-2 btn btn-primary" onClick={() => this.newTask()}>
                        New
                    </button>
                </div>

                <NewTaskDialogComp
                    show={this.state.showNewTaskDialog}
                    onHide={() => this.onNewTaskDialogHide()}
                    testId={this.props.match.params.testid}
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

                <i className="row">
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
        this.state = {
            selectedLevel: 1,
        };
    }

    addTask = (taskId) => {
        client.mutate({
            mutation: ADD_TASK,
            variables: {testId: this.props.testId, taskId: taskId, level: this.state.selectedLevel}
        })
            .then(result => {
                console.log(result);
                this.setState({success: 'Task added!', error: null});
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: 'Error!', success: null});
            })
    }

    render() {
        return (
            <div>
                <div className="d-flex justify-content-between">

                    <strong>{this.props.task.question}</strong>

                    {(this.props.selectedTaskId === this.props.task.id) ?
                        <button className="btn btn-primary btn-sm"
                                onClick={() => this.addTask(this.props.task.id)}>
                            Add
                        </button> :
                        <span>{this.props.task.usage}</span>
                    }
                </div>

                {(this.props.selectedTaskId === this.props.task.id) && this.props.task.answers.map(
                    (answer, i) =>
                        <div key={answer.number}
                             className={(answer.number === this.props.task.solutionNumber) ? 'font-weight-bold' : 'font-weight-light'}>
                            {`${i + 1}. ${answer.answer}`}
                        </div>
                )}
            </div>
        );
    }


}

export default NewTaskSearchComp;