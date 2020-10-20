import {act} from '@testing-library/react'
import PropTypes from "prop-types";

export async function waitNextTick(timeout = 0) {
    await act(async () => {
        await new Promise(resolve => setTimeout(resolve, timeout));
    });
}

waitNextTick.propTypes = {
    timeout: PropTypes.number,
}


