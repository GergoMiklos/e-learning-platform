import gql from "graphql-tag";
import client from "../ApolloClient";
import React from "react";
import {useHistory} from "react-router-dom";

const USER_DETAILS_FRAGMENT = gql`
    fragment UserDetails on User {
        id
        name
        code
    }`;

export default function EditGroupUserElementComp(props) {
    //todo error handling (null/error) / useQuery cache-first?
    const user = client.readFragment({
        id: `User:${props.userId}`,
        fragment: USER_DETAILS_FRAGMENT,
    });
    return (
        <div className="d-flex justify-content-between">
            <strong>{user.name}</strong>
            <div>{user.code}</div>
            {props.selectedUserId === user.id &&
            <button className="btn btn-outline-warning btn-sm"
                    onClick={() => props.deleteUser()}>
                Delete
            </button>}
        </div>
    );
}

EditGroupUserElementComp.fragments = {
    USER_DETAILS_FRAGMENT: USER_DETAILS_FRAGMENT,
}