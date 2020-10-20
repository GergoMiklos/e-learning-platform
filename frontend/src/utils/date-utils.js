import PropTypes from "prop-types";

export const isDateInAWeek = (date) => {
    const sixDays = 1000 * 60 * 60 * 24 * 6;
    return (Date.now() - date?.getTime()) < sixDays;
}

export const formatShortDate = (date) => {
    const options = {
        weekday: 'long',
        hour: 'numeric',
        minute: 'numeric'
    }
    return date.toLocaleString(/*navigator.language*/ 'en', options); //todo
}

export const formatLongDate = (date) => {
    const options = {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric'
    };
    return date.toLocaleString(/*navigator.language*/ 'en', options); //todo
}

isDateInAWeek.propTypes = {
    date: PropTypes.instanceOf(Date).isRequired,
}

formatShortDate.propTypes = {
    date: PropTypes.instanceOf(Date).isRequired,
}

formatLongDate.propTypes = {
    date: PropTypes.instanceOf(Date).isRequired,
}