import React, {useState} from 'react'
import gql from 'graphql-tag';
import toast from 'toasted-notes';
import {useMutation, useQuery} from "@apollo/client";
import StudentPageComp from "../../components/student-page/StudentPageComp";
import {useAuthentication} from "../../AuthService";
import LoadingComp from "../../components/common/LoadingComp";
import GroupListElementCont from "../common/GroupListElementCont";


const STUDENT_GROUPS_QUERY = gql`
    query getUser($userId: ID!) {
        user(userId: $userId) {
            id
            name
            code
            studentGroups {
                ...GroupDetails
            }
        }
    }
${GroupListElementCont.fragments.GROUP_DETAILS_FRAGMENT}`;

const JOIN_GROUP_MUTATION = gql`
    mutation AddStudentToGroupFromCode($userId: ID!, $groupCode: String!) {
        addStudentToGroupFromCode(userId: $userId, groupCode: $groupCode)  {
            ...GroupDetails
        }
    }
${GroupListElementCont.fragments.GROUP_DETAILS_FRAGMENT}`;


export default function StudentPageCont() {
    const {userId} = useAuthentication();
    const [joinGroupCode, setJoinGroupCode] = useState('');

    const {data} = useQuery(
        STUDENT_GROUPS_QUERY, {
            variables: {userId: userId},
            pollInterval: 60 * 1000,
        });

    const [joinGroup] = useMutation(JOIN_GROUP_MUTATION, {
        onCompleted: (data) => toast.notify(`Joined to group: ${data.addStudentToGroupFromCode.name}`),
        onError: () => toast.notify(`No group with code: ${joinGroupCode}`),
        update: (cache, {data: {addStudentToGroupFromCode}}) => {
            cache.modify({
                id: `User:${userId}`,
                fields: {
                    studentGroups(existingGroupRefs, {INVALIDATE}) {
                        return INVALIDATE;
                    },
                },
            });
        },
    });


    if (!data?.user) {
        return (<LoadingComp/>);
    }

    return (
        <StudentPageComp
            user={data.user}
            joinCode={joinGroupCode}
            onJoinCodeChange={value => setJoinGroupCode(value.toUpperCase().slice(0, 8))}
            joinDisabled={!joinGroupCode || !joinGroupCode.trim().length}
            onJoinGroup={() => joinGroup({variables: {userId: data.user.id, groupCode: joinGroupCode}})}
        />
    );
}


