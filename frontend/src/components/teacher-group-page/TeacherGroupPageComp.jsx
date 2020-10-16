import React, {useState} from 'react'
import {Link, Route, useHistory, useRouteMatch} from "react-router-dom";
import TeacherGroupElementCont from "../../containers/teacher-group-page/TeacherGroupElementCont";
import TeacherGroupNewsCont from "../../containers/teacher-group-page/TeacherGroupNewCont";
import NewTestDialogCont from "../../containers/teacher-group-page/NewTestDialogCont";
import PropTypes from "prop-types";


export default function TeacherGroupPageComp({group, editPath, onNavigateBack}) {
    let match = useRouteMatch();

    const [selectedTestId, setSelectedTestId] = useState(null);

    return (
        <div className="container">
            <section className="row justify-content-between my-1">
                <button onClick={() => onNavigateBack()} className="col-auto btn btn-secondary">
                    Back
                </button>
                <Link to={editPath} className="col-auto btn btn-outline-warning">
                    Edit
                </Link>
            </section>

            <section className="row bg-warning text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">{group.name}</h1>
                <h1 className="col-auto rounded-pill bg-primary px-3">{group.code}</h1>
                <i className="col-12 mt-3">{group.description}</i>
            </section>

            <section className="row rounded shadow bg-light my-3 p-3">
                <h1 className="col-12">News</h1>
            </section>

            <TeacherGroupNewsCont groupId={group.id} />

            {group.news &&
            <section className="row alert alert-warning my-3">
                <div className="badge badge-primary text-center mr-2 mb-1 py-1">
                    {formatLongDate(group.newsChangedDate)}
                </div>
                <div className="col-12 ">
                    {group.news}
                </div>
            </section>
            }

            <section className="row rounded shadow bg-light my-3 p-3 justify-content-between">
                <h1>Tests</h1>
                <Link to={`${match.url}/new`}>
                    <button className="btn btn-lg btn-primary">
                        {'New'}
                    </button>
                </Link>
            </section>

            <Route path={`${match.url}/new`} render={(props) =>
                (<NewTestDialogCont groupId={group.id} {...props} />)
            }/>

            <div className="row my-3">
                <ul className="col-12 list-group">
                    {!group.tests?.length ? "No Tests" :
                        group.tests.map(test =>
                            <li
                                className="list-group-item list-group-item-action"
                                onClick={() => setSelectedTestId(test.id)}
                                key={test.id}
                            >
                                <TeacherGroupElementCont
                                    testId={test.id}
                                    selectedTestId={selectedTestId}
                                />
                            </li>
                        )}
                </ul>
            </div>
        </div>
    );
}

TeacherGroupPageComp.propTypes = {
    group: PropTypes.object.isRequired,
    editPath: PropTypes.string.isRequired,
    onNavigateBack: PropTypes.func.isRequired,
}

const formatLongDate = (date) => {
    const options = {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric'
    };
    return new Date(date).toLocaleString(/*navigator.language*/ 'en', options); //todo
}
