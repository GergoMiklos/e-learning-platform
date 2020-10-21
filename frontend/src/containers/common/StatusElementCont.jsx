import React from 'react';
import PropTypes from 'prop-types';
import StatusElementComp from '../../components/common/StatusElementComp';
import { statuses } from '../../constants';

export default function StatusElementCont({
  status,
  changedTime,
  solutionTime,
}) {
  return (
    <StatusElementComp
      status={status}
      changedTime={changedTime}
      isInactive={isStatusInactive(solutionTime)}
    />
  );
}

const isStatusInactive = (statusChangedTime) => {
  const fiveMins = 1000 * 60 * 5;
  return Date.now() - statusChangedTime.getTime() > fiveMins;
};

StatusElementCont.propTypes = {
  status: PropTypes.oneOf(statuses).isRequired,
  changedTime: PropTypes.instanceOf(Date).isRequired,
  solutionTime: PropTypes.instanceOf(Date).isRequired,
};
