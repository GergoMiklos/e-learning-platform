import {act, render} from '@testing-library/react'
import PropTypes, {object} from "prop-types";
import {isDateInAWeek} from "./date-utils";
import {MockedProvider} from "@apollo/client/testing";
import {cache as clientCache} from "../ApolloClient";
import StudentPageCont from "../containers/student-page/StudentPageCont";
import {BrowserRouter} from "react-router-dom";
import React from "react";

function renderWithWrappers({WrappedComponent, requestMocks = [], route='/'}) {
    window.history.pushState({}, 'Test page', route)

    return render(
        <MockedProvider mocks={requestMocks} cache={clientCache}>
            <BrowserRouter>
                <WrappedComponent/>
            </BrowserRouter>
        </MockedProvider>
    );
}

export default renderWithWrappers;

renderWithWrappers.propTypes = {
    children: PropTypes.node.isRequired,
    requestMocks: PropTypes.arrayOf(object),
    route: PropTypes.string,
}

export async function waitNextTick(timeout = 0) {
    await act(async () => {
        await new Promise(resolve => setTimeout(resolve, timeout));
    });
}

waitNextTick.propTypes = {
    timeout: PropTypes.number,
}


