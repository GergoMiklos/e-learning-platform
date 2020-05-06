import React, { Component } from 'react'

class LearnLiveTestComp extends Component {
    constructor(props){
        super(props)
        this.state = {
            started: false,
            answered: false,
            finished: false,
            test : {
                name: 'Matematika teszt',
                question: 'Mi a jó válasz?',
                answers: ['A válasz', 'B válasz', 'C válasz', 'D válasz']
            }
        }
    }


    render() {
        return (
            <div className="container">
                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">{this.state.test.name}</h1>
                </div>
                {!this.state.started && <div className="row">
                    {this.state.test.description}
                    <button className="col-12 btn btn-primary" onClick={() => this.testStart()}>Start</button>
                </div>
                }
                {this.state.started && !this.state.finished && <div className="rounded shadow my-3 p-3">
                    <h2>
                        {this.state.test.question}
                    </h2>
                    <div className="row my-3">
                        <ul className="list-group col-12">
                            {
                                this.state.test.answers.map(
                                    answer =>
                                        <button className="m-2 btn btn-primary" onClick={() => this.taskAnswer()}>
                                            {answer}
                                        </button>
                                )
                            }
                        </ul>
                    </div>
                </div>
                }
                {this.state.started && !this.state.finished && this.state.answered &&
                <button className="col-12 btn btn-primary" onClick={() => this.nextTask()}>Next</button>
                }
                {this.state.finished && <div className="row">
                    Teszt vége
                    <button className="col-12 btn btn-primary">Befejezés</button>
                </div>
                }
            </div>
        );
    }

    testStart = () => {
        this.setState({started: true})
    }

    taskAnswer = () => {
        this.setState({answered: true})
    }

    nextTask = () => {
        this.setState({answered: false})
    }

    testFinish = () => {
        console.log("Test befejezése");
    }
}

export default LearnLiveTestComp;