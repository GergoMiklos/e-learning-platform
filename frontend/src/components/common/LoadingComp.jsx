import React from 'react';
import { Spinner } from 'react-bootstrap';
import PropTypes from 'prop-types';

export default function LoadingComp({ text }) {
  return (
    <div className="middle container">
      <div className="row justify-content-center" data-testid="loading">
        <Spinner animation="border" variant="secondary" />
      </div>

      {text && (
        <div className="row justify-content-center my-1 text-secondary">
          <div>{text}</div>
        </div>
      )}
    </div>
  );
}

LoadingComp.propTypes = {
  text: PropTypes.string,
};
