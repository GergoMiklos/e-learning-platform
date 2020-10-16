import gql from "graphql-tag";
import client from "../../ApolloClient";
import React from "react";
import EditGroupUserElementComp from "../../components/edit-group-page/EditGroupUserElementComp";
import PropTypes, {number, string} from "prop-types";

const USER_DETAILS_FRAGMENT = gql`
    fragment UserDetails on User {
        id
        name
        code
    }`;

export default function EditGroupUserElementCont({userId, selectedUserId, onDelete}) {
    const user = client.readFragment({
        id: `User:${userId}`,
        fragment: USER_DETAILS_FRAGMENT,
    });

    if(!user) {
        return (<div/>);
    }

    return (
        <EditGroupUserElementComp
            user={user}
            isSelected={selectedUserId === userId}
            onDelete={onDelete}
        />
    );
}

EditGroupUserElementCont.fragments = {
    USER_DETAILS_FRAGMENT: USER_DETAILS_FRAGMENT,
}

EditGroupUserElementCont.propTypes = {
    userId: PropTypes.oneOfType([number, string]).isRequired,
    selectedUserId: PropTypes.oneOfType([number, string]),
    onDelete: PropTypes.func.isRequired,
}