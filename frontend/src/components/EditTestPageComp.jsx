import React, {Component} from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import toaster from "toasted-notes";
import EditTestDetailsComp from "./EditTestDetailsComp";
import EditTestElementComp from "./EditTestElementComp";

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

//TODO
const CHANGE_TESTTASK_LEVEL = gql`
    mutation ChangeTestTaskLevel($testTaskInputs: [TestTaskInput!]) {
        changeTestTaskLevel(testTaskInputs: $testTaskInputs) {
            id
        }
    }`;

class EditTestPageComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            test: null,
            selectedTestTaskId: null,
            levels: Array.from({length: 10}, (_, i) => i + 1),
            unsavedTestTaskLevels: new Map(),
            testTasksGroupedByLevel: new Map(),
        };
    }

    navigateBack = () => {
        this.props.history.push(`/teach/group/${this.props.match.params.groupid}`);
    }

    selectTestTask = (id) => {
        this.setState({selectedTestTaskId: id});
    }

    newTask = () => {
        this.props.history.push(`/teach/test/${this.state.test.id}/tasks`);
    }

    componentDidMount() {
        this.loadData();
    }

    calculateTestTasksGroupedByLevel() {
        const result = new Map();
        this.state.levels.forEach((level) => {
                let levelGroup = this.state.test.testTasks.filter(testTask => testTask.level === level);
                if(levelGroup.length) {
                    result.set(level, levelGroup);
                }
            }
        )
        this.setState({testTasksGroupedByLevel: result});
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
                this.calculateTestTasksGroupedByLevel();
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'});
            });
    }

    addUnsavedTestTaskLevel(testTaskId, level) {
        this.setState({
            unsavedTestTaskLevels: this.state.unsavedTestTaskLevels.set(testTaskId, level),
        });
    }

    changeTestTaskLevels = () => {
        client.mutate({
            mutation: CHANGE_TESTTASK_LEVEL,
            variables: {
                testTaskInputs: [...this.state.unsavedTestTaskLevels]
                    .map(([testTaskId, level]) => ({id: testTaskId, level: level}))
            },
        })
            .then(() => {
                this.showNotification({text: 'Levels changed successfully', type: 'success'});
                this.setState({unsavedTestTaskLevels: new Map(),});
                this.loadData();
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

                <div className="row rounded shadow my-3 p-3 justify-content-between">
                    <h1>Tasks</h1>
                    <button className="btn btn-primary" onClick={() => this.newTask()}>Add New</button>
                </div>

                <button className="row btn btn-block btn-lg btn-warning m-1"
                        disabled={!this.state.unsavedTestTaskLevels.size}
                        onClick={() => this.changeTestTaskLevels()}
                >
                    Save Levels!
                </button>

                <div className="row">
                    {[ ...this.state.testTasksGroupedByLevel].map(([level, levelGroup]) =>
                        <div key={level} className="col-12">
                            <div className="mt-3 text-primary">Level {level}:</div>
                            <ul className="list-group">
                                {levelGroup.map(testTask =>
                                    <li
                                        className="list-group-item list-group-item-action"
                                        key={testTask.id}
                                        onClick={() => this.selectTestTask(testTask.id)}
                                    >
                                        <EditTestElementComp
                                            testTask={testTask}
                                            selectedTestTaskId={this.state.selectedTestTaskId}
                                            onDelete={() => this.loadData()}
                                            onLevelChange={(testTaskId, level) => this.addUnsavedTestTaskLevel(testTaskId, level)}
                                            levels={this.state.levels}
                                        />
                                    </li>
                                )}
                            </ul>
                        </div>
                    )}
                </div>
            </div>
        );
    }

}


export default EditTestPageComp;