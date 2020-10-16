import gql from "graphql-tag";
import client from "../../ApolloClient";
import React from "react";
import {useRouteMatch} from "react-router-dom";
import {useMutation} from "@apollo/client";
import toast from "toasted-notes";
import TeacherGroupElementComp from "../../components/teacher-group-page/TeacherGroupElementComp";
import PropTypes, {number, string} from "prop-types";

const TEST_DETAILS_FRAGMENT = gql`
    fragment TestDetails on Test {
        id
        name
        description
        active
    }`;


const EDIT_TEST_STATUS_MUTATION = gql`
    mutation EditTestStatus($testId: ID!, $active: Boolean!) {
        editTestStatus(testId: $testId, active: $active)  {
            ...TestDetails
        }
    }
${TEST_DETAILS_FRAGMENT}`;


export default function TeacherGroupElementCont({testId, selectedTestId}) {
    let match = useRouteMatch();

    const [changeTestStatus] = useMutation(EDIT_TEST_STATUS_MUTATION, {
        onCompleted: (data) => toast.notify(`Test status changed to: ${data.editTestStatus.active ? 'Active' : 'Inactive'}`),
        onError: () => toast.notify(`Error :(`),
    });

    const test = client.readFragment({
        id: `Test:${testId}`,
        fragment: TEST_DETAILS_FRAGMENT,
    });

    return (
        <TeacherGroupElementComp
            isSelected={selectedTestId === test.id}
            test={test}
            onChangeStatus={() => changeTestStatus({variables: {testId: testId, active: !test.active}})}
            statusPath={`${match.url}/test/${test.id}`}
            editPath={`${match.url}/test/${test.id}/edit`}
        />);

}

TeacherGroupElementCont.propTypes = {
    testId: PropTypes.oneOfType([number, string]).isRequired,
    selectedTestId: PropTypes.oneOfType([number, string]),
}

TeacherGroupElementCont.fragments = {
    TEST_DETAILS_FRAGMENT: TEST_DETAILS_FRAGMENT,
};