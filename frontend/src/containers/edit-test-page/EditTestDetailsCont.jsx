import React from 'react'
import gql from "graphql-tag";
import client from "../../ApolloClient";
import toast from "toasted-notes";
import {useMutation} from "@apollo/client";
import FormComp from "../../components/common/FormComp";
import {useHistory} from "react-router-dom";
import FormCont from "../common/FormCont";
import PropTypes, {number, resetWarningCache, string} from "prop-types";

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
            ...TestDetails
        }
    }
${TEST_DETAILS_FRAGMENT}`;

export default function EditTestDetailsCont({testId}) {
    const test = client.readFragment({
        id: `Test:${testId}`,
        fragment: TEST_DETAILS_FRAGMENT,
    });

    const [editTest] = useMutation(EDIT_TEST_MUTATION, {
        onCompleted: () => toast.notify(`Test details edited successfully`),
        onError: () => toast.notify(`Error`),
    });

    if(!test) {
        return (<div/>);
    }

    return (
        <FormCont
            initialName={test.name}
            initialDescription={test.description}
            onSubmit={values => {
                editTest({
                    variables: {
                        input: {
                            description: values.description,
                            name: values.name
                        },
                        testId: testId,
                    },
                })
            }}
        />
    );
}


EditTestDetailsCont.fragments = {
    TEST_DETAILS_FRAGMENT: TEST_DETAILS_FRAGMENT,
}

EditTestDetailsCont.propTypes = {
    testId: PropTypes.oneOfType([number, string]).isRequired,
}
