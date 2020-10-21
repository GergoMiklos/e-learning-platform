import React from 'react';
import { useHistory } from 'react-router-dom';
import PropTypes from 'prop-types';

export default function NotFoundPageComp({ text }) {
  const history = useHistory();

  return (
    <div onClick={() => history.goBack()}>
      <div className="middle text-secondary">
        <div>{text || 'Page not found!'}</div>
      </div>
    </div>
  );
}

NotFoundPageComp.propTypes = {
  text: PropTypes.string,
};
