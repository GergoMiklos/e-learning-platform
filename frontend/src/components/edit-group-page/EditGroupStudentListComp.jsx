import React, { useState } from 'react';
import PropTypes, { number, string } from 'prop-types';
import EditGroupStudentElementCont from '../../containers/edit-group-page/EditGroupStudentElementCont';

export default function EditGroupStudentListComp({
  students,
  addUserCode,
  onAddUserCodeChange,
  onAddStudent,
  groupId,
}) {
  const [selectedUserId, selectUserId] = useState(null);

  return (
    <div>
      <div className="row rounded shadow bg-light my-3 p-3">
        <h1 className="col-12">Students</h1>
      </div>

      <div className="row input-group px-3">
        <div className="input-group-prepend">
          <span className="input-group-text">New Student: </span>
        </div>
        <input
          type="text"
          className="form-control"
          placeholder="USER CODE"
          value={addUserCode}
          onChange={(event) => onAddUserCodeChange(event.target.value)}
        />
        <div className="input-group-append">
          <button className="btn btn-primary" onClick={() => onAddStudent()}>
            Add
          </button>
        </div>
      </div>

      <div className="row my-3">
        <ul className="col-12 list-group">
          {students?.length === 0
            ? 'No Students'
            : students.map((user) => (
                <li
                  className="list-group-item list-group-item-action"
                  key={user.id}
                  onClick={() => selectUserId(user.id)}
                >
                  <EditGroupStudentElementCont
                    user={user}
                    groupId={groupId}
                    selectedUserId={selectedUserId}
                  />
                </li>
              ))}
        </ul>
      </div>
    </div>
  );
}

EditGroupStudentListComp.propTypes = {
  groupId: PropTypes.oneOfType([number, string]).isRequired,
  students: PropTypes.arrayOf(PropTypes.object.isRequired).isRequired,
  addUserCode: PropTypes.string.isRequired,
  onAddUserCodeChange: PropTypes.func.isRequired,
  onAddStudent: PropTypes.func.isRequired,
};
