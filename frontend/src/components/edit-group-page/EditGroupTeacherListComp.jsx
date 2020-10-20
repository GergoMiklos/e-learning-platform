import React, {useState} from 'react'
import EditGroupTeacherElementCont from "../../containers/edit-group-page/EditGroupTeacherElementCont";
import PropTypes, {number, string} from "prop-types";


export default function EditGroupTeacherListComp({teachers, addUserCode, onAddUserCodeChange, onAddTeacher, groupId}) {
    const [selectedUserId, selectUserId] = useState(null);

    return (
        <div>
            <div className="row rounded shadow bg-light my-3 p-3">
                <h1 className="col-12">Teachers</h1>
            </div>

            <div className="row input-group px-3">
                <div className="input-group-prepend">
                    <span className="input-group-text">New Teacher: </span>
                </div>
                <input
                    type="text"
                    className="form-control"
                    placeholder="USER CODE"
                    value={addUserCode}
                    onChange={event => onAddUserCodeChange(event.target.value)}
                />
                <div className="input-group-append">
                    <button className="btn btn-primary" onClick={() => onAddTeacher()}>
                        Add
                    </button>
                </div>
            </div>

            <div className="row my-3">
                <ul className="col-12 list-group">
                    {teachers?.length === 0 ? "No Teachers" : teachers.map(user =>
                        <li
                            className="list-group-item list-group-item-action"
                            key={user.id}
                            onClick={() => selectUserId(user.id)}
                        >
                            <EditGroupTeacherElementCont
                                user={user}
                                groupId={groupId}
                                selectedUserId={selectedUserId}
                            />
                        </li>
                    )}
                </ul>
            </div>
        </div>
    );
}

EditGroupTeacherListComp.propTypes = {
    groupId: PropTypes.oneOfType([number, string]).isRequired,
    teachers: PropTypes.arrayOf(PropTypes.object.isRequired).isRequired,
    addUserCode: PropTypes.string.isRequired,
    onAddUserCodeChange: PropTypes.func.isRequired,
    onAddTeacher: PropTypes.func.isRequired,
}