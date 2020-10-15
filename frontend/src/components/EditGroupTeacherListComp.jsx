import React, {useState} from 'react'
import gql from "graphql-tag";
import toast from "toasted-notes";
import {useMutation} from "@apollo/client";
import EditGroupUserElementComp from "./EditGroupUserElementComp";
import EditGroupTeacherElementComp from "./EditGroupTeacherElementComp";
import {useHistory} from "react-router-dom";

const ADD_TEACHER_MUTATION = gql`
    mutation AddTeacherFromCodeToGroup($groupId: ID!, $userCode: String!) {
        addTeacherFromCodeToGroup(groupId: $groupId, userCode: $userCode)  {
            ...UserDetails
        }
    }
${EditGroupUserElementComp.fragments.USER_DETAILS_FRAGMENT}`;


export default function EditGroupTeacherListComp(props) {
    const [addUserCode, setAddUserCode] = useState('');
    const [selectedUserId, selectUserId] = useState(null);
    const [addTeacher] = useMutation(ADD_TEACHER_MUTATION, {
        onCompleted: (data) => toast.notify(`Teacher added: ${data.addStudentFromCodeToGroup.name}`),
        onError: () => toast.notify(`No user with code: ${addUserCode}`),
        update: (cache, {data: {addStudentToGroupFromCode}}) => {
            cache.modify({
                id: `Group:${props.groupId}`,
                fields: {
                    //Todo melyik jobb?
                    teachers(existingUserRefs, {INVALIDATE}) {
                        return INVALIDATE;
                    },

                    //     studentGroups(existingGroupRefs = [], { readField }) {
                    //         console.log(user);
                    //         console.log(addStudentToGroupFromCode);
                    //         console.log(existingGroupRefs);
                    //
                    //         const newGroupRef = cache.writeFragment({
                    //             data: addStudentToGroupFromCode,
                    //             fragment: gql`
                    //                 fragment addGroup on Group {
                    //                     id
                    //                     name
                    //                     news
                    //                     newsChangedDate
                    //                 }`
                    //         });
                    //
                    //         if (existingGroupRefs.some(ref => readField('id', ref) === newGroupRef.id)) {
                    //             return existingGroupRefs;
                    //         }
                    //
                    //         return [...existingGroupRefs, newGroupRef];
                    //     },
                },
            });
        },
    });

    return (
        <div>
            <div className="row rounded shadow bg-light my-3 p-3">
                <h1 className="col-12">Teachers</h1>
            </div>

            <div className="row input-group px-3">
                <div className="input-group-prepend">
                    <span className="input-group-text">New Teacher: </span>
                </div>
                <input
                    type="text"
                    className="form-control"
                    placeholder="USER CODE"
                    value={addUserCode}
                    onChange={event => setAddUserCode(event.target.value.toUpperCase().slice(0, 8))}
                />
                <div className="input-group-append">
                    <button className="btn btn-primary" onClick={() => addTeacher({
                        variables: {
                            groupId: props.groupId,
                            userCode: addUserCode
                        }
                    })}>
                        Add
                    </button>
                </div>
            </div>

            <div className="row my-3">
                <ul className="col-12 list-group">
                    {props.teachers?.length === 0 ? "No Teachers" : props.teachers.map(user =>
                        <li
                            className="list-group-item list-group-item-action"
                            key={user.id}
                            onClick={() => selectUserId(user.id)}
                        >
                            <EditGroupTeacherElementComp
                                userId={user.id}
                                groupId={props.groupId}
                                selectedUserId={selectedUserId}
                            />
                        </li>
                    )}
                </ul>
            </div>
        </div>
    );
}
