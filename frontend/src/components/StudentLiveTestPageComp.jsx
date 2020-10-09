import React, {useState} from 'react'
import gql from "graphql-tag";
import toast from "toasted-notes";
import {useMutation, useQuery} from "@apollo/client";
import AuthService from "../AuthService";
import {useHistory} from "react-router-dom";

const NEXT_TASK_QUERY = gql`
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

const CHECK_TASK_SOLUTION_MUTATION = gql`
    mutation CheckSolution($userId: ID!, $testId: ID!, $solutionNumber: Int!) {
        checkSolution(userId: $userId, testId: $testId, solutionNumber: $solutionNumber) {
            solutionNumber
            allSolutions
            correctSolutions
            solvedTasks
            allTasks
        }
    }`;


export default function StudentLiveTestPageComp(props) {
    const [chosenAnswerNumber, chooseAnswerNumber] = useState(null);
    const {loading, error, data, refetch} = useQuery(NEXT_TASK_QUERY, {
        variables: {userId: AuthService.getUserId(), testId: props.match.params.testid},
        fetchPolicy: "cache-first",
    },)
    const [checkSolution, {data: solutionData}] = useMutation(CHECK_TASK_SOLUTION_MUTATION, {
        onError: () => toast.notify(`Error :(`),
    },);

    if (loading) {
        return <div/>;
    }

    if (!data.nextTask) {
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
                    {data.nextTask.task.question}
                </h2>

                <div className="col-12 my-3">
                    <ul className="list-group col-12">
                        {data.nextTask.task.answers.map(
                            ({answer, number}) =>
                                <button
                                    key={number}
                                    className={`m-2 btn btn-lg text-left btn-${calculateAnswerColor({
                                        answerNumber: number,
                                        chosenAnswerNumber,
                                        correctAnswerNumber: data.nextTask.task.solutionNumber
                                    })}`}
                                    onClick={() => {
                                        checkSolution({
                                            variables: {
                                                userId: AuthService.getUserId(),
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
                        refetch();
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
    if (answerNumber === chosenAnswerNumber && answerNumber === correctAnswerNumber) {
        return 'success';
    }
    if (answerNumber === chosenAnswerNumber && answerNumber !== correctAnswerNumber) {
        return 'warning';
    } else {
        return 'light';
    }
}