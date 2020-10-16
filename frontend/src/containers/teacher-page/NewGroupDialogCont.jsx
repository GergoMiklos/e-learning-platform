import React from 'react'
import gql from "graphql-tag";
import {useMutation} from "@apollo/client";
import toast from "toasted-notes";
import ModalFormComp from "../../components/common/ModalFormComp";
import {useAuthentication} from "../../AuthService";
import {useHistory} from "react-router-dom";
import GroupListElementCont from "../common/GroupListElementCont";

const CREATE_GROUP_MUTATION = gql`
    mutation CreateGroup($userId: ID!, $input: NameDescInput!) {
        createGroup(userId: $userId, input: $input){
            ...GroupDetails
        }
    }
${GroupListElementCont.fragments.GROUP_DETAILS_FRAGMENT}`;

export default function NewGroupDialogCont() {
    let history = useHistory();
    const {userId} = useAuthentication();

    const [createGroup] = useMutation(CREATE_GROUP_MUTATION, {
        onCompleted: (data) => toast.notify(`Group created with name: ${data.createGroup.name}`),
        onError: () => toast.notify(`Error :(`),
        update: (cache) => {
            cache.modify({
                id: `User:${userId}`,
                fields: {
                    teacherGroups(existingGroupRefs, {INVALIDATE}) {
                        return INVALIDATE;
                    },
                },
            });
        },
    });

    const onSubmit = (values) => {
        createGroup({
            variables: {
                input: {
                    description: values.description,
                    name: values.name
                },
                userId: userId,
            },
        }).then(() => history.goBack());
    };

    return (
        <ModalFormComp
            title={'New Group'}
            onHide={() => history.goBack()}
            onSubmit={onSubmit}
        />
    );
}
