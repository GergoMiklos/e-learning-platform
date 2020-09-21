import React, {Component} from "react";
import client from "../ApolloClient";
import toaster from "toasted-notes";
import gql from "graphql-tag";

const DELETE_TASK = gql`
    mutation DeleteTaskFromTest($testTaskId: ID!) {
        deleteTaskFromTest(testTaskId: $testTaskId)
    }`;

class EditTestElementComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            unsavedChange: false,
            selectedLevel: this.props.testTask.level,
            deleted: false,
        };
    }

    deleteTask = () => {
        client.mutate({
            mutation: DELETE_TASK,
            variables: {testTaskId: this.props.testTask.id}
        })
            .then(() => {
                this.showNotification({text: 'Task deleted successfully', type: 'success'});
                this.setState({deleted: true});
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'});
            })
    }

    selectUnsavedLevel = (level) => {
        this.setState({unsavedChange: true, selectedLevel: level});
        this.props.onLevelChange(this.props.testTask.id, level);
    }

    showNotification = ({text, type}) => {
        toaster.notify(() => (
            <div className={`alert alert-${type}`}>
                {text}
            </div>
        ))
    }

    calculateColor() {
        if (this.state.deleted) {
            return 'light';
        }
        if (this.state.unsavedChange) {
            return 'warning';
        } else {
            return 'primary';
        }
    }

    render() {
        return (
            <div className="container">
                <div className="row justify-content-between">
                    <strong className="col-10">{this.props.testTask.task.question}</strong>
                    <span className={`col-auto align-self-start align-items-start" badge badge-${this.calculateColor()} p-2`}>
                        {this.state.selectedLevel}
                    </span>
                </div>

                {(this.props.selectedTestTaskId === this.props.testTask.id) &&
                <div className="row"> {this.props.testTask.task.answers.map(
                    (answer, i) =>
                        <div key={answer.number}
                             className={(answer.number === this.props.testTask.task.solutionNumber) ? 'font-weight-bold col-12' : 'font-weight-light col-12'}>
                            {`${i + 1}. ${answer.answer}`}
                        </div>
                )}
                </div>
                }

                {(this.props.selectedTestTaskId === this.props.testTask.id) && !this.state.deleted &&
                <div className="row justify-content-end">
                    <select value={this.state.selectedLevel}
                            onChange={(event) => this.selectUnsavedLevel(event.target.value)}>
                        {this.props.levels.map((level) =>
                            <option value={level} key={level}>
                                Level: {level}
                            </option>
                        )}
                    </select>
                    <button className="btn btn-danger btn-sm" onClick={() => this.deleteTask()}>
                        Delete
                    </button>
                </div>
                }
            </div>

        );
    }

}

export default EditTestElementComp;