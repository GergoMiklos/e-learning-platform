import React from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import {useHistory} from "react-router-dom";

const TEST_DETAILS_FRAGMENT = gql`
    fragment TestDetials on Test {
        id
        name
        description
    }`;

export default function StudentGroupElementComp(props) {
    //TODO csak props, readFragment vagy cache-first useQuery?
    const test = client.readFragment({
        id: `Test:${props.testId}`,
        fragment: TEST_DETAILS_FRAGMENT,
    });

    return (
        <div>
            <div className="d-flex justify-content-between">
                <strong>{test.name}</strong>

                {(props.selectedTestId === test.id) &&
                <button className="btn btn-primary btn-sm" onClick={() => props.onStart()}>
                    Start
                </button>
                }
            </div>

            {(props.selectedTestId === test.id) &&
            <div className="font-weight-light">
                {test.description}
            </div>
            }
        </div>
    );
}

StudentGroupElementComp.fragments = {
    TEST_DETAILS_FRAGMENT: TEST_DETAILS_FRAGMENT,
};

