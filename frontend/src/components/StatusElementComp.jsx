import React from "react";


export default function StatusElementComp(props) {
    return (
        <div
            className={`btn-${calculateStatusColor(props.studentStatus)} btn rounded-pill disabled w-100 font-weight-bold`}
        >
            {props.studentStatus.status === 'NOT_STARTED' ? 'NOT STARTED' : calculateStatusTime(props.studentStatus)}
        </div>
    );
}

const isStatusInactive = (statusChangedTime) => {
    const now = new Date();
    const fiveMins = 1000 * 60 * 5;
    return (now.getTime() - new Date(statusChangedTime).getTime()) > fiveMins;
}

const calculateStatusColor = (studentStatus) => {
    if (studentStatus.status === 'NOT_STARTED') {
        return 'info';
    } else if (isStatusInactive(studentStatus.statusChangedTime)) {
        return 'warning';
    } else if (studentStatus.status === 'IN_PROGRESS') {
        return 'success';
    } else if (studentStatus.status === 'PROBLEM') {
        return 'danger';
    } else {
        return 'secondary'
    }
}

const calculateStatusTime = (studentStatus) => {
    const now = new Date();
    const diff = now.getTime() - new Date(studentStatus.statusChangedTime).getTime();
    if (diff < 1000 * 60 * 60) {
        return `${Math.round(diff / (1000 * 60))} mins ago`
    } else if (diff < 1000 * 60 * 60 * 24) {
        return `${Math.round(diff / (1000 * 60 * 60))} hours ago`
    } else {
        return `${Math.round(diff / (1000 * 60 * 60 * 24))} days ago`
    }
}