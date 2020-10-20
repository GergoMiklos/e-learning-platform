import React from 'react'
import EditGroupDetailsCont from "../../containers/edit-group-page/EditGroupDetailsCont";
import EditGroupTeacherListCont from "../../containers/edit-group-page/EditGroupTeacherListCont";
import EditGroupStudentListCont from "../../containers/edit-group-page/EditGroupStudentListCont";
import PropTypes from "prop-types";


export default function EditGroupPageComp({group, onNavigateBack}) {

    return (
        <div className="container">
            <button className="row btn btn-secondary mt-1"
                    onClick={() => onNavigateBack()}>
                Back
            </button>

            <section className="row bg-primary text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">Edit Group</h1>
            </section>

            <EditGroupDetailsCont
                className="row"
                group={group}
            />

            <EditGroupTeacherListCont
                groupId={group.id}
                teachers={group.teachers}
            />

            <EditGroupStudentListCont
                groupId={group.id}
                students={group.students}
            />
        </div>
    );
}

EditGroupPageComp.propTypes = {
    group: PropTypes.object.isRequired,
    onNavigateBack: PropTypes.func.isRequired,
}
