import React from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import toast from "toasted-notes";
import {useMutation} from "@apollo/client";
import NameDescFormComp from "./NameDescFormComp";

//todo itt lehtne loadolni egy fájlból is, és úgy beállítani
const TEST_DETAILS_FRAGMENT = gql`
    fragment TestDetails on Test {
        id
        name
        description
    }`;

const EDIT_TEST_MUTATION = gql`
    mutation EditTest($testId: ID!, $input: NameDescInput!) {
        editTest(testId: $testId, input: $input) {
            ...TestDetials
        }
    }
${TEST_DETAILS_FRAGMENT}`;

export default function EditTestDetailsComp(props) {
    //todo error handling (null/error) / useQuery cache-first?
    const test = client.readFragment({
        id: `Test:${props.testId}`,
        fragment: TEST_DETAILS_FRAGMENT,
    });

    const [editTest] = useMutation(EDIT_TEST_MUTATION, {
        onCompleted: () => toast.notify(`Test details edited successfully`),
        onError: () => toast.notify(`Error`),
    });

    return (
        <NameDescFormComp
            initialName={test.name}
            initialDescription={test.description}
            onSubmit={values => {
                editTest({
                    variables: {
                        input: {
                            description: values.description,
                            name: values.name
                        },
                        testId: test.id,
                    },
                })
            }}
        />
    );
}


EditTestDetailsComp.fragments = {
    TEST_DETAILS_FRAGMENT: TEST_DETAILS_FRAGMENT,
}
