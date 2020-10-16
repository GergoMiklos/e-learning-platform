import gql from "graphql-tag";
import client from "../../ApolloClient";
import {useMutation} from "@apollo/client";
import toast from "toasted-notes";
import React from "react";
import NewTaskSearchElementComp from "../../components/new-task-page/NewTaskSearchElementComp";
import {taskLevels} from "../../constants";
import PropTypes, {number, string} from "prop-types";

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

export default function NewTaskSearchElementCont({testId, taskId, selectedTaskId, selectedLevel, onSelectLevel}) {

    const task = client.readFragment({
        id: `Task:${taskId}`,
        fragment: TASK_DETAILS_FRAGMENT,
    });

    const [addTask] = useMutation(ADD_TASK_MUTATION, {
        onCompleted: () => toast.notify('Task added successfully'),
        onError: () => toast.notify(`Error :(`),
        update: (cache) => {
            cache.modify({
                id: `Test:${testId}`,
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
        <NewTaskSearchElementComp
                task={task}
                isSelected={selectedTaskId === taskId}
                selectedLevel={selectedLevel}
                levels={taskLevels}
                onSelectLevel={onSelectLevel}

                onAddTask={() => addTask({
                    variables: {
                        testId: testId,
                        taskId: taskId,
                        level: selectedLevel
                    }
                })}
        />
    );
}

NewTaskSearchElementCont.fragments = {
    TASK_DETAILS_FRAGMENT: TASK_DETAILS_FRAGMENT,
}

NewTaskSearchElementCont.propTypes = {
    testId: PropTypes.oneOfType([number, string]).isRequired,
    taskId: PropTypes.oneOfType([number, string]).isRequired,
    selectedTaskId: PropTypes.oneOfType([number, string]),
    selectedLevel: PropTypes.number.isRequired,
    onSelectLevel: PropTypes.func.isRequired,
}