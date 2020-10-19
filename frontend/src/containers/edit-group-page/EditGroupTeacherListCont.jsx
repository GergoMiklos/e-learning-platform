import React, {useState} from 'react'
import gql from "graphql-tag";
import toast from "toasted-notes";
import {useMutation} from "@apollo/client";
import EditGroupTeacherElementCont from "./EditGroupTeacherElementCont";
import EditGroupTeacherListComp from "../../components/edit-group-page/EditGroupTeacherListComp";
import PropTypes, {number, string} from "prop-types";

const ADD_TEACHER_MUTATION = gql`
    mutation AddTeacherFromCodeToGroup($groupId: ID!, $userCode: String!) {
        addTeacherFromCodeToGroup(groupId: $groupId, userCode: $userCode)  {
            id
            ...TeacherDetails
        }
    }
${EditGroupTeacherElementCont.fragments.USER_DETAILS_FRAGMENT}`;

export default function EditGroupTeacherListCont({groupId, teachers}) {
    const [addUserCode, setAddUserCode] = useState('');

    const [addTeacher] = useMutation(ADD_TEACHER_MUTATION, {
        onCompleted: (data) => toast.notify(`Teacher added: ${data.addStudentFromCodeToGroup.name}`),
        onError: () => toast.notify(`No user with code: ${addUserCode}`),
        update: (cache) => {
            cache.modify({
                id: `Group:${groupId}`,
                fields: {
                    students(existingUserRefs, {INVALIDATE}) {
                        return INVALIDATE;
                    },
                },
            });
        },
    });

    return (
        <EditGroupTeacherListComp
            groupId={groupId}
            teachers={teachers}
            addUserCode={addUserCode}
            onAddUserCodeChange={value => setAddUserCode(value.toUpperCase().slice(0, 8))}

            onAddTeacher={() => addTeacher({
                variables: {
                    groupId: groupId,
                    userCode: addUserCode
                }
            })}
        />
    );
}

EditGroupTeacherListCont.propTypes = {
    groupId: PropTypes.oneOfType([number, string]).isRequired,
    teachers: PropTypes.arrayOf(PropTypes.object.isRequired).isRequired,
}
