import gql from "graphql-tag";
import {useMutation} from "@apollo/client";
import toast from "toasted-notes";
import React from "react";
import EditGroupUserElementComp from "./EditGroupUserElementComp";

const DELETE_STUDENT_MUTATION = gql`
    mutation DeleteStudentFromGroup($userId: ID!, $groupId: ID!) {
        deleteStudentFromGroup(userId: $userId, groupId: $groupId)
    }`;

export default function EditGroupStudentElementComp(props) {
    const [deleteStudent] = useMutation(DELETE_STUDENT_MUTATION, {
        onCompleted: (data) => toast.notify(`Student deleted successfully`),
        onError: () => toast.notify(`Error`),
        update: (cache) => cache.modify({
            id: `Group:${props.groupId}`,
            fields: {
                students(existingUserRefs, {readField}) {
                    return existingUserRefs.filter(userRef => props.userId !== readField('id', userRef));
                },
            }
        }),
    });
    return (
        <EditGroupUserElementComp
            userId={props.userId}
            selectedUserId={props.selectedUserId}
            deleteUser={() => deleteStudent({variables: {userId: props.userId, groupId: props.groupId}})}
        />
    );
}