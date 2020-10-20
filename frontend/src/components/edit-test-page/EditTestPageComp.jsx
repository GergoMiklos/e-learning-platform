import React, {useState} from 'react'
import {Link} from "react-router-dom";
import EditTestDetailsCont from "../../containers/edit-test-page/EditTestDetailsCont";
import EditTestElementCont from "../../containers/edit-test-page/EditTestElementCont";
import PropTypes from "prop-types";


export default function EditTestPageComp({test, testTasks, onNavigateBack, newTaskPath}) {
    const [selectedTestTaskId, selectTestTaskId] = useState(null);

    return (
        <div className="container">
            <button onClick={() => onNavigateBack()} className="row btn btn-secondary mt-1">
                Back
            </button>

            <section className="row bg-primary text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">Edit Test</h1>
            </section>

            <EditTestDetailsCont test={test} className="row"/>

            <section className="row rounded shadow bg-light my-3 p-3 justify-content-between">
                <h1>Tasks</h1>
                <Link to={newTaskPath}>
                    <button className="btn btn-primary">
                        New
                    </button>
                </Link>
            </section>

            <section className="row">
                {!test.testTasks?.length ? "No Tasks" :
                    [...testTasks].map(([level, levelGroup]) =>
                    <div key={level} className="col-12">
                        <div className="mt-3">
                            Level {level}:
                        </div>
                        <ul className="list-group">
                            {levelGroup.map(testTask =>
                                <li
                                    className="list-group-item list-group-item-action"
                                    key={testTask.id}
                                    onClick={() => selectTestTaskId(testTask.id)}
                                >
                                    <EditTestElementCont
                                        testTask={testTask}
                                        testId={test.id}
                                        selectedTestTaskId={selectedTestTaskId}
                                    />
                                </li>
                            )}
                        </ul>
                    </div>
                )}
            </section>
        </div>
    );
}

EditTestPageComp.propTypes = {
    testTasks: PropTypes.object.isRequired,
    test: PropTypes.object.isRequired,
    onNavigateBack: PropTypes.func.isRequired,
    newTaskPath: PropTypes.string.isRequired,
}
