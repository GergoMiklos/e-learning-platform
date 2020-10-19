import React from 'react'
import gql from "graphql-tag";
import toast from 'toasted-notes';
import {useMutation, useQuery} from "@apollo/client";
import {useAuthentication} from "../../AuthService";
import {useHistory, useParams} from "react-router-dom";
import LoadingComp from "../../components/common/LoadingComp";
import StudentGroupPageComp from "../../components/student-group-page/StudentGroupPageComp";
import StudentGroupElementCont from "./StudentGroupElementCont";

const STUDENT_GROUP_QUERY = gql`
    query getGroup($groupId: ID!) {
        group(groupId: $groupId) {
            id
            name
            code
            description
            news
            newsChangedDate
            tests(active: true) {
                id
                ...TestDetials
            }
        }
    }
${StudentGroupElementCont.fragments.TEST_DETAILS_FRAGMENT}`;

const LEAVE_GROUP_MUTATION = gql`
    mutation DeleteUserFromGroup($userId: ID!, $groupId: ID!) {
        deleteStudentFromGroup(userId: $userId, groupId: $groupId)
    }`;


export default function StudentGroupPageCont() {
    let history = useHistory();
    const {groupid: groupId} = useParams();
    const {userId} = useAuthentication();

    const {data} = useQuery(STUDENT_GROUP_QUERY, {
        variables: {groupId: groupId},
    })

    const [leaveGroup] = useMutation(LEAVE_GROUP_MUTATION, {
        onCompleted: () => toast.notify(`Group left successfully`),
        onError: () => toast.notify(`Error`),
    });

    if (!data?.group) {
        return (<LoadingComp/>);
    }

    return (
        <StudentGroupPageComp
            group={data.group}
            onLeaveGroup={() =>
                leaveGroup({
                    variables: {
                        userId: userId,
                        groupId: groupId,
                    }
                }).then(() => history.push('/student'))
            }
            onNavigateBack={() => history.goBack()}
        />
    );
}


