import React, {useState} from "react";
import client from "../ApolloClient";
import toast from "toasted-notes";
import gql from "graphql-tag";
import {useMutation} from "@apollo/client";
import {useHistory} from "react-router-dom";

const DELETE_TASK_MUTATION = gql`
    mutation DeleteTaskFromTest($testTaskId: ID!) {
        deleteTaskFromTest(testTaskId: $testTaskId)
    }`;

//todo itt lehtne loadolni egy fájlból is, és úgy beállítani?
const TESTTASK_DETAILS_FRAGMENT = gql`
    fragment TestTaskDetails on TestTask {
        id
        level
        explanation
        correctSolutions
        allSolutions
        task {
            id
            question
            answers {
                id
                number
                answer
            }
            solutionNumber
        }
    }`;

const EDIT_TESTTASK_MUTATION = gql`
    mutation EditTestTasks($testId: ID!, $testTaskInputs: [TestTaskInput!]!) {
        editTestTasks(testId: $testId, testTaskInputs: $testTaskInputs) {
            ...TestTaskDetails
        }
    }
${TESTTASK_DETAILS_FRAGMENT}`;

export default function EditTestElementComp(props) {
    //todo error handling (null/error) / useQuery cache-first?
    const testTask = client.readFragment({
        id: `TestTask:${props.testTaskId}`,
        fragment: TESTTASK_DETAILS_FRAGMENT,
    });
    const [levelState, setLevel] = useState({changed: false, level: testTask.level});
    const [explanationState, setExplanation] = useState({changed: false, explanation: testTask.explanation});

    const [deleteTask] = useMutation(DELETE_TASK_MUTATION, {
        onCompleted: () => toast.notify(`Task deleted successfully`),
        onError: () => toast.notify(`Error :(`),
        update: cache => {
            cache.modify({
                id: `Test:${(props.testId)}`,
                fields: {
                    testTasks(existingTestTaskRefs, {readField}) {
                        return existingTestTaskRefs.filter(
                            testTaskRef => props.testTaskId !== readField('id', testTaskRef)
                        );
                    },
                },
            });
        }

    });
    const [editTestTask] = useMutation(EDIT_TESTTASK_MUTATION, {
        onCompleted: () => toast.notify(`Task changed successfully`),
        onError: () => toast.notify(`Error :(`),
    });

    return (
        <div className="container">
            <div className="row justify-content-between">
                <strong className="col-12 col-lg-8">
                    {testTask.task.question}
                </strong>
                <span className="col-9 col-lg-2">
                    Correct/All: {testTask.allSolutions === 0 ? 0: Math.floor((testTask.correctSolutions/testTask.allSolutions)*100)}%
                </span>
                <span className="col-auto">
                    <div className={`badge badge-${levelState.changed ? 'warning' : 'primary'} p-2`}>
                        {levelState.level}
                    </div>
                </span>
            </div>

            {(props.selectedTestTaskId === testTask.id) &&
            <div className="row rounded bg-secondary my-2 p-1"> {testTask.task.answers.map(
                (answer, i) =>
                    <div
                        key={answer.number}
                        className={(answer.number === testTask.task.solutionNumber) ? 'font-weight-bold col-12' : 'font-weight-light col-12'}
                    >
                        {`${i + 1}. ${answer.answer}`}
                    </div>
            )}
            </div>
            }

            {(props.selectedTestTaskId === testTask.id) &&
            <div className="row input-group mb-3">
                <div className="input-group-prepend">
                    <label className="input-group-text">Explanation:</label>
                </div>
                <input
                    type="text"
                    className="form-control"
                    placeholder="Add explanation for the solution"
                    value={explanationState.explanation ?? ''}
                    onChange={event => setExplanation({changed: true, explanation: event.target.value.slice(0, 500)})}
                />
                <div className="input-group-append">
                    <select
                        className="col-auto"
                        value={levelState.level}
                        onChange={(event) => setLevel({changed: true, level: event.target.value})}
                    >
                        {props.levels.map((level) =>
                            <option value={level} key={level}>
                                Level: {level}
                            </option>
                        )}
                    </select>
                    <button
                        className="btn btn-primary"
                        onClick={() => editTestTask({
                            variables: {
                                testId: props.testId,
                                testTaskInputs: [{
                                    id: testTask.id,
                                    level: levelState.level,
                                    explanation: explanationState.explanation,
                                }],
                            }
                        })}
                        disabled={!levelState.changed && !explanationState.changed}
                    >
                        Save
                    </button>
                    <button
                        className="btn btn-outline-warning btn-sm"
                        onClick={() => deleteTask({variables: {testTaskId: testTask.id}})}
                    >
                        Delete
                    </button>
                </div>
            </div>
            }
        </div>
    );
}

EditTestElementComp.fragments = {
    TESTTASK_DETAILS_FRAGMENT: TESTTASK_DETAILS_FRAGMENT,
}
