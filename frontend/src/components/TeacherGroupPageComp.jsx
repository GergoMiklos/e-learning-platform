import React, {useState} from 'react'
import gql from "graphql-tag";
import NewTestDialogComp from "./NewTestDialogComp";
import toast from "toasted-notes";
import {useMutation, useQuery} from "@apollo/client";
import TeacherGroupElementComp from "./TeacherGroupElementComp";
import {Link, useHistory, useRouteMatch, Route} from "react-router-dom";
import LoadingComp from "./LoadingComp";

const TEACHER_GROUP_QUERY = gql`
    query getGroup($groupId: ID!) {
        group(groupId: $groupId) {
            id
            name
            code
            description
            news
            newsChangedDate
            tests {
                ...TestDetails
            }
        }
    }
${TeacherGroupElementComp.fragments.TEST_DETAILS_FRAGMENT}`;

const CHANGE_NEWS_MUTATION = gql`
    mutation EditGroupNews($groupId: ID!, $text: String!) {
        editGroupNews(groupId: $groupId, text: $text) {
            id
            news
            newsChangedDate
        }
    }`;

export default function TeacherGroupPageComp(props) {
    const [selectedTestId, setSelectedTestId] = useState(null);
    const [changeNewsText, setChangeNewsText] = useState(null);

    const {loading, error, data} = useQuery(
        TEACHER_GROUP_QUERY, {
            variables: {groupId: props.match.params.groupid},
        });
    const [changeNews] = useMutation(CHANGE_NEWS_MUTATION, {
        onCompleted: () => toast.notify(`News changed successfully`),
        onError: () => toast.notify(`Error`),
    });

    if (!data?.group) {
        return (<LoadingComp/>);
    }

    return (
        <div className="container">
            <div className="row justify-content-between my-1">
                <button className="col-auto btn btn-secondary" onClick={() => props.history.goBack()}>
                    Back
                </button>
                <button
                    className="col-auto btn btn-outline-warning"
                    onClick={() => props.history.push(`${props.match.url}/edit`)}
                >
                    Edit
                </button>
            </div>

            <div className="row bg-warning text-light rounded shadow my-3 p-3">
                <h1 className="col-auto">{data.group.name}</h1>
                <h1 className="col-auto rounded-pill bg-primary px-3">{data.group.code}</h1>
                <i className="col-12 mt-3">{data.group.description}</i>
            </div>

            <div className="row rounded shadow bg-light my-3 p-3">
                <h1 className="col-12">News</h1>
            </div>

            <div className="row input-group mb-3">
                <div className="input-group-prepend">
                    <span className="input-group-text">
                        Change news
                    </span>
                </div>
                <textarea
                    className="form-control"
                    placeholder="News"
                    onChange={event => setChangeNewsText(event.target.value.slice(0, 500))}
                />
                <div className="input-group-append">
                    <button
                        className="btn btn-outline-primary"
                        onClick={() => changeNews({variables: {groupId: data.group.id, text: changeNewsText}})}
                        disabled={!changeNewsText || !changeNewsText.trim().length}
                    >
                        Change
                    </button>
                </div>
            </div>

            {data.group.news &&
            <div className="row alert alert-warning my-3">
                <div className="badge badge-primary text-center mr-2 mb-1 py-1">
                    {formatLongDate(new Date(data.group.newsChangedDate))}
                </div>
                <div className="col-12 ">
                    {data.group.news}
                </div>
            </div>
            }

            <div className="row rounded shadow bg-light my-3 p-3 justify-content-between">
                <h1>Tests</h1>
                <Link to={`${props.match.url}/new`}>
                    <button className="btn btn-lg btn-primary">
                        {'New'}
                    </button>
                </Link>
            </div>

            <Route path={`${props.match.url}/new`} render={(props) =>
                (<NewTestDialogComp groupId={data.group.id} {...props} />)
            }/>

            {/*<NewTestDialogComp*/}
            {/*    show={showNewTestDialog}*/}
            {/*    onHide={() => setShowNewTestDialog(false)}*/}
            {/*    groupId={data.group.id}*/}
            {/*/>*/}

            {data.group.tests?.length === 0 ? "No Tests" :
                <div className="row my-3">
                <ul className="col-12 list-group">
                    {data.group.tests.map(test => //todo miért nem csak egy onEdit/onStatuses fgv van, és adja át az idet az element stb? Még jobb lenne ha a router dolgokat ő intézné
                        <li
                            className="list-group-item list-group-item-action"
                            onClick={() => setSelectedTestId(test.id)}
                            key={test.id}
                        >
                            <TeacherGroupElementComp
                                testId={test.id}
                                selectedTestId={selectedTestId}
                            />
                        </li>
                    )}
                </ul>
            </div>}
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
