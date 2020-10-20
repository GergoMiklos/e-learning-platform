import React from 'react';
import ParentElementCont from "../../containers/parent-page/ParentElementCont";
import PropTypes from "prop-types";


export default function ParentPageComp({user, addFollowedCode, onAddFollowedCodeChange, isAddFollowedDisabled, onAddFollowedStudent}) {

    return (
        <div className="container">
            <section className="row input-group mx-0 my-3">
                <div className="input-group-prepend">
                    <span className="input-group-text">Follow student:</span>
                </div>
                <input
                    type="text" className="form-control"
                    placeholder="USER CODE"
                    value={addFollowedCode}
                    onChange={event => onAddFollowedCodeChange(event.target.value)}
                />
                <div className="input-group-append">
                    <button
                        className="btn btn-outline-primary"
                        onClick={() => onAddFollowedStudent()}
                        disabled={isAddFollowedDisabled}
                    >
                        Add
                    </button>
                </div>
            </section>

            <section className="row btn-group btn-block m-1">
                <div className="col-3 btn btn-info disabled">Not Started</div>
                <div className="col-3 btn btn-success disabled">In Progress</div>
                <div className="col-3 btn btn-warning disabled">Inactive</div>
                <div className="col-3 btn btn-danger disabled">Problem</div>
            </section>

            {!user.followedStudents?.length ? "No Students" :
                user.followedStudents.map(student =>
                    <ParentElementCont
                        key={student.id}
                        student={student}
                    />
                )}
        </div>
    );
}


ParentPageComp.propTypes = {
    user: PropTypes.object.isRequired,
    isAddFollowedDisabled: PropTypes.bool.isRequired,
    addFollowedCode: PropTypes.string.isRequired,
    onAddFollowedStudent: PropTypes.func.isRequired,
    onAddFollowedCodeChange: PropTypes.func.isRequired,
}