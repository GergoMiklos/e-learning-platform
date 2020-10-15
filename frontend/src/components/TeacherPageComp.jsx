import React, {useState} from 'react'
import gql from "graphql-tag";
import NewGroupDialogComp from "./NewGroupDialogComp";
import GroupListElementComp from "./GroupListElementComp";
import {useQuery} from "@apollo/client";
import AuthService, {useAuthentication} from "../AuthService";
import {Link, Route, useHistory} from "react-router-dom";
import LoadingComp from "./LoadingComp";
import NewTestDialogComp from "./NewTestDialogComp";


const TEACHER_GROUPS_QUERY = gql`
    query User($userId: ID!) {
        user(userId: $userId) {
            id
            name
            code
            teacherGroups {
                ...GroupDetails
            }
        }
    }
${GroupListElementComp.fragments.GROUP_DETAILS_FRAGMENT}`;

export default function TeacherPageComp(props) {
    let history = useHistory();
    const {userId} = useAuthentication();

    const {loading, error, data} = useQuery(
        TEACHER_GROUPS_QUERY, {
            variables: {userId: userId},
        });

    if (!data?.user) {
        return (<LoadingComp/>);
    }

    return (
        <div className="container">
            <div className="row bg-warning text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">{data.user.name}</h1>
                <h1 className="col-auto rounded-pill bg-primary px-3">{data.user.code}</h1>
            </div>

            <div className="row rounded shadow bg-light my-3 p-3 justify-content-between">
                <h1>Teacher Groups</h1>
                <Link to={`${props.match.url}/new`}>
                    <button className="btn btn-lg btn-primary">
                        {'New'}
                    </button>
                </Link>
            </div>

            <Route path={`${props.match.url}/new`} render={(props) =>
                (<NewGroupDialogComp {...props} />)
            }/>

            {data.user.teacherGroups?.length === 0 ? "No Groups" :
            <div className="row my-3">
                <ul className="col-12 list-group">
                    {data.user.teacherGroups.map(group =>
                        <li
                            className="list-group-item list-group-item-action"
                            key={group.id}
                        >
                            <GroupListElementComp groupId={group.id}/>
                        </li>
                    )}
                </ul>
            </div>
            }
        </div>
    );
}
