import gql from "graphql-tag";
import {useMutation} from "@apollo/client";
import toast from "toasted-notes";
import React from "react";
import EditGroupUserElementComp from "../../components/edit-group-page/EditGroupUserElementComp";
import client from "../../ApolloClient";
import PropTypes, {number, string} from "prop-types";

const USER_DETAILS_FRAGMENT = gql`
    fragment StudentDetails on User {
        id
        name
        code
    }`;

const DELETE_STUDENT_MUTATION = gql`
    mutation DeleteStudentFromGroup($userId: ID!, $groupId: ID!) {
        deleteStudentFromGroup(userId: $userId, groupId: $groupId)
    }`;


export default function EditGroupStudentElementCont({userId, groupId, selectedUserId}) {
    const user = client.readFragment({
        id: `User:${userId}`,
        fragment: USER_DETAILS_FRAGMENT,
    });

    const [deleteStudent] = useMutation(DELETE_STUDENT_MUTATION, {
        onCompleted: () => toast.notify(`Student deleted successfully`),
        onError: () => toast.notify(`Error`),
        update: (cache) => cache.modify({
            id: `Group:${groupId}`,
            fields: {
                students(existingUserRefs, {readField}) {
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
            onDelete={() => deleteStudent({variables: {userId: userId, groupId: groupId}})}
        />
    );
}


EditGroupStudentElementCont.fragments = {
    USER_DETAILS_FRAGMENT: USER_DETAILS_FRAGMENT,
}

EditGroupStudentElementCont.propTypes = {
    userId: PropTypes.oneOfType([number, string]).isRequired,
    groupId: PropTypes.oneOfType([number, string]).isRequired,
    selectedUserId: PropTypes.oneOfType([number, string]),
}