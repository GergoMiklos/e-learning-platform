import React, {useState} from "react";
import client from "../../ApolloClient";
import toast from "toasted-notes";
import gql from "graphql-tag";
import {useMutation} from "@apollo/client";
import EditTestElementComp from "../../components/edit-test-page/EditTestElementComp";
import {taskLevels} from "../../constants";
import PropTypes, {number, string} from "prop-types";

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

export default function EditTestElementCont({testId, testTaskId, selectedTestTaskId}) {

    const testTask = client.readFragment({
        id: `TestTask:${testTaskId}`,
        fragment: TESTTASK_DETAILS_FRAGMENT,
    });

    const [levelState, setLevel] = useState({changed: false, level: testTask.level});
    const [explanationState, setExplanation] = useState({changed: false, explanation: testTask.explanation ?? ''});

    const [deleteTask] = useMutation(DELETE_TASK_MUTATION, {
        onCompleted: () => toast.notify(`Task deleted successfully`),
        onError: () => toast.notify(`Error :(`),
        update: cache => {
            cache.modify({
                id: `Test:${testId}`,
                fields: {
                    testTasks(existingTestTaskRefs, {readField}) {
                        return existingTestTaskRefs.filter(
                            testTaskRef => testTaskId !== readField('id', testTaskRef)
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

    if(!testTask) {
        return (<div/>);
    }

    return (
        <EditTestElementComp
            testTask={testTask}
            isSelected={testTaskId === selectedTestTaskId}
            levelState={levelState}
            explanationState={explanationState}
            onLevelChange={value => setLevel({changed: true, level: value})}
            onExplanationChange={value => setExplanation({changed: true, explanation: value.slice(0, 500)})}
            levels={taskLevels}

            onEdit={() => editTestTask({
                variables: {
                    testId: testId,
                    testTaskInputs: [{
                        id: testTask.id,
                        level: levelState.level,
                        explanation: explanationState.explanation,
                    }],
                }
            })}
            isEditDisabled={!levelState.changed && !explanationState.changed}

            onDelete={() => deleteTask({variables: {testTaskId: testTaskId}})}
        />
    );
}

EditTestElementCont.fragments = {
    TESTTASK_DETAILS_FRAGMENT: TESTTASK_DETAILS_FRAGMENT,
}

EditTestElementCont.propTypes = {
    testId: PropTypes.oneOfType([number, string]).isRequired,
    testTaskId: PropTypes.oneOfType([number, string]).isRequired,
    selectedTestTaskId: PropTypes.oneOfType([number, string]),
}
