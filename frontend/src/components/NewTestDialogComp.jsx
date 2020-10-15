import React from 'react'
import gql from "graphql-tag";
import {Modal} from "react-bootstrap";
import toast from "toasted-notes";
import {useMutation} from "@apollo/client";
import NameDescFormComp from "./NameDescFormComp";
import {useHistory} from "react-router-dom";

const CREATE_TEST_MUTATION = gql`
    mutation CreateTest($groupId: ID!, $input: NameDescInput!) {
        createTest(groupId: $groupId, input: $input) {
            name
        }
    }`;

export default function NewTestDialogComp(props) {
    let history = useHistory();

    const [createTest] = useMutation(CREATE_TEST_MUTATION, {
        onCompleted: (data) => {
            toast.notify(`Test created with name: ${data.createTest.name}`);
            history.goBack();
        },
        onError: () => toast.notify(`Error :(`),
        update: (cache, {data: {createTest}}) => {
            cache.modify({
                id: `Group:${props.groupId}`, //todo ezt honnan?
                fields: {
                    //Todo melyik jobb?
                    tests(existingTestRefs, {INVALIDATE}) {
                        return INVALIDATE;
                    },

                    //     tests(existingTestRefs = [], { readField }) {
                    //         const newTestRef = cache.writeFragment({
                    //             data: createTest,
                    //             fragment: gql`
                    //                 fragment addTest on Test {
                    //                     id
                    //                     name
                    //                     description
                    //                 }`
                    //         });
                    //
                    //         if (existingTestRefs.some(ref => readField('id', ref) === newTestRef.id)) {
                    //             return existingTestRefs;
                    //         }
                    //
                    //         return [...existingTestRefs, newTestRef];
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
                    <h1>New Test</h1>
                </div>
                <NameDescFormComp
                    initial
                    onSubmit={values => {
                        createTest({
                            variables: {
                                groupId: props.groupId,
                                input: {description: values.description, name: values.name},
                            }
                        });
                    }}
                />
            </div>
        </Modal>
    );
}