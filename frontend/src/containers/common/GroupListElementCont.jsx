import React from 'react'
import client from "../../ApolloClient";
import gql from 'graphql-tag';
import {useRouteMatch} from "react-router-dom";
import GroupListElementComp from "../../components/common/GroupListElementComp";
import PropTypes, {number, string} from "prop-types";

//todo itt lehtne loadolni egy fájlból is, és úgy beállítani
const GROUP_DETAILS_FRAGMENT = gql`
    fragment GroupDetails on Group {
        id
        name
        news
        newsChangedDate
    }`;

export default function GroupListElementCont({groupId}) {
    let match = useRouteMatch();

    const group = client.readFragment({
        id: `Group:${groupId}`,
        fragment: GROUP_DETAILS_FRAGMENT,
    });

    if (!group) {
        return (<div/>);
    }

    return (
        <GroupListElementComp
            group={group}
            onClickPath={`${match.url}/group/${groupId}`}
            isNewsFresh={isDateFresh(group.newsChangedDate)}
        />
    );
}

GroupListElementCont.fragments = {
    GROUP_DETAILS_FRAGMENT: GROUP_DETAILS_FRAGMENT,
};

GroupListElementCont.propTypes = {
    groupId: PropTypes.oneOfType([number, string]).isRequired,
}


const isDateFresh = (date) => {
    const now = new Date();
    const sixDays = 1000 * 60 * 60 * 24 * 6;
    return (now.getTime() - date.getTime()) < sixDays;
}


