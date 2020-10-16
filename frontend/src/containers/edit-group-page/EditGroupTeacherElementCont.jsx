import gql from "graphql-tag";
import {useMutation} from "@apollo/client";
import toast from "toasted-notes";
import EditGroupUserElementComp from "../../components/edit-group-page/EditGroupUserElementComp";
import React from "react";
import PropTypes, {number, string} from "prop-types";
import client from "../../ApolloClient";

const USER_DETAILS_FRAGMENT = gql`
    fragment TeacherDetails on User {
        id
        name
        code
    }`;

const DELETE_TEACHER_MUTATION = gql`
    mutation DeleteTeacherFromGroup($userId: ID!, $groupId: ID!) {
        deleteTeacherFromGroup(userId: $userId, groupId: $groupId)
    }`;


export default function EditGroupTeacherElementCont({userId, groupId, selectedUserId}) {
    const user = client.readFragment({
        id: `User:${userId}`,
        fragment: USER_DETAILS_FRAGMENT,
    });

    const [deleteTeacher] = useMutation(DELETE_TEACHER_MUTATION, {
        onCompleted: () => toast.notify(`Teacher deleted successfully`),
        onError: () => toast.notify(`Error`),
        update: (cache) => cache.modify({
            id: `Group:${groupId}`,
            fields: {
                teachers(existingUserRefs, {readField}) {
                    return existingUserRefs.filter(userRef => userId !== readField('id', userRef));
                },
            }
        }),
    });

    if(!user) {
        return (<div/>);
    }

    return (
        <EditGroupUserElementComp
            user={user}
            isSelected={selectedUserId === userId}
            onDelete={() => deleteTeacher({variables: {userId: userId, groupId: groupId}})}
        />
    );
}

EditGroupTeacherElementCont.fragments = {
    USER_DETAILS_FRAGMENT: USER_DETAILS_FRAGMENT,
}

EditGroupTeacherElementCont.propTypes = {
    userId: PropTypes.oneOfType([number, string]).isRequired,
    groupId: PropTypes.oneOfType([number, string]).isRequired,
    selectedUserId: PropTypes.oneOfType([number, string]),
}