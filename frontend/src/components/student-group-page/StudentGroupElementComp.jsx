import React from 'react';
import { Link } from 'react-router-dom';
import { Collapse } from 'react-bootstrap';
import PropTypes from 'prop-types';

export default function StudentGroupElementComp({
  test,
  isSelected,
  onClickPath,
}) {
  return (
    <article>
      <div className="d-flex justify-content-between">
        <strong>{test.name}</strong>

        {isSelected && (
          <Link to={onClickPath} className="btn btn-primary btn-sm">
            Start
          </Link>
        )}
      </div>

      <Collapse in={isSelected}>
        <div className="font-weight-light">{test.description}</div>
      </Collapse>
    </article>
  );
}

StudentGroupElementComp.propTypes = {
  test: PropTypes.object.isRequired,
  isSelected: PropTypes.bool.isRequired,
  onClickPath: PropTypes.string.isRequired,
};
