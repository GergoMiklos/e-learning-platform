import React from "react";
import StatusElementComp from "../../components/common/StatusElementComp";
import PropTypes, {number, string} from "prop-types";
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
    const now = new Date();
    const fiveMins = 1000 * 60 * 5;
    return (now.getTime() - statusChangedTime.getTime()) > fiveMins;
}

StatusElementCont.propTypes = {
    status: PropTypes.oneOf(statuses).isRequired,
    changedTime: PropTypes.instanceOf(Date).isRequired,
}
