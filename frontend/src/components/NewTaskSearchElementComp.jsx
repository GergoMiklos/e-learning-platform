import gql from "graphql-tag";
import client from "../ApolloClient";
import {useMutation} from "@apollo/client";
import toast from "toasted-notes";
import React from "react";

const TASK_DETAILS_FRAGMENT = gql`
    fragment TaskDetails on Task {
        id
        usage
        question
        answers {
            id
            number
            answer
        }
        solutionNumber
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
        onCompleted: (data) => toast.notify('Task added successfully'),
        onError: () => toast.notify(`Error :(`),
        update: (cache, {data: {addTaskToTest}}) => {
            cache.modify({
                id: `Test:${props.testId}`,
                fields: {
                    //Todo kellenek ezek ide?
                    testTasks(existingTestTaskRefs, {INVALIDATE}) {
                        return INVALIDATE;
                    },

                    //     studentGroups(existingTestTaskRefs = [], { readField }) {
                    //
                    //         const newTestTaskRef = cache.writeFragment({
                    //             data: addTaskToTest,
                    //             fragment: gql`
                    //                 fragment addGroup on Group {
                    //                     id
                    //                     name
                    //                     news
                    //                     newsChangedDate
                    //                 }`
                    //         });
                    //
                    //         if (existingTestTaskRefs.some(ref => readField('id', ref) === newTestTaskRef.id)) {
                    //             return existingTestTaskRefs;
                    //         }
                    //
                    //         return [...existingTestTaskRefs, newTestTaskRef];
                    //     },
                },
            });
        },
    });
    return (
        <div className="container">
            <div className="row justify-content-between">
                <strong className="col-10">{task.question}</strong>
                <span className="col-auto">{task.usage}</span>
            </div>

            {(props.selectedTaskId === task.id) &&
            <div className="row rounded bg-secondary m-2 p-1"> {task.answers.map(
                (answer, i) =>
                    <div key={answer.number}
                         className={(answer.number === task.solutionNumber) ? 'font-weight-bold col-12' : 'font-weight-light col-12'}>
                        {`${i + 1}. ${answer.answer}`}
                    </div>
            )}
            </div>
            }

            {(props.selectedTaskId === task.id) &&
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
            }
        </div>
    );
}

NewTaskSearchElementComp.fragments = {
    TASK_DETAILS_FRAGMENT: TASK_DETAILS_FRAGMENT,
}