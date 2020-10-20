import React from 'react'
import NewGroupDialogCont from "../../containers/teacher-page/NewGroupDialogCont";
import {Link, Route, useRouteMatch} from "react-router-dom";
import PropTypes from 'prop-types';
import GroupListElementCont from "../../containers/common/GroupListElementCont";


export default function TeacherPageComp({user}) {
    let match = useRouteMatch()

    return (
        <div className="container">
            <section className="row bg-warning text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">{user.name}</h1>
                <h1 className="col-auto rounded-pill bg-primary px-3">{user.code}</h1>
            </section>

            <section className="row rounded shadow bg-light my-3 p-3 justify-content-between">
                <h1>Teacher Groups</h1>
                <Link to={`${match.url}/new`}>
                    <button className="btn btn-lg btn-primary">
                        New
                    </button>
                </Link>
            </section>

            <Route path={`${match.url}/new`} render={(props) =>
                (<NewGroupDialogCont {...props} />)
            }/>

            <section className="row my-3">
                <ul className="col-12 list-group">
                    {!user.teacherGroups?.length ? "No Groups" :
                        user.teacherGroups.map(group =>
                            <li
                                className="list-group-item list-group-item-action"
                                key={group.id}
                            >
                                <GroupListElementCont group={group}/>
                            </li>
                        )}
                </ul>
            </section>
        </div>
    );
}

TeacherPageComp.propTypes = {
    user: PropTypes.object.isRequired,
}
