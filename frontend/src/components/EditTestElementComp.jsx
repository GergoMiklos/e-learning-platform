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

//todo itt lehtne loadolni egy fájlból is, és úgy beállítani
const TESTTASK_DETAILS_FRAGMENT = gql`
    fragment TestTaskDetails on TestTask {
        id
        level
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

export default function EditTestElementComp(props) {
    //todo error handling (null/error) / useQuery cache-first?
    const testTask = client.readFragment({
        id: `TestTask:${props.testTaskId}`,
        fragment: TESTTASK_DETAILS_FRAGMENT,
    });

    const [selectedLevel, setUnsavedLevel] = useState({changed: false, level: testTask.level});
    const [deleteTask] = useMutation(DELETE_TASK_MUTATION, {
        onCompleted: () => toast.notify(`Task deleted successfully`),
        onError: () => toast.notify(`Error :(`),
        //Todo kell a testId, és kikéne törölni a changedből is!!
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

    return (
        <div className="container">
            <div className="row justify-content-between">
                <strong className="col-10">{testTask.task.question}</strong>
                <span
                    className={`col-auto align-self-start align-items-start" badge badge-${selectedLevel.changed ? 'warning' : 'primary'} p-2`}
                >
                        {selectedLevel.level}
                </span>
            </div>

            {(props.selectedTestTaskId === testTask.id) &&
            <div className="row rounded bg-secondary m-2 p-1"> {testTask.task.answers.map(
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
            <div className="row justify-content-end">
                <select
                    value={selectedLevel.level}
                    onChange={(event) => {
                        setUnsavedLevel({changed: true, level: event.target.value});
                        props.onLevelChange(event.target.value);
                    }}
                >
                    {props.levels.map((level) =>
                        <option value={level} key={level}>
                            Level: {level}
                        </option>
                    )}
                </select>
                <button
                    className="btn btn-outline-warning btn-sm"
                    onClick={() => deleteTask({variables: {testTaskId: testTask.id}})}
                >
                    Delete
                </button>
            </div>
            }
        </div>
    );
}

EditTestElementComp.fragments = {
    TESTTASK_DETAILS_FRAGMENT: TESTTASK_DETAILS_FRAGMENT,
}
