import gql from "graphql-tag";
import client from "../ApolloClient";
import React, {useState} from "react";
import {useHistory, useRouteMatch} from "react-router-dom";
import StatusElementComp from "./StatusElementComp";
import GroupListElementComp from "./GroupListElementComp";
import AuthService from "../AuthService";
import {useMutation, useQuery} from "@apollo/client";
import toast from "toasted-notes";

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


export default function TeacherGroupElementComp({testId, selectedTestId, ...props}) {
    const [changeTestStatus] = useMutation(EDIT_TEST_STATUS_MUTATION, {
        onCompleted: (data) => toast.notify(`Test status changed to: ${data.editTestStatus.active ? 'Active' : 'Inactive'}`),
        onError: () => toast.notify(`Error :(`),
    });

    const test = client.readFragment({
        id: `Test:${testId}`,
        fragment: TEST_DETAILS_FRAGMENT,
    });

    if (selectedTestId === test.id) {
        return (
            <SelectedElementComp
                test={test}
                onChangeStatus={() => changeTestStatus({variables: { testId: testId, active: !test.active}})}
            />);
    } else {
        return (<ElementComp test={test}/>);
    }
}

function SelectedElementComp({test, onChangeStatus, ...props}) {
    let history = useHistory();
    let match = useRouteMatch();

    return (
        <div>
            <div className="d-flex justify-content-between">
                <div className="font-weight-bold">
                    {test.name}
                </div>

                <span>
                    {test.active &&
                    <button
                        className="btn btn-primary btn-sm"
                        onClick={() => history.push(`${match.url}/test/${test.id}`)}
                    >
                        Statuses
                    </button>
                    }
                    <button
                        className="btn btn-outline-warning btn-sm"
                        onClick={() => history.push(`${match.url}/test/${test.id}/edit`)}
                    >
                        Edit
                    </button>
                    <button
                        className="btn btn-secondary btn-sm"
                        onClick={() => onChangeStatus()}
                    >
                        {test.active ? 'Inactivate' : 'Activate'}
                    </button>
                </span>
            </div>

            <div className='font-weight-light'>
                {test.description}
            </div>
        </div>
    );
}

function ElementComp({test, ...props}) {
    return (
        <div className="d-flex justify-content-between">
            <div className="font-weight-bold">
                {test.name}
            </div>

            {!test.active &&
            <div className="badge badge-pill bg-secondary mx-2 px-2 py-2">
                INACTIVE
            </div>
            }
        </div>
    );
}


TeacherGroupElementComp.fragments = {
    TEST_DETAILS_FRAGMENT: TEST_DETAILS_FRAGMENT,
};