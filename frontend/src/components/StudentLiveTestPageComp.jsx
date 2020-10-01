import React, {Component} from 'react'
import AuthenticationService from "../AuthenticationService";
import client from "../ApolloClient";
import gql from "graphql-tag";
import toaster from "toasted-notes";

const NEXT_TASK = gql`
    query getNextTask($userId: ID!, $testId: ID!) {
        nextTask(userId: $userId, testId: $testId) {
            task {
                question
                answers {
                    number
                    answer
                }
                solutionNumber
            }
        }
    }`;

const CHECK_TASK_SOLUTION = gql`
    mutation CheckSolution($userId: ID!, $testId: ID!, $solutionNumber: Int!) {
        checkSolution(userId: $userId, testId: $testId, solutionNumber: $solutionNumber) {
            solutionNumber
            allSolutions
            correctSolutions
            solvedTasks
            allTasks
        }
    }`;

class StudentLiveTestPageComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            answered: false,
            testTask: null,
            taskSolution: null,
            chosenAnswerNumber: null,
            correctAnswerNumber: null,
        }
    }

    loadNextTask = () => {
        let userId = AuthenticationService.getUserId();
        client.query({
            query: NEXT_TASK,
            variables: {userId: userId, testId: this.props.match.params.testid},
            fetchPolicy: 'no-cache',
        })
            .then((result) => {
                if (result.data) {
                    this.setState({testTask: result.data.nextTask});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'});
            })
    }

    answerTask = (number) => {
        this.setState({answered: true, chosenAnswerNumber: number})
        let userId = AuthenticationService.getUserId();
        client.mutate({
            mutation: CHECK_TASK_SOLUTION,
            variables: {userId: userId, testId: this.props.match.params.testid, solutionNumber: number}
        })
            .then(result => {
                if (result.data) {
                    this.setState(
                        {
                            taskSolution: result.data.checkSolution,
                            correctAnswerNumber: result.data.checkSolution.solutionNumber
                        });
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: `Something went wrong`, type: 'danger'})
            })
    }


    nextTask = () => {
        this.setState({answered: false, chosenAnswerNumber: null, correctAnswerNumber: null})
        this.loadNextTask();
    }

    navigateBack = () => {
        this.props.history.push(`/learn/group/${this.props.match.params.groupid}`);
    }

    componentDidMount() {
        this.loadNextTask();
    }

    showNotification = ({text, type}) => {
        toaster.notify(() => (
            <div className={`alert alert-${type}`}>
                {text}
            </div>
        ));
    }

    //todo használjuk inkább a már meglévő adatokat
    caclculateAnswerColor = (number) => {
        if (!this.state.answered || !this.state.correctAnswerNumber) {
            return 'primary';
        }
        if (number === this.state.chosenAnswerNumber && this.state.correctAnswerNumber === number) {
            return 'success';
        }
        if (number === this.state.chosenAnswerNumber && this.state.correctAnswerNumber !== number) {
            return 'danger';
        } else {
            return 'light';
        }
    }

    render() {
        if (!this.state.testTask) {
            return (<div/>);
        }
        return (
            <div className="container">
                <button className="row btn btn-secondary mt-1" onClick={() => this.navigateBack()}>
                    Back
                </button>

                <div className="row rounded shadow my-3">
                    <h2 className="col-12 m-1">
                        {this.state.testTask.task.question}
                    </h2>

                    <div className="col-12 my-3">
                        <ul className="list-group col-12">
                            {this.state.testTask.task.answers.map(
                                ({answer, number}) =>
                                    <button key={number}
                                            className={`m-2 btn btn-lg text-left btn-${this.caclculateAnswerColor(number)}`}
                                            onClick={() => this.answerTask(number)}
                                            disabled={this.state.answered}
                                    >
                                        {answer}
                                    </button>
                            )}
                        </ul>
                    </div>
                </div>

                {this.state.answered && this.state.taskSolution &&
                <div className="col-12 d-flex justify-content-between">
                    <div>
                        <div>
                            Tasks (Solved/All): {this.state.taskSolution.solvedTasks}/{this.state.taskSolution.allTasks}
                        </div>
                        <div>
                            Answers (Correct/All): {this.state.taskSolution.correctSolutions}/{this.state.taskSolution.allSolutions}
                        </div>
                    </div>
                    <button className="btn btn-primary btn-lg p-3" onClick={() => this.nextTask()}>Next</button>
                </div>
                }

            </div>
        );
    }

}

export default StudentLiveTestPageComp;