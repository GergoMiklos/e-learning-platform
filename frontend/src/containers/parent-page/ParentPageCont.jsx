import React, {useState} from 'react';
import gql from "graphql-tag";
import toast from "toasted-notes";
import {useMutation, useQuery} from "@apollo/client";
import {useAuthentication} from "../../AuthService";
import LoadingComp from "../../components/common/LoadingComp";
import ParentElementCont from "./ParentElementCont";
import ParentPageComp from "../../components/parent-page/ParentPageComp";

const PARENT_FOLLOWED_STATUSES_QUERY = gql`
    query getUser($userId: ID!) {
        user(userId: $userId) {
            id
            followedStudents {
                ...FollowedStudentDetails
            }
        }
    }
${ParentElementCont.fragments.FOLLOWED_STUDENT_DETAILS_FRAGMENT}`


const ADD_FOLLOWED_STUDENT_MUTATION = gql`
    mutation AddStudentFromCodeToParent($parentId: ID!, $studentCode: String!) {
        addStudentFromCodeToParent(parentId: $parentId, studentCode: $studentCode)  {
            ...FollowedStudentDetails
        }
    }
${ParentElementCont.fragments.FOLLOWED_STUDENT_DETAILS_FRAGMENT}`

export default function ParentPageCont(props) {
    const {userId} = useAuthentication();
    const [addFollowedCode, setAddFollowedCode] = useState('');

    const {data} = useQuery(PARENT_FOLLOWED_STATUSES_QUERY, {
        variables: {userId: userId},
    });

    const [addFollowed] = useMutation(ADD_FOLLOWED_STUDENT_MUTATION, {
        onCompleted: (data) => toast.notify(`Student followed: ${data.addStudentFromCodeToParent.name}`),
        onError: (error) => toast.notify(`No user with code: ${addFollowedCode}`),
        update: (cache, {data: {addStudentFromCodeToParent}}) => {
            cache.modify({
                id: `User:${data.user.id}`,
                fields: {
                    followedStudents(existingUserRefs, {INVALIDATE}) {
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
        <ParentPageComp
            user={data.user}
            addFollowedCode={addFollowedCode}
            onAddFollowedCodeChange={value => setAddFollowedCode(value.toUpperCase().slice(0, 8))}
            isAddFollowedDisabled={!addFollowedCode || !addFollowedCode.trim().length}

            onAddFollowedStudent={() => addFollowed({
                variables: {
                    parentId: userId,
                    studentCode: addFollowedCode
                }
            })}

        />
    );
}

