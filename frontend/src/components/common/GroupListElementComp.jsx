import React from 'react';
import {Link} from "react-router-dom";
import PropTypes from "prop-types";

export default function GroupListElementComp({group, onClickPath, isNewsFresh}) {

    return (
        <Link to={onClickPath}>
            <strong className="mx-2 text-dark">
                {group.name}
            </strong>

            {isNewsFresh &&
            <span className="badge badge-pill bg-warning text-light mx-2 px-2 py-1">
                {formatShortDate(group.newsChangedDate)}
            </span>
            }
        </Link>
    );
}

GroupListElementComp.propTypes = {
    group: PropTypes.object.isRequired,
    onClickPath: PropTypes.string.isRequired,
    isNewsFresh: PropTypes.bool.isRequired,
}

const formatShortDate = (date) => {
    const options = {
        weekday: 'long',
        hour: 'numeric',
        minute: 'numeric'
    }
    return date.toLocaleString(/*navigator.language*/ 'en', options); //todo
}