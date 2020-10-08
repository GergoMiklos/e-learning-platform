import React from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import toast from "toasted-notes";
import {useMutation} from "@apollo/client";
import NameDescFormComp from "./NameDescFormComp";
import {useHistory} from "react-router-dom";

//todo itt lehtne loadolni egy fájlból is, és úgy beállítani
const GROUP_DETAILS_FRAGMENT = gql`
    fragment GroupDetails on Group {
        id
        name
        description
    }`;

const EDIT_GROUP_MUTATION = gql`
    mutation EditGroup($groupId: ID!, $input: NameDescInput!) {
        editGroup(groupId: $groupId, input: $input) {
            ...GroupDetails
        }
    }
${GROUP_DETAILS_FRAGMENT}`;

export default function EditGroupDetailsComp(props) {
    //todo error handling (null/error) / useQuery cache-first?
    const group = client.readFragment({
        id: `Group:${props.groupId}`,
        fragment: GROUP_DETAILS_FRAGMENT,
    });

    const [editTest] = useMutation(EDIT_GROUP_MUTATION, {
        onCompleted: () => toast.notify(`Group details edited successfully`),
        onError: () => toast.notify(`Error`),
    });

    return (
        <NameDescFormComp
            initialName={group.name}
            initialDescription={group.description}
            onSubmit={values => {
                editTest({
                    variables: {
                        input: {
                            description: values.description,
                            name: values.name
                        },
                        groupId: group.id,
                    },
                })
            }}
        />
    );
}


EditGroupDetailsComp.fragments = {
    GROUP_DETAILS_FRAGMENT: GROUP_DETAILS_FRAGMENT,
}