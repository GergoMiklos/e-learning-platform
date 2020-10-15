import React from 'react'
import client from "../ApolloClient";
import gql from 'graphql-tag';
import {useHistory, useRouteMatch} from "react-router-dom";


//todo itt lehtne loadolni egy fájlból is, és úgy beállítani
const GROUP_DETAILS_FRAGMENT = gql`
        fragment GroupDetails on Group {
            id
            name
            news
            newsChangedDate
        }`;



export default function GroupListElementComp(props) {
    let history = useHistory();
    let match = useRouteMatch();
    //todo error handling (null/error) / useQuery cache-first?
    const group = client.readFragment({
        id: `Group:${props.groupId}`,
        fragment: GROUP_DETAILS_FRAGMENT,
    });

    return (
        <div onClick={() => history.push(`${match.url}/group/${group.id}`)}>
            <strong className="mx-2">
                {group.name}
            </strong>

            {group.news && isDateFresh(new Date(group.newsChangedDate)) &&
            <span className="badge badge-pill bg-warning text-light mx-2 px-2 py-1">
                {formatShortDate(new Date(group.newsChangedDate))}
            </span>
            }
        </div>
    );
}

GroupListElementComp.fragments = {
    GROUP_DETAILS_FRAGMENT: GROUP_DETAILS_FRAGMENT,
};


const formatShortDate = (date) => {
    const options = {
        weekday: 'long',
        hour: 'numeric',
        minute: 'numeric'
    }
    return date.toLocaleString(/*navigator.language*/ 'en', options); //todo
}

const isDateFresh = (date) => {
    const now = new Date();
    const sixDays = 1000 * 60 * 60 * 24 * 6;
    return (now.getTime() - date.getTime()) < sixDays;
}

