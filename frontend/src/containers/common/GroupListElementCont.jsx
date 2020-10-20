import React from 'react'
import client from "../../ApolloClient";
import gql from 'graphql-tag';
import {useRouteMatch} from "react-router-dom";
import GroupListElementComp from "../../components/common/GroupListElementComp";
import PropTypes, {number, string} from "prop-types";
import {isDateInAWeek} from "../../utils/date-utils";


const GROUP_DETAILS_FRAGMENT = gql`
    fragment GroupDetails on Group {
        id
        name
        news
        newsChangedDate
    }`;

export default function GroupListElementCont({group}) {
    let match = useRouteMatch();

    // const group2 = client.readFragment({
    //     id: `Group:${group.id}`,
    //     fragment: GROUP_DETAILS_FRAGMENT,
    // });

    if (!group) {
        return (<div/>);
    }

    return (
        <GroupListElementComp
            group={group}
            onClickPath={`${match.url}/group/${group.id}`}
            isNewsFresh={isDateInAWeek(group.newsChangedDate)}
        />
    );
}

GroupListElementCont.fragments = {
    GROUP_DETAILS_FRAGMENT: GROUP_DETAILS_FRAGMENT,
};

GroupListElementCont.propTypes = {
    //groupId: PropTypes.oneOfType([number, string]).isRequired,
    group: PropTypes.object.isRequired,
}



