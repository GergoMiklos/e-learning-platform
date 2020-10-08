import gql from "graphql-tag";
import {useMutation} from "@apollo/client";
import toast from "toasted-notes";
import EditGroupUserElementComp from "./EditGroupUserElementComp";
import React from "react";
import {useHistory} from "react-router-dom";

const DELETE_TEACHER_MUTATION = gql`
    mutation DeleteTeacherFromGroup($userId: ID!, $groupId: ID!) {
        deleteTeacherFromGroup(userId: $userId, groupId: $groupId)
    }`;


export default function EditGroupTeacherElementComp(props) {
    const [deleteTeacher] = useMutation(DELETE_TEACHER_MUTATION, {
        onCompleted: () => toast.notify(`Teacher deleted successfully`),
        onError: () => toast.notify(`Error`),
        update: (cache) => cache.modify({
            id: `Group:${props.groupId}`,
            fields: {
                //Todo melyik jobb?
                // students(existingUserRefs, {INVALIDATE}) {
                //     return INVALIDATE;
                // },
                teachers(existingUserRefs, {readField}) {
                    return existingUserRefs.filter(userRef => props.userId !== readField('id', userRef));
                },
            }
        }),
    });
    return (
        <EditGroupUserElementComp
            userId={props.userId}
            selectedUserId={props.selectedUserId}
            deleteUser={() => deleteTeacher({variables: {userId: props.userId, groupId: props.groupId}})}
        />
    );
}