import React from 'react';
import gql from "graphql-tag";
import toast from "toasted-notes";
import {useMutation} from "@apollo/client";
import {useAuthentication} from "../../AuthService";
import ParentElementComp from "../../components/parent-page/ParentElementComp";
import PropTypes from "prop-types";

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
            id
            ...ParentStudentStatusDetails
        }
    }
${STUDENTSTATUS_DETAILS_FRAGMENT}`

const DELETE_FOLLOWED_STUDENT_MUTATION = gql`
    mutation DeleteStudentFromParent($parentId: ID!, $studentId: ID!) {
        deleteStudentFromParent(parentId: $parentId, studentId: $studentId)
    }`;


export default function ParentElementCont({student}) {
    const {userId} = useAuthentication()

    const [deleteFollowed] = useMutation(DELETE_FOLLOWED_STUDENT_MUTATION, {
        onCompleted: () => toast.notify(`Student deleted successfully`),
        onError: () => toast.notify(`Error :(}`),
        update: (cache) => {
            cache.modify({
                id: `User:${userId}`,
                fields: {
                    followedStudents(existingUserRefs = [], {readField}) {
                        return existingUserRefs.filter(
                            userRef => student.id !== readField('id', userRef)
                        );
                    },
                }
            });
        },
    });

    if(!student) {
        return (<div/>);
    }

    return (
        <ParentElementComp
            student={student}

            onDelete={() => deleteFollowed({
                variables: {
                    parentId: userId, studentId: student.id,
                }
            })}
        />

    );
}

ParentElementCont.fragments = {
    STUDENTSTATUS_DETAILS_FRAGMENT: STUDENTSTATUS_DETAILS_FRAGMENT,
    FOLLOWED_STUDENT_DETAILS_FRAGMENT: FOLLOWED_STUDENT_DETAILS_FRAGMENT,
}

ParentElementCont.propTypes = {
    student: PropTypes.object.isRequired,
}

