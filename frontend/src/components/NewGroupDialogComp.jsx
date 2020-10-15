import React from 'react'
import gql from "graphql-tag";
import {Modal} from "react-bootstrap";
import {useMutation} from "@apollo/client";
import toast from "toasted-notes";
import NameDescFormComp from "./NameDescFormComp";
import AuthService, {useAuthentication} from "../AuthService";
import GroupListElementComp from "./GroupListElementComp";
import {useHistory} from "react-router-dom";

const CREATE_GROUP_MUTATION = gql`
    mutation CreateGroup($userId: ID!, $input: NameDescInput!) {
        createGroup(userId: $userId, input: $input){
            ...GroupDetails
        }
    }
${GroupListElementComp.fragments.GROUP_DETAILS_FRAGMENT}`;

export default function NewGroupDialogComp(props) {
    let history = useHistory();
    const {userId} = useAuthentication();

    const [createGroup] = useMutation(CREATE_GROUP_MUTATION, {
        onCompleted: (data) => {
            toast.notify(`Group created with name: ${data.createGroup.name}`);
            history.goBack();
        },
        onError: () => toast.notify(`Error`),
        update: (cache, {data: {createGroup}}) => {
            cache.modify({
                id: `User:${userId}`,
                fields: {
                    //Todo melyik jobb?
                    teacherGroups(existingGroupRefs, {INVALIDATE}) {
                        return INVALIDATE;
                    },

                    //     teacherGroup(existingGroupRefs = [], { readField }) {
                    //         console.log(user);
                    //         console.log(createGroup);
                    //         console.log(existingGroupRefs);
                    //
                    //         const newGroupRef = cache.writeFragment({
                    //             data: createGroup,
                    //             fragment: gql`
                    //                 fragment addGroup on Group {
                    //                     id
                    //                     name
                    //                     news
                    //                     newsChangedDate
                    //                 }`
                    //         });
                    //
                    //         if (existingGroupRefs.some(ref => readField('id', ref) === newGroupRef.id)) {
                    //             return existingGroupRefs;
                    //         }
                    //
                    //         return [...existingGroupRefs, newGroupRef];
                    //     },
                },
            });
        },
    });

    return (
        <Modal
            centered
            onHide={() => history.goBack()}
            show={true}
        >
            <div className="container">
                <div className="row bg-primary text-light shadow p-3">
                    <h1>New Group</h1>
                </div>
                    <NameDescFormComp
                        onSubmit={values => {
                            createGroup({
                                variables: {
                                    input: {
                                        description: values.description,
                                        name: values.name
                                    },
                                    userId: userId,
                                },
                            })
                        }}
                    />
                </div>
        </Modal>
    );
}
