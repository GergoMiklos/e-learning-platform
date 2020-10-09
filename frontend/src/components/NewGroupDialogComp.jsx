import React from 'react'
import gql from "graphql-tag";
import {Modal} from "react-bootstrap";
import {useMutation} from "@apollo/client";
import toast from "toasted-notes";
import NameDescFormComp from "./NameDescFormComp";
import AuthService from "../AuthService";
import {useHistory} from "react-router-dom";

//todo fragment
const CREATE_GROUP_MUTATION = gql`
    mutation CreateGroup($userId: ID!, $input: NameDescInput!) {
        createGroup(userId: $userId, input: $input) {
            name
        }
    }`;

export default function NewGroupDialogComp(props) {
    const [createGroup] = useMutation(CREATE_GROUP_MUTATION, {
        onCompleted: (data) => toast.notify(`Group created with name: ${data.createGroup.name}`),
        onError: (error) => toast.notify(`Error`),
        update: (cache, {data: {createGroup}}) => {
            cache.modify({
                id: `User:${AuthService.getUserId()}`,
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
            show={props.show}
            centered
            onHide={() => props.onHide()}
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
                                    userId: AuthService.getUserId(),
                                },
                            })
                            props.onHide();
                        }}
                    />
                </div>
        </Modal>
    );
}
