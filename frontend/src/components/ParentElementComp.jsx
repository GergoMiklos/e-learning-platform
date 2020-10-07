import React, {useState} from 'react';
import gql from "graphql-tag";
import toast from "toasted-notes";
import StatusElementComp from "./StatusElementComp";
import {useMutation, useQuery} from "@apollo/client";

const USERTESTSTATUS_DETAILS_FRAGMENT = gql`
    fragment UserTestStatusDetials on UserTestStatus {
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
        userTestStatuses {
            ...UserTestStatusDetials
        }
    }
${USERTESTSTATUS_DETAILS_FRAGMENT}`

const DELETE_FOLLOWED_STUDENT_MUTATION = gql`
    mutation DeleteStudentFromParent($parentId: ID!, $studentId: ID!) {
        deleteStudentFromParent(parentId: $parentId, studentId: $studentId)
    }`;


export default function ParentElementComp(props) {
    const [deleteFollowed] = useMutation(DELETE_FOLLOWED_STUDENT_MUTATION, {
        onCompleted: () => toast.notify(`Student deleted successfully`),
        onError: () => toast.notify(`Error :(}`),
        update: (cache) => {
            cache.modify({
                id: `User:${props.parentId}`, //Todo userid kell és nem szép, esetleg store?
                fields: {
                    //Todo melyik jobb?
                    // studentGroups(existingGroupRefs, {INVALIDATE}) {
                    //     return INVALIDATE;
                    // },

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

            {props.student.userTestStatuses.length === 0 ? "No statuses" :
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
                        {props.student.userTestStatuses.map(uts =>
                            <tr key={uts.id}>
                                <td className="font-weight-bold">
                                    {uts.test.name}
                                </td>
                                <td className="text-center">
                                    {uts.solvedTasks}/{uts.test.allTasks}
                                </td>
                                <td className="text-center">
                                    {uts.correctSolutions}/{uts.allSolutions}
                                </td>
                                <td className='text-center'>
                                    <StatusElementComp userTestStatus={uts}/>
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
    USERTESTSTATUS_DETAILS_FRAGMENT: USERTESTSTATUS_DETAILS_FRAGMENT,
    FOLLOWED_STUDENT_DETAILS_FRAGMENT: FOLLOWED_STUDENT_DETAILS_FRAGMENT,
}

