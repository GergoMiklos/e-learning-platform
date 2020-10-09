import React from 'react'
import gql from "graphql-tag";
import client from "../ApolloClient";
import {useHistory, useRouteMatch} from "react-router-dom";

const TEST_DETAILS_FRAGMENT = gql`
    fragment TestDetials on Test {
        id
        name
        description
    }`;

export default function StudentGroupElementComp(props) {
    let history = useHistory();
    let match = useRouteMatch();
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
                <button
                    className="btn btn-primary btn-sm"
                    onClick={() => history.push(`${match.url}/test/${test.id}`)}
                >
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

