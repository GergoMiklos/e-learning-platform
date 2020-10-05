import React, {useState} from 'react';
import gql from "graphql-tag";
import toast from "toasted-notes";
import AuthenticationService from "../AuthenticationService";
import StatusElementComp from "./StatusElementComp";
import {useMutation, useQuery} from "@apollo/client";
import ParentElementComp from "./ParentElementComp";

const PARENT_FOLLOWED_STATUSES_QUERY = gql`
    query getUser($userId: ID!) {
        user(userId: $userId) {
            id
            followedStudents {
                ...FollowedStudentDetails
            }
        }
    }
${ParentElementComp.fragments.FOLLOWED_STUDENT_DETAILS_FRAGMENT}`


const ADD_FOLLOWED_STUDENT_MUTATION = gql`
    mutation AddStudentFromCodeToParent($parentId: ID!, $studentCode: String!) {
        addStudentFromCodeToParent(parentId: $parentId, studentCode: $studentCode)  {
            ...FollowedStudentDetails
        }
    }
${ParentElementComp.fragments.FOLLOWED_STUDENT_DETAILS_FRAGMENT}`

export default function ParentPageComp(props) {
    const userId = AuthenticationService.getUserId();
    const [addFollowedCode, setAddFollowedCode] = useState('');
    const {loading, error, data} = useQuery(PARENT_FOLLOWED_STATUSES_QUERY, {
        variables: {userId: userId},
    });
    const [addFollowed] = useMutation(ADD_FOLLOWED_STUDENT_MUTATION, {
        onCompleted: (data) => toast.notify(`Student followed: ${data.addStudentFromCodeToParent.name}`),
        onError: (error) => toast.notify(`No user with code: ${error}`),
        update: (cache, {data: {addStudentFromCodeToParent}}) => {
            cache.modify({
                id: `User:${data.user.id}`,
                fields: {
                    //Todo melyik jobb?
                    // studentGroups(existingGroupRefs, {INVALIDATE}) {
                    //     return INVALIDATE;
                    // },

                    followedStudents(existingUserRefs = [], {readField}) {
                        const newUserRef = cache.writeFragment({
                            data: addStudentFromCodeToParent,
                            fragment: ParentElementComp.fragments.FOLLOWED_STUDENT_DETAILS_FRAGMENT,
                            fragmentName: 'FollowedStudentDetails',
                        });

                        if (existingUserRefs.some(ref => readField('id', ref) === newUserRef.id)) {
                            return existingUserRefs;
                        }

                        return [...existingUserRefs, newUserRef];
                    },
                },
            });
        },
    });

    if (!data) {
        return (<div/>);
    }

    return (
        <div className="container">
            <div className="row input-group mx-0 my-3">
                <div className="input-group-prepend">
                    <span className="input-group-text">Follow student:</span>
                </div>
                <input
                    type="text" className="form-control"
                    placeholder="USER CODE"
                    value={addFollowedCode}
                    onChange={event => setAddFollowedCode(event.target.value.toUpperCase().slice(0, 8))}
                />
                <div className="input-group-append">
                    <button
                        className="btn btn-outline-primary"
                        onClick={() => addFollowed({
                            variables: {
                                parentId: data.user.id,
                                studentCode: addFollowedCode
                            }
                        })}
                        disabled={!addFollowedCode || !addFollowedCode.trim().length}
                    >
                        Add
                    </button>
                </div>
            </div>

            <div className="row btn-group btn-block m-1">
                <div className="col-3 btn btn-info disabled">Not Started</div>
                <div className="col-3 btn btn-success disabled">In Progress</div>
                <div className="col-3 btn btn-warning disabled">Inactive</div>
                <div className="col-3 btn btn-danger disabled">Problem</div>
            </div>

            {data.user.followedStudents.map(student =>
                <ParentElementComp
                    key={student.id}
                    student={student}
                    parentId={data.user.id}
                />
            )}
        </div>
    );
}

