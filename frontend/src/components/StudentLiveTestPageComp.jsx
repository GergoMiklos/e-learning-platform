import React, {useEffect, useState} from 'react'
import gql from "graphql-tag";
import toast from "toasted-notes";
import {useMutation, useLazyQuery} from "@apollo/client";
import AuthService from "../AuthService";
import {useHistory} from "react-router-dom";

const NEXT_TASK_MUTATION = gql`
    mutation CalculateNextTask($testId: ID!) {
        calculateNextTask(testId: $testId) {
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

const CHECK_TASK_SOLUTION_MUTATION = gql`
    mutation CheckSolution($testId: ID!, $solutionNumber: Int!) {
        checkSolution(testId: $testId, solutionNumber: $solutionNumber) {
            solutionNumber
            allSolutions
            correctSolutions
            solvedTasks
            allTasks
        }
    }`;


export default function StudentLiveTestPageComp(props) {
    const [chosenAnswerNumber, chooseAnswerNumber] = useState(null);
    const [nextTask, {data: nextTaskData}] = useMutation(NEXT_TASK_MUTATION)
    const [checkSolution, {data: solutionData}] = useMutation(CHECK_TASK_SOLUTION_MUTATION, {
        onError: () => toast.notify(`Error :(`),
    },);

    useEffect(() => nextTask({
        variables: {testId: props.match.params.testid},
    }), []);

    if (!nextTaskData) {
        return <div/>;
    }

    if (!nextTaskData.nextTask) {
        return (
            <div
                className="middle"
                onClick={() => props.history.goBack()}
            >
                <strong className="bg-warning text-light rounded-pill shadow p-3">
                    No tasks available :(
                </strong>
            </div>
        );
    }

    return (
        <div className="container">
            <button
                className="row btn btn-secondary mt-1"
                onClick={() => props.history.goBack()}
            >
                Back
            </button>

            <div className="row rounded shadow bg-light my-3">
                <h2 className="col-12 m-1">
                    {nextTaskData.nextTask.task.question}
                </h2>

                <div className="col-12 my-3">
                    <ul className="list-group col-12">
                        {nextTaskData.nextTask.task.answers.map(
                            ({answer, number}) =>
                                <button
                                    key={number}
                                    className={`m-2 btn btn-lg text-left btn-${calculateAnswerColor({
                                        answerNumber: number,
                                        chosenAnswerNumber,
                                        correctAnswerNumber: nextTaskData.nextTask.task.solutionNumber
                                    })}`}
                                    onClick={() => {
                                        checkSolution({
                                            variables: {
                                                testId: props.match.params.testid,
                                                solutionNumber: number
                                            }
                                        });
                                        chooseAnswerNumber(number);
                                    }}
                                    disabled={chosenAnswerNumber}
                                >
                                    {answer}
                                </button>
                        )}
                    </ul>
                </div>
            </div>

            {chosenAnswerNumber && solutionData &&
            <div className="col-12 d-flex justify-content-between">
                <div>
                    <div>
                        Tasks(Solved/All): {solutionData.checkSolution.solvedTasks}/{solutionData.checkSolution.allTasks}
                    </div>
                    <div>
                        Answers(Correct/All): {solutionData.checkSolution.correctSolutions}/{solutionData.checkSolution.allSolutions}
                    </div>
                </div>

                <button
                    className="btn btn-primary btn-lg p-3"
                    onClick={() => {
                        chooseAnswerNumber(null)
                        nextTask({
                            variables: {userId: AuthService.getUserId(), testId: props.match.params.testid},
                        });
                    }}
                >
                    Next
                </button>
            </div>
            }
        </div>
    );
}

const calculateAnswerColor = ({answerNumber, chosenAnswerNumber, correctAnswerNumber}) => {
    if (!chosenAnswerNumber) {
        return 'primary';
    }
    if (answerNumber === correctAnswerNumber) {
        return 'success';
    }
    if (answerNumber === chosenAnswerNumber && answerNumber !== correctAnswerNumber) {
        return 'warning';
    } else {
        return 'light';
    }
}