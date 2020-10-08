import React, {useState} from 'react'
import gql from "graphql-tag";
import NewTaskDialogComp from "./NewTaskDialogComp";
import NewTaskSearchElementComp from "./NewTaskSearchElementComp";
import {useLazyQuery} from "@apollo/client";
import {useHistory} from "react-router-dom";

const SEARCH_TASKS_QUERY = gql`
    query SearchTasks($searchText: String, $page: Int!) {
        searchTasks(searchText: $searchText, page: $page) {
            totalPages
            totalElements
            tasks {
                ...TaskDetails
            }
        }
    }
${NewTaskSearchElementComp.fragments.TASK_DETAILS_FRAGMENT}`;


export default function NewTaskPageComp(props) {
    const levels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]; //todo
    const [selectedTaskId, selectTaskId] = useState(null);
    const [selectedLevel, selectLevel] = useState(1);
    const [currentPage, setCurrentPage] = useState(0);
    const [searchText, setSearchText] = useState('');
    const [showNewTaskDialog, setShowNewTaskDialog] = useState(false);

    //TODO ez is végtelen hívás kb, olyan mintha a taskot nem bírná :D
    const [call, {data, loading, error, called, fetchMore}] = useLazyQuery(SEARCH_TASKS_QUERY, {
            fetchPolicy: "network-only", //todo :( lehetne no cache (ha nem readfragmentből olvasnánk)
            //fetchPolicy: "cache-first",
        }
    );

    if (!called && !data) {
        call({variables: {searchText: searchText, page: 0}});
    }

    if (!data) {
        return (<div/>)
    }

    return (
        <div className="container">
            <button className="row btn btn-secondary mt-1"
                    onClick={() => props.history.push(`/teach/group/${props.match.params.groupid}/test/${props.match.params.testid}/edit`)}>
                Back
            </button>

            <div className="row justify-content-between rounded shadow bg-light my-3 p-3">
                <h1>Add Tasks</h1>
                <button className="btn btn-primary" onClick={() => setShowNewTaskDialog(true)}>
                    New
                </button>
            </div>

            <NewTaskDialogComp
                show={showNewTaskDialog}
                onHide={() => setShowNewTaskDialog(false)}
                testId={props.match.params.testid}
                levels={levels}
                selectedLevel={selectedLevel}
                selectLevel={(level) => selectLevel(level)}
            />

            <div className="row input-group mb-3">
                <input type="text" className="form-control" placeholder="Search for tasks"
                       value={searchText} onChange={event => setSearchText(event.target.value)}/>
                <div className="input-group-append">
                    <button className="btn btn-primary"
                            onClick={() => call({variables: {searchText: searchText, page: 0}})}>
                        Search
                    </button>
                </div>
            </div>

            <i className="row m-1">
                {`${data.searchTasks.totalElements} results:`}
            </i>

            <div className="row my-3">
                <ul className="col-12 list-group">
                    {data.searchTasks.tasks.map(task =>
                        <li
                            className="list-group-item list-group-item-action"
                            key={task.id}
                            onClick={() => selectTaskId(task.id)}
                        >
                            <NewTaskSearchElementComp
                                testId={props.match.params.testid}
                                taskId={task.id}
                                selectedTaskId={selectedTaskId}
                                levels={levels}
                                selectedLevel={selectedLevel}
                                selectLevel={(level) => selectLevel(level)}
                            />
                        </li>
                    )}
                </ul>
            </div>

            {(currentPage + 1 < data.searchTasks.totalPages) &&
            <button
                className="row btn btn-secondary btn-block"
                onClick={() => fetchMore({ //TODO ezek rondák
                    onCompleted: () => setCurrentPage(currentPage + 1),
                    variables: {
                        searchText: searchText, page: currentPage + 1,
                    },
                    updateQuery: (prev, {moreSearchTasks}) => {
                        if (!moreSearchTasks) return prev;
                        return Object.assign({}, prev, {
                            tasks: [...prev.tasks, ...moreSearchTasks.searchTasks.tasks]
                        });
                    }
                })}
            >
                Load More
            </button>
            }
        </div>
    );
}


