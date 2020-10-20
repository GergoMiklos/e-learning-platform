import React, {useState} from 'react';
import {Link, Route, useRouteMatch} from "react-router-dom";
import NewTaskDialogCont from "../../containers/new-task-page/NewTaskDialogCont";
import NewTaskSearchElementCont from "../../containers/new-task-page/NewTaskSearchElementCont";
import PropTypes, {number, string} from "prop-types";


export default function NewTaskPageComp({searchData, onSearch, onLoadMore, isMore, selectedLevel, selectLevel, onNavigateBack, searchText, onSearchTextChange, testId}) {

    let match = useRouteMatch();
    const [selectedTaskId, selectTaskId] = useState(null);

    return (
        <div className="container">
            <button className="row btn btn-secondary mt-1"
                    onClick={() => onNavigateBack()}>
                Back
            </button>

            <section className="row rounded shadow bg-light my-3 p-3 justify-content-between">
                <h1>Add Tasks</h1>
                <Link to={`${match.url}/new`}>
                    <button className="btn btn-lg btn-primary">
                        {'New'}
                    </button>
                </Link>
            </section>

            <Route path={`${match.url}/new`} render={(props) =>
                (<NewTaskDialogCont {...props} />)
            }/>

            <section className="row input-group mb-3">
                <input type="text" className="form-control" placeholder="Search for tasks"
                       value={searchText}
                       onChange={event => onSearchTextChange(event.target.value)}
                />
                <div className="input-group-append">
                    <button
                        className="btn btn-primary"
                        onClick={() => onSearch()}
                        onKeyDown={e => {
                            if (e.key === 'Enter') onSearch()
                        }}
                        autoFocus={true}
                        onFocus={e => e.currentTarget.focus()}
                    >
                        Search
                    </button>
                </div>
            </section>

            <i className="row m-1">
                {searchData.totalElements ? `${searchData.totalElements} results:` : 'No Results'}
            </i>

            <section className="row my-3">
                <ul className="col-12 list-group">
                    {searchData.tasks?.map(task =>
                        <li
                            className="list-group-item list-group-item-action"
                            key={task.id}
                            onClick={() => selectTaskId(task.id)}
                        >
                            <NewTaskSearchElementCont
                                testId={testId}
                                task={task}
                                selectedTaskId={selectedTaskId}
                                selectedLevel={selectedLevel}
                                onSelectLevel={level => selectLevel(level)}
                            />
                        </li>
                    )}
                </ul>
            </section>

            {isMore &&
            <button
                className="row btn btn-secondary btn-block"
                onClick={() => onLoadMore()}
            >
                Load More
            </button>
            }
        </div>
    );
}

NewTaskPageComp.propTypes = {
    searchData: PropTypes.object.isRequired,
    selectedLevel: PropTypes.number.isRequired,
    searchText: PropTypes.string.isRequired,
    onSearchTextChange: PropTypes.func.isRequired,
    onNavigateBack: PropTypes.func.isRequired,
    selectLevel: PropTypes.func.isRequired,
    isMore: PropTypes.bool.isRequired,
    onLoadMore: PropTypes.func.isRequired,
    onSearch: PropTypes.func.isRequired,
    testId: PropTypes.oneOfType([number, string]).isRequired,
}

