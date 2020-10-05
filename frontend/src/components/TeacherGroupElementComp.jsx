import gql from "graphql-tag";
import client from "../ApolloClient";
import React from "react";

const TEST_DETAILS_FRAGMENT = gql`
    fragment TestDetials on Test {
        id
        name
        description
    }`;

export default function TeacherGroupElementComp(props) {
    const test = client.readFragment({
        id: `Test:${props.testId}`,
        fragment: TEST_DETAILS_FRAGMENT,
    });

    return (
        <div>
            <div className="d-flex justify-content-between">
                <strong>{test.name}</strong>
                {props.selectedTestId === test.id &&
                <span>
                    <button className="btn btn-primary btn-sm" onClick={() => props.onStatuses()}>
                        Statuses
                    </button>
                    <button className="btn btn-outline-warning btn-sm" onClick={() => props.onEdit()}>
                        Edit
                    </button>
                </span>
                }
            </div>

            {props.selectedTestId === test.id &&
            <div className='font-weight-light'>
                {test.description}
            </div>
            }
        </div>
    );
}

TeacherGroupElementComp.fragments = {
    TEST_DETAILS_FRAGMENT: TEST_DETAILS_FRAGMENT,
};