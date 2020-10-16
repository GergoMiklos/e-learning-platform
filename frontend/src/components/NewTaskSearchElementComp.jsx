import gql from "graphql-tag";
import client from "../ApolloClient";
import {useMutation} from "@apollo/client";
import toast from "toasted-notes";
import React from "react";
import {useHistory} from "react-router-dom";
import {Collapse} from "react-bootstrap";

const TASK_DETAILS_FRAGMENT = gql`
    fragment TaskDetails on Task {
        id
        usage
        question
        solutionNumber
        answers {
            id
            number
            answer
        }
    }`;

const ADD_TASK_MUTATION = gql`
    mutation AddTaskToTest($testId: ID!, $taskId: ID!, $level: Int) {
        addTaskToTest(testId: $testId, taskId: $taskId, level: $level) {
            id
            level
            task {
                ...TaskDetails
            }
        }
    }
${TASK_DETAILS_FRAGMENT}`;

export default function NewTaskSearchElementComp(props) {
    //todo error handling (null/error) / useQuery cache-first?
    const task = client.readFragment({
        id: `Task:${props.taskId}`,
        fragment: TASK_DETAILS_FRAGMENT,
    });

    const [addTask] = useMutation(ADD_TASK_MUTATION, {
        onCompleted: () => toast.notify('Task added successfully'),
        onError: () => toast.notify(`Error :(`),
        update: (cache, {data: {addTaskToTest}}) => {
            cache.modify({
                id: `Test:${props.testId}`,
                fields: {
                    testTasks(existingTestTaskRefs, {INVALIDATE}) {
                        return INVALIDATE;
                    },
                },
            });
        },
    });

    if(!task) {
        return (<div/>);
    }

    return (
        <div className="container">
            <article className="row justify-content-between">
                <strong className="col-10">{task.question}</strong>
                <span className="col-auto">{task.usage}</span>
            </article>

            <Collapse in={(props.selectedTaskId === task.id)}>
                <article>
                    <div className="row rounded bg-secondary my-2 py-1"> {task.answers.map(
                        (answer, i) =>
                            <div key={answer.number}
                                 className={(answer.number === task.solutionNumber) ? 'font-weight-bold col-12' : 'font-weight-light col-12'}>
                                {`${i + 1}. ${answer.answer}`}
                            </div>
                    )}
                    </div>
                    <div className="row justify-content-end">
                        <select
                            value={props.selectedLevel}
                            onChange={(event) => props.selectLevel(event.target.value)}
                        >
                            {props.levels.map((level) =>
                                <option value={level} key={level}>
                                    Level: {level}
                                </option>
                            )}
                        </select>
                        <button
                            className="btn btn-primary btn-sm"
                            onClick={() => addTask({
                                variables: {
                                    testId: props.testId,
                                    taskId: task.id,
                                    level: props.selectedLevel
                                }
                            })}
                        >
                            Add
                        </button>
                    </div>
                </article>
            </Collapse>
        </div>
    );
}

NewTaskSearchElementComp.fragments = {
    TASK_DETAILS_FRAGMENT: TASK_DETAILS_FRAGMENT,
}