import React, { Component } from 'react'

class TaskListComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            selected: null,
            tasks : [{
                id: 1,
                question: 'Mi a jó válasz?',
                answers: ['A válasz', 'B válasz', 'C válasz', 'D válasz']
            },
                {
                    id: 2,
                    question: 'Mi a jó már megint?',
                    answers: ['A válasz', 'B válasz', 'C válasz', 'Dddd válasz']
                }

            ],
        };
    }


    render() {
        return (
            <div className="container">

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Tasks</h1>
                    <button className="col-2 btn btn-warning btn" onClick={() => this.newTask()}>New</button>
                </div>

                <div className="row my-3">
                    <table className="col-12 table table-striped rounded shadow">
                        <tbody>
                        {
                            this.state.tasks.map(
                                task =>
                                    <tr onClick={() => this.taskClicked(task.id)}>
                                        <td>
                                            <div className="container">
                                                <div className="row">
                                                    <strong className="col-10">{task.question}</strong>
                                                    <button className="col-2 btn btn-warning btn">Add</button>
                                                </div>
                                                {(this.state.selected === task.id) && task.answers.map(
                                                    answer =>
                                                        <div className="row">{answer}</div>
                                                )

                                                }
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

    taskClicked = (id) => {
        console.log("Test " + id + " clicked!");
        if(this.state.selected === id) {
            this.setState({selected: null});
        } else {
            this.setState({selected: id});
        }
    }

    newTask= () => {
        this.props.history.push(`/teach/test/${this.props.match.params.testid}/tasks/new`)
    }

}

export default TaskListComp;