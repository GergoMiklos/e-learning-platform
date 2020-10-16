import React from 'react'
import gql from "graphql-tag";
import client from "../../ApolloClient";
import {useRouteMatch} from "react-router-dom";
import StudentGroupElementComp from "../../components/student-group-page/StudentGroupElementComp";
import PropTypes, {number, string} from "prop-types";

const TEST_DETAILS_FRAGMENT = gql`
    fragment TestDetials on Test {
        id
        name
        description
    }`;

export default function StudentGroupElementCont({testId, selectedTestId}) {
    let match = useRouteMatch();

    const test = client.readFragment({
        id: `Test:${testId}`,
        fragment: TEST_DETAILS_FRAGMENT,
    });

    if(!test) {
        return (<div/>);
    }

    return (
        <StudentGroupElementComp
            test={test}
            isSelected={selectedTestId === test.id}
            onClickPath={`${match.url}/test/${test.id}`}
        />
    );
}

StudentGroupElementCont.fragments = {
    TEST_DETAILS_FRAGMENT: TEST_DETAILS_FRAGMENT,
};

StudentGroupElementCont.propTypes = {
    testId: PropTypes.oneOfType([number, string]).isRequired,
    selectedTestId: PropTypes.oneOfType([number, string]),
}

