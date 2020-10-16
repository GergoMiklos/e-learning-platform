import React, {useState} from 'react';
import gql from "graphql-tag";
import toast from "toasted-notes";
import StatusElementComp from "./common/StatusElementComp";
import {useMutation} from "@apollo/client";
import AuthService from "../AuthService";

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

//Todo read fragment studentId
export default function ParentElementComp(props) {
    const [deleteFollowed] = useMutation(DELETE_FOLLOWED_STUDENT_MUTATION, {
        onCompleted: () => toast.notify(`Student deleted successfully`),
        onError: () => toast.notify(`Error :(}`),
        update: (cache) => {
            cache.modify({
                id: `User:${AuthService.getUserId()}`,
                fields: {
                    followedStudents(existingUserRefs = [], {readField}) {
                        return existingUserRefs.filter(
                            userRef => props.student.id !== readField('id', userRef)
                        );
                    },
                }
            });
        },
    });

    return (
        <div>
            <div className="col-12 rounded shadow my-3 p-3 d-flex justify-content-between bg-light">
                <h1>{props.student.name}</h1>
                <button
                    className="btn btn-outline-warning"
                    onClick={() => deleteFollowed({
                        variables: {
                            parentId: props.parentId, studentId: props.student.id,
                        }
                    })}
                >
                    Unfollow
                </button>
            </div>

            {props.student.studentStatuses?.length === 0 ? "No Statuses" :
                <div className="table-responsive-sm px-1">
                    <table className="col-12 table table-striped bg-light">
                        <thead>
                        <tr>
                            <th>Test</th>
                            <th className="text-center">Tasks (Solved/All)</th>
                            <th className="text-center">Answers (Correct/All)</th>
                            <th className="text-center">Status</th>
                        </tr>
                        </thead>

                        <tbody>
                        {props.student.studentStatuses.map(status =>
                            <tr key={status.id}>
                                <td className="font-weight-bold">
                                    {status.test.name}
                                </td>
                                <td className="text-center">
                                    {status.solvedTasks}/{status.test.allTasks}
                                </td>
                                <td className="text-center">
                                    {status.correctSolutions}/{status.allSolutions}
                                </td>
                                <td className='text-center'>
                                    <StatusElementComp studentStatus={status}/>
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            }
        </div>
    );
}

ParentElementComp.fragments = {
    STUDENTSTATUS_DETAILS_FRAGMENT: STUDENTSTATUS_DETAILS_FRAGMENT,
    FOLLOWED_STUDENT_DETAILS_FRAGMENT: FOLLOWED_STUDENT_DETAILS_FRAGMENT,
}

