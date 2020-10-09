import React, {useState} from 'react'
import gql from "graphql-tag";
import toast from 'toasted-notes';
import {useMutation, useQuery} from "@apollo/client";
import StudentGroupElementComp from "./StudentGroupElementComp";
import AuthService from "../AuthService";
import {useHistory} from "react-router-dom";

const STUDENT_GROUP_QUERY = gql`
    query getGroup($groupId: ID!) {
        group(groupId: $groupId) {
            id
            name
            code
            description
            news
            newsChangedDate
            tests {
                ...TestDetials
            }
        }
    }
${StudentGroupElementComp.fragments.TEST_DETAILS_FRAGMENT}`;

const LEAVE_GROUP_MUTATION = gql`
    mutation DeleteUserFromGroup($userId: ID!, $groupId: ID!) {
        deleteStudentFromGroup(userId: $userId, groupId: $groupId)
    }`;


export default function StudentGroupPageComp(props) {
    const [selectedTestId, setSelectedTestId] = useState(null);
    const {loading, error, data} = useQuery(STUDENT_GROUP_QUERY, {
        variables: {groupId: props.match.params.groupid},
    },)
    //todo ez (meg minden useMutation!) mehetne máshová is, nem? talán useeffecttel
    const [leaveGroup] = useMutation(LEAVE_GROUP_MUTATION, {
        onCompleted: (data) => {
            toast.notify(`Group left successfully`);
            props.history.goBack();
        },
        onError: () => toast.notify(`Error`),
    },);

    if (loading) {
        return (<div/>);
    }

    return (
        <div className="container">
            <div className="row justify-content-between my-1">
                <button className="col-auto btn btn-secondary" onClick={() => props.history.goBack()}>
                    Back
                </button>
                <button className="col-auto btn btn-outline-warning"
                        onClick={() => leaveGroup({
                            variables: {
                                userId: AuthService.getUserId(),
                                groupId: props.match.params.groupid
                            }
                        })}
                >
                    Leave
                </button>
            </div>

            <div className="row bg-primary text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">{data.group.name}</h1>
                <h1 className="col-auto rounded-pill bg-warning px-3">{data.group.code}</h1>
                <i className="col-12 mt-3">{data.group.description}</i>
            </div>

            <div className="row rounded shadow bg-light my-3 p-3">
                <h1 className="col-12">News</h1>
            </div>

            {data.group.news &&
            <div className="row alert alert-warning my-3">
                <div className="col-auto badge badge-primary text-center mr-2 mb-1 py-1">
                    {formatLongDate(new Date(data.group.newsChangedDate))}
                </div>
                <div className="col-12">
                    {data.group.news}
                </div>
            </div>
            }

            <div className="row rounded shadow bg-light my-3 p-3">
                <h1 className="col-12">Tests</h1>
            </div>

            {data.group.tests &&
            <div className="row my-3">
                <ul className="col-12 list-group">
                    {data.group.tests.map(test =>
                        <li
                            className="list-group-item list-group-item-action"
                            onClick={() => setSelectedTestId(test.id)}
                            key={test.id}
                        >
                            <StudentGroupElementComp
                                testId={test.id}
                                selectedTestId={selectedTestId}
                            />
                        </li>
                    )}
                </ul>
            </div>
            }
        </div>
    );
}


const formatLongDate = (date) => {
    const options = {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric'
    };
    return new Date(date).toLocaleString(/*navigator.language*/ 'en', options); //todo
}

