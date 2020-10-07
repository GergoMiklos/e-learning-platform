import React, {useState} from 'react'
import gql from "graphql-tag";
import toast from "toasted-notes";
import EditTestDetailsComp from "./EditTestDetailsComp";
import EditTestElementComp from "./EditTestElementComp";
import {useMutation, useQuery} from "@apollo/client";

const {Map: ImmutableMap} = require('immutable');

const TEST_QUERY = gql`
    query getTest($testId: ID!) {
        test(testId: $testId) {
            ...TestDetails
            testTasks {
                ...TestTaskDetails
            }
        }
    }
    ${EditTestElementComp.fragments.TESTTASK_DETAILS_FRAGMENT}
${EditTestDetailsComp.fragments.TEST_DETAILS_FRAGMENT}`;


//TODO ha az answerst kiszeded működni fog!!!
// const TEST_QUERY = gql`
//     query getTest($testId: ID!) {
//         test(testId: $testId) {
//             id
//             name
//             description
//             testTasks {
//                 id
//                 level
//                 task {
//                     id
//                     question
//                     answers {
//                         id
//                         number
//                         answer
//                     }
//                     solutionNumber
//                 }
//             }
//         }
//     }`;

const CHANGE_TESTTASK_LEVEL_MUTATION = gql`
    mutation ChangeTestTaskLevel($testTaskInputs: [TestTaskInput!]) {
        changeTestTaskLevel(testTaskInputs: $testTaskInputs) {
            ...TestTaskDetails
        }
    }
${EditTestElementComp.fragments.TESTTASK_DETAILS_FRAGMENT}`;

export default function EditTestPageComp(props) {
    const levels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]; //todo
    const [selectedTestTaskId, selectTestTaskId] = useState(null);
    const [unsavedTestTaskLevels, setUnsavedTestTaskLevels] = useState(ImmutableMap());
    const {data, loading, error} = useQuery(TEST_QUERY, {
        variables: {testId: props.match.params.testid},
        //fetchPolicy: 'cache-first',
        //fetchPolicy: 'cache-and-network',
        fetchPolicy: 'network-only',
    });
    const [changeTestTaskLevels] = useMutation(CHANGE_TESTTASK_LEVEL_MUTATION, {
        onCompleted: () => {
            setUnsavedTestTaskLevels(ImmutableMap());
            toast.notify(`Levels changed successfully`);
        },
        onError: () => toast.notify(`Error :(`),
    });

    if (!data) {
        return (<div/>);
    }

    return (
        <div className="container">
            <button
                className="row btn btn-secondary mt-1"
                onClick={() => props.history.push(`/teach/group/${props.match.params.groupid}`)}
            >
                Back
            </button>

            <div className="row bg-primary text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">Edit Test</h1>
            </div>

            <EditTestDetailsComp
                className="row"
                testId={data.test.id}
            />

            <div className="row rounded shadow bg-light my-3 p-3 justify-content-between">
                <h1>Tasks</h1>
                <button
                    className="btn btn-primary"
                    onClick={() => props.history.push(`/teach/group/${props.match.params.groupid}/test/${data.test.id}/tasks`)}
                >
                    Add New
                </button>
            </div>

            <button
                className="row btn btn-block btn-lg btn-warning m-1"
                disabled={!unsavedTestTaskLevels.size}
                onClick={() => changeTestTaskLevels({
                    variables: {
                        testTaskInputs: [...unsavedTestTaskLevels]
                            .map(([testTaskId, level]) => ({id: testTaskId, level: level}))
                    }
                })}
            >
                Save level changes!
            </button>

            <div className="row">
                {[...calculateTestTasksGroupedByLevel(data.test.testTasks, levels)].map(([level, levelGroup]) =>
                    <div key={level} className="col-12">
                        <div className="mt-3 font-weight-bold">Level {level}:</div>
                        <ul className="list-group">
                            {levelGroup.map(testTask =>
                                <li
                                    className="list-group-item list-group-item-action"
                                    key={testTask.id}
                                    onClick={() => selectTestTaskId(testTask.id)}
                                >
                                    <EditTestElementComp
                                        testTaskId={testTask.id}
                                        testId={data.test.id}
                                        selectedTestTaskId={selectedTestTaskId}
                                        onLevelChange={level => setUnsavedTestTaskLevels(unsavedTestTaskLevels.set(testTask.id, level))}
                                        levels={levels}
                                    />
                                </li>
                            )}
                        </ul>
                    </div>
                )}
            </div>
        </div>
    );
}


// export default function EditTestPageCont(props) {
//     //todo ez így nem lesz jó! Ez rohadt sokszor lefut! (too many re-renders)
//     const {loading, error, data} = useQuery(
//         TEST_QUERY, {
//             variables: {testId: props.match.params.testid},
//             //fetchPolicy: 'cache-first',
//             fetchPolicy: 'cache-and-network',
//             //fetchPolicy: 'network-only',
//         });
//     const levels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]; //todo
//     const [list, setList] = useState(null)
//     {console.log("lefutott")}
//     // {console.log(loading)}
//     // {console.log(data)}
//     useEffect(() => {
//         if(!loading) {
//             setList([...calculateTestTasksGroupedByLevel(data.test.testTasks, levels)]);
//         }
//     }, [data])
//
//     if (loading) {
//         return (<div/>);
//     }
//
//
//
//     return (
//         <EditTestPageComp
//             {...props}
//             data={data}
//             list={list}
//         />
//     );
// }

const calculateTestTasksGroupedByLevel = (testTasks, levels) => {
    const result = new Map();
    levels.forEach((level) => {
            let levelGroup = testTasks.filter(testTask => testTask.level === level);
            if (levelGroup.length) {
                result.set(level, levelGroup);
            }
        }
    )
    return result;
}