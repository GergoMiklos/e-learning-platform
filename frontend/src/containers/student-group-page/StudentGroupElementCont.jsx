import React from 'react'
import gql from "graphql-tag";
import {useRouteMatch} from "react-router-dom";
import StudentGroupElementComp from "../../components/student-group-page/StudentGroupElementComp";
import PropTypes, {number, string} from "prop-types";

const TEST_DETAILS_FRAGMENT = gql`
    fragment TestDetials on Test {
        id
        name
        description
    }`;

export default function StudentGroupElementCont({test, selectedTestId}) {
    let match = useRouteMatch();

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
    test: PropTypes.object.isRequired,
    selectedTestId: PropTypes.oneOfType([number, string]),
}

