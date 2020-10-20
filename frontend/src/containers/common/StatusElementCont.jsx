import React from "react";
import StatusElementComp from "../../components/common/StatusElementComp";
import PropTypes from "prop-types";
import {statuses} from "../../constants";


export default function StatusElementCont({status, changedTime}) {
    return (
        <StatusElementComp
            status={status}
            changedTime={changedTime}
            isInactive={isStatusInactive(changedTime)}
        />
    );
}

const isStatusInactive = (statusChangedTime) => {
    const fiveMins = 1000 * 60 * 5;
    return (Date.now() - statusChangedTime.getTime()) > fiveMins;
}

StatusElementCont.propTypes = {
    status: PropTypes.oneOf(statuses).isRequired,
    changedTime: PropTypes.instanceOf(Date).isRequired,
}
