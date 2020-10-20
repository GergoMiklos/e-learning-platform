import React, {useState} from 'react'
import StudentGroupElementCont from "../../containers/student-group-page/StudentGroupElementCont";
import PropTypes from "prop-types";
import {formatLongDate} from "../../utils/date-utils";


export default function StudentGroupPageComp({group, onLeaveGroup, onNavigateBack}) {
    const [selectedTestId, selectTestId] = useState(null)

    return (
        <div className="container">
            <section className="row justify-content-between my-1">
                <button
                    onClick={() => onNavigateBack()}
                    className="col-auto btn btn-secondary"
                >
                    Back
                </button>
                <button
                    onClick={() => onLeaveGroup()}
                    className="col-auto btn btn-outline-warning"
                >
                    Leave
                </button>
            </section>

            <section className="row bg-primary text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">{group.name}</h1>
                <h1 className="col-auto rounded-pill bg-warning px-3">{group.code}</h1>
                <i className="col-12 mt-3">{group.description}</i>
            </section>

            <section className="row rounded shadow bg-light my-3 p-3">
                <h1 className="col-12">News</h1>
            </section>

            {group.news &&
            <section className="row alert alert-warning my-3">
                <div className="col-auto badge badge-primary text-center mr-2 mb-1 py-1">
                    {formatLongDate(group.newsChangedDate)}
                </div>
                <div className="col-12">
                    {group.news}
                </div>
            </section>
            }

            <section className="row rounded shadow bg-light my-3 p-3">
                <h1 className="col-12">Tests</h1>
            </section>

            <section className="row my-3">
                <ul className="col-12 list-group">
                    {!group.tests?.length ? "No Tests" :
                        group.tests.map(test =>
                            <li
                                className="list-group-item list-group-item-action"
                                onClick={() => selectTestId(test.id)}
                                key={test.id}
                            >
                                <StudentGroupElementCont
                                    test={test}
                                    selectedTestId={selectedTestId}
                                />
                            </li>
                        )}
                </ul>
            </section>
        </div>
    );
}

StudentGroupPageComp.propTypes = {
    group: PropTypes.object.isRequired,
    onLeaveGroup: PropTypes.func.isRequired,
    onNavigateBack: PropTypes.func.isRequired,
}
