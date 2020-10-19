import React, {useState} from 'react'
import gql from "graphql-tag";
import toast from "toasted-notes";
import {useMutation} from "@apollo/client";
import EditGroupStudentElementCont from "../../containers/edit-group-page/EditGroupStudentElementCont";
import EditGroupStudentListComp from "../../components/edit-group-page/EditGroupStudentListComp";
import PropTypes, {number, string} from "prop-types";

const ADD_STUDENT_MUTATION = gql`
    mutation AddStudentFromCodeToGroup($groupId: ID!, $userCode: String!) {
        addStudentFromCodeToGroup(groupId: $groupId, userCode: $userCode)  {
            id
            ...StudentDetails
        }
    }
${EditGroupStudentElementCont.fragments.USER_DETAILS_FRAGMENT}`;

export default function EditGroupStudentListCont({groupId, students}) {
    const [addUserCode, setAddUserCode] = useState('');

    const [addStudent] = useMutation(ADD_STUDENT_MUTATION, {
        onCompleted: (data) => toast.notify(`Student added: ${data.addStudentFromCodeToGroup.name}`),
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
        <EditGroupStudentListComp
            groupId={groupId}
            students={students}
            addUserCode={addUserCode}
            onAddUserCodeChange={value => setAddUserCode(value.toUpperCase().slice(0, 8))}

            onAddStudent={() => addStudent({
                variables: {
                    groupId: groupId,
                    userCode: addUserCode
                }
            })}
        />
    );
}

EditGroupStudentListCont.propTypes = {
    groupId: PropTypes.oneOfType([number, string]).isRequired,
    students: PropTypes.arrayOf(PropTypes.object.isRequired).isRequired,
}


