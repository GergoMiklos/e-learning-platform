import gql from 'graphql-tag';
import React from 'react';
import PropTypes from 'prop-types';

const USER_DETAILS_FRAGMENT = gql`
  fragment UserDetails on User {
    id
    name
    code
  }
`;

export default function EditGroupUserElementComp({
  user,
  isSelected,
  onDelete,
}) {
  return (
    <article className="d-flex justify-content-between">
      <strong>{user.name}</strong>
      <div>{user.code}</div>

      {isSelected && (
        <button
          className="btn btn-outline-warning btn-sm"
          onClick={() => onDelete()}
        >
          Delete
        </button>
      )}
    </article>
  );
}

EditGroupUserElementComp.fragments = {
  USER_DETAILS_FRAGMENT,
};

EditGroupUserElementComp.propTypes = {
  user: PropTypes.object.isRequired,
  isSelected: PropTypes.bool.isRequired,
  onDelete: PropTypes.func.isRequired,
};
