import React from 'react';
import gql from "graphql-tag";
import toast from "toasted-notes";
import {useMutation} from "@apollo/client";
import {useAuthentication} from "../../AuthService";
import ParentElementComp from "../../components/parent-page/ParentElementComp";
import client from "../../ApolloClient";

const STUDENTSTATUS_DETAILS_FRAGMENT = gql`
    fragment ParentStudentStatusDetails on StudentStatus {
        id
        status
        statusChangedTime
        correctSolutions
        allSolutions
        solvedTasks
        test {
            id
            name
            description
            allTasks
        }
    }`;

const FOLLOWED_STUDENT_DETAILS_FRAGMENT = gql`
    fragment FollowedStudentDetails on User {
        id
        name
        code
        studentStatuses {
            ...ParentStudentStatusDetails
        }
    }
${STUDENTSTATUS_DETAILS_FRAGMENT}`

const DELETE_FOLLOWED_STUDENT_MUTATION = gql`
    mutation DeleteStudentFromParent($parentId: ID!, $studentId: ID!) {
        deleteStudentFromParent(parentId: $parentId, studentId: $studentId)
    }`;


export default function ParentElementCont({studentId}) {
    const {userId} = useAuthentication()

    const student = client.readFragment({
        id: `User:${studentId}`,
        fragment: FOLLOWED_STUDENT_DETAILS_FRAGMENT,
        fragmentName: 'FollowedStudentDetails',
    });

    const [deleteFollowed] = useMutation(DELETE_FOLLOWED_STUDENT_MUTATION, {
        onCompleted: () => toast.notify(`Student deleted successfully`),
        onError: () => toast.notify(`Error :(}`),
        update: (cache) => {
            cache.modify({
                id: `User:${userId}`,
                fields: {
                    followedStudents(existingUserRefs = [], {readField}) {
                        return existingUserRefs.filter(
                            userRef => studentId !== readField('id', userRef)
                        );
                    },
                }
            });
        },
    });

    if(!studentId) {
        return (<div/>);
    }

    return (
        <ParentElementComp
            student={student}

            onDelete={() => deleteFollowed({
                variables: {
                    parentId: userId, studentId: studentId,
                }
            })}
        />

    );
}

ParentElementCont.fragments = {
    STUDENTSTATUS_DETAILS_FRAGMENT: STUDENTSTATUS_DETAILS_FRAGMENT,
    FOLLOWED_STUDENT_DETAILS_FRAGMENT: FOLLOWED_STUDENT_DETAILS_FRAGMENT,
}

