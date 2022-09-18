import React from 'react';
import PropTypes from 'prop-types';

export default function PercentageComp({
  correct = 0,
  all = 0,
  onlyPercentage = false,
}) {
  return (
    <div className="d-flex justify-content-center">
      <strong className="mx-1">
        {all === 0 ? 0 : Math.floor((correct / all) * 100)}%
      </strong>
      {!onlyPercentage && 
        <i>
          ({correct}/{all})
        </i>
      }
    </div>
  );
}

PercentageComp.propTypes = {
  all: PropTypes.number.isRequired,
  correct: PropTypes.number.isRequired,
  onlyPercentage: PropTypes.bool,
};
