import React from 'react'
import PropTypes from "prop-types";
import GroupListElementCont from "../../containers/common/GroupListElementCont";

export default function StudentPageComp({user, joinCode, onJoinCodeChange, onJoinGroup, joinDisabled}) {

    return (
        <div className="container">
            <section className="row bg-primary text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">{user.name}</h1>
                <h1 className="col-auto rounded-pill bg-warning px-3">{user.code}</h1>
            </section>

            <section className="row rounded shadow bg-light my-3 p-3">
                <h1 className="col-12">Student Groups</h1>
            </section>

            <section className="row input-group mb-3">
                <div className="input-group-prepend">
                    <span className="input-group-text">Join group</span>
                </div>
                <input
                    type="text" className="form-control" placeholder="GROUP CODE"
                    value={joinCode}
                    onChange={event => onJoinCodeChange(event.target.value)}
                />
                <div className="input-group-append">
                    <button
                        data-testid="asd"
                        className="btn btn-outline-primary"
                        onClick={() => onJoinGroup()}
                        disabled={joinDisabled}
                    >
                        Join
                    </button>
                </div>
            </section>

            <section className="row my-3">
                <ul className="col-12 list-group">
                    {user.studentGroups?.length === 0 ? "No Groups" :
                        user.studentGroups.map(group =>
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

StudentPageComp.propTypes = {
    user: PropTypes.object.isRequired,
    joinCode: PropTypes.string.isRequired,
    onJoinCodeChange: PropTypes.func.isRequired,
    onJoinGroup: PropTypes.func.isRequired,
    joinDisabled: PropTypes.bool.isRequired,
}


