import React from 'react'
import gql from "graphql-tag";
import EditGroupDetailsComp from "./EditGroupDetailsComp";
import {useQuery} from "@apollo/client";
import EditGroupTeacherListComp from "./EditGroupTeacherListComp";
import EditGroupStudentListComp from "./EditGroupStudentListComp";
import EditGroupUserElementComp from "./EditGroupUserElementComp";
import {useHistory} from "react-router-dom";
import LoadingComp from "./LoadingComp";

//TODO itt írd ki azt is ami nekem kell, pl id-k!
const EDIT_GROUP_QUERY = gql`
    query getGroup($groupId: ID!) {
        group(groupId: $groupId) {
            id
            ...GroupDetails
            teachers {
                id
                ...UserDetails
            }
            students {
                id
                ...UserDetails
            }
        }
    }
    ${EditGroupUserElementComp.fragments.USER_DETAILS_FRAGMENT}
${EditGroupDetailsComp.fragments.GROUP_DETAILS_FRAGMENT}`;

export default function EditGroupPageComp(props) {
    const {loading, error, data} = useQuery(EDIT_GROUP_QUERY, {
        variables: {groupId: props.match.params.groupid},
    });

    if (!data?.group) {
        return (<LoadingComp/>);
    }

    return (
        <div className="container">
            <button className="row btn btn-secondary mt-1"
                    onClick={() => props.history.goBack()}>
                Back
            </button>

            <div className="row bg-primary text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">Edit Group</h1>
            </div>

            <EditGroupDetailsComp
                className="row"
                groupId={data.group.id}
            />

            <EditGroupTeacherListComp
                groupId={props.match.params.groupid}
                teachers={data.group.teachers}
            />

            <EditGroupStudentListComp
                groupId={props.match.params.groupid}
                students={data.group.students}
            />
        </div>
    );
}
