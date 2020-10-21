import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { formatShortDate } from '../../utils/date-utils';

export default function GroupListElementComp({
  group,
  onClickPath,
  isNewsFresh,
}) {
  return (
    <Link to={onClickPath}>
      <strong className="mx-2 text-dark">{group.name}</strong>

      {isNewsFresh && (
        <span className="badge badge-pill bg-warning text-light mx-2 px-2 py-1">
          {formatShortDate(group.newsChangedDate)}
        </span>
      )}
    </Link>
  );
}

GroupListElementComp.propTypes = {
  group: PropTypes.object.isRequired,
  onClickPath: PropTypes.string.isRequired,
  isNewsFresh: PropTypes.bool.isRequired,
};
