import React, {useState} from 'react'
import gql from 'graphql-tag';
import toast from 'toasted-notes';
import {useMutation, useQuery} from "@apollo/client";
import GroupListElementComp from "./GroupListElementComp";
import AuthService from "../AuthService";
import {useHistory} from "react-router-dom";

//todo itt lehetne loadolni egy fájlból is, és úgy beállítani
const STUDENT_GROUPS_QUERY = gql`
    query getUser($userId: ID!) {
        user(userId: $userId) {
            id
            name
            code
            studentGroups {
                ...GroupDetials
            }
        }
    }
${GroupListElementComp.fragments.GROUP_DETAILS_FRAGMENT}`;

const JOIN_GROUP_MUTATION = gql`
    mutation AddStudentToGroupFromCode($userId: ID!, $groupCode: String!) {
        addStudentToGroupFromCode(userId: $userId, groupCode: $groupCode)  {
            ...GroupDetials
        }
    }
${GroupListElementComp.fragments.GROUP_DETAILS_FRAGMENT}`;


export default function StudentPageComp(props) {
    //todo useeffect a useridra?
    const userId = AuthService.getUserId();
    const [joinGroupCode, setJoinGroupCode] = useState('');
    const {loading, error, data} = useQuery(
        STUDENT_GROUPS_QUERY, {
            variables: {userId: userId},
        });
    const [joinGroup] = useMutation(JOIN_GROUP_MUTATION, {
        onCompleted: (data) => toast.notify(`Joined to group: ${data.addStudentToGroupFromCode.name}`),
        onError: () => toast.notify(`No group with code: ${joinGroupCode}`),
        update: (cache, {data: {addStudentToGroupFromCode}}) => {
            cache.modify({
                id: `User:${data.user.id}`,
                fields: {
                    //Todo melyik jobb? A sorrendet tönkre teszi a második megoldás
                    // studentGroups(existingGroupRefs, {INVALIDATE}) {
                    //     return INVALIDATE;
                    // },
                    studentGroups(existingGroupRefs = [], {readField}) {
                        const newGroupRef = cache.writeFragment({
                            data: addStudentToGroupFromCode,
                            fragment: GroupListElementComp.fragments.GROUP_DETAILS_FRAGMENT,
                        });

                        if (existingGroupRefs.some(ref => readField('id', ref) === newGroupRef.id)) {
                            return existingGroupRefs;
                        }
                        return [newGroupRef, ...existingGroupRefs];
                    },
                },
            });
        },
    });

    if (!data || !data.user) {
        return (<div/>);
    }

    return (
        <div className="container">
            <div className="row bg-primary text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">{data.user.name}</h1>
                <h1 className="col-auto rounded-pill bg-warning px-3">{data.user.code}</h1>
            </div>

            <div className="row rounded shadow bg-light my-3 p-3">
                <h1 className="col-12">Student Groups</h1>
            </div>

            <div className="row input-group mb-3">
                <div className="input-group-prepend">
                    <span className="input-group-text">Join group</span>
                </div>
                <input
                    type="text" className="form-control" placeholder="GROUP CODE"
                    value={joinGroupCode}
                    onChange={event => setJoinGroupCode(event.target.value.toUpperCase().slice(0, 8))}
                />
                <div className="input-group-append">
                    <button
                        className="btn btn-outline-primary"
                        onClick={() => joinGroup({variables: {userId: data.user.id, groupCode: joinGroupCode}})}
                        disabled={!joinGroupCode || !joinGroupCode.trim().length}
                    >
                        Join
                    </button>
                </div>
            </div>

            {data.user.studentGroups &&
            <div className="row my-3">
                <ul className="col-12 list-group">
                    {data.user.studentGroups.map(group =>
                        <li
                            className="list-group-item list-group-item-action"
                            onClick={() => props.history.push(`/learn/group/${group.id}`)}
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


