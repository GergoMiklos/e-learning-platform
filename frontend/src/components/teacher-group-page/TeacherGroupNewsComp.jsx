import React from 'react';
import PropTypes from 'prop-types';

export default function TeacherGroupNewsComp({
  newsText,
  changeDisabled,
  onNewsTextChange,
  onChangeNews,
}) {
  return (
    <section className="input-group mb-3">
      <div className="input-group-prepend">
        <span className="input-group-text">Change news</span>
      </div>
      <textarea
        value={newsText}
        className="form-control"
        placeholder="News"
        onChange={(event) => onNewsTextChange(event.target.value)}
      />
      <div className="input-group-append">
        <button
          className="btn btn-outline-primary"
          onClick={() => onChangeNews()}
          disabled={changeDisabled}
        >
          Change
        </button>
      </div>
    </section>
  );
}

TeacherGroupNewsComp.propTypes = {
  newsText: PropTypes.string.isRequired,
  changeDisabled: PropTypes.bool.isRequired,
  onNewsTextChange: PropTypes.func.isRequired,
  onChangeNews: PropTypes.func.isRequired,
};
