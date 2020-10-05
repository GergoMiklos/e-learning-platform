import React, {useState} from 'react'
import gql from "graphql-tag";
import AuthenticationService from "../AuthenticationService";
import NewGroupDialogComp from "./NewGroupDialogComp";
import GroupListElementComp from "./GroupListElementComp";
import {useQuery} from "@apollo/client";


const TEACHER_GROUPS_QUERY = gql`
    query User($userId: ID!) {
        user(userId: $userId) {
            id
            name
            code
            teacherGroups {
                ...GroupDetials
            }
        }
    }
${GroupListElementComp.fragments.GROUP_DETAILS_FRAGMENT}`;

export default function TeacherPageComp(props) {
    //todo useeffect a useridra?
    const userId = AuthenticationService.getUserId();
    const [showNewGroupDialog, setShowNewGroupDialog] = useState(false);
    const {loading, error, data} = useQuery(
        TEACHER_GROUPS_QUERY, {
            variables: {userId: userId},
        });

    if (!data) {
        return (<div/>);
    }

    return (
        <div className="container">
            <div className="row bg-warning text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">{data.user.name}</h1>
                <h1 className="col-auto rounded-pill bg-primary px-3">{data.user.code}</h1>
            </div>

            <div className="row rounded shadow bg-light my-3 p-3 d-flex justify-content-between">
                <h1>Teacher Groups</h1>
                <button className="btn btn-primary" onClick={() => setShowNewGroupDialog(true)}>
                    New
                </button>
            </div>

            <NewGroupDialogComp
                show={showNewGroupDialog}
                onHide={() => setShowNewGroupDialog(false)}
            />

            {data.user.teacherGroups &&
            <div className="row my-3">
                <ul className="col-12 list-group">
                    {data.user.teacherGroups.map(group =>
                        <li
                            className="list-group-item list-group-item-action"
                            onClick={() => props.history.push(`/teach/group/${group.id}`)}
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
