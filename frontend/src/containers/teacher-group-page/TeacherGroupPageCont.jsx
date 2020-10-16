import React from 'react'
import gql from "graphql-tag";
import {useQuery} from "@apollo/client";
import {useParams, useRouteMatch, useHistory} from "react-router-dom";
import LoadingComp from "../../components/common/LoadingComp";
import TeacherGroupElementCont from "../../containers/teacher-group-page/TeacherGroupElementCont";
import TeacherGroupPageComp from "../../components/teacher-group-page/TeacherGroupPageComp";

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
${TeacherGroupElementCont.fragments.TEST_DETAILS_FRAGMENT}`;


export default function TeacherGroupPageCont() {
    let match = useRouteMatch();
    let history = useHistory();
    const {groupid: groupId} = useParams()

    const {data} = useQuery(
        TEACHER_GROUP_QUERY, {
            variables: {groupId: groupId},
        });

    if (!data?.group) {
        return (<LoadingComp/>);
    }

    return (
        <TeacherGroupPageComp
            group={data.group}
            editPath={`${match.url}/edit`}
            onNavigateBack={() => history.goBack()}
        />
    );
}

