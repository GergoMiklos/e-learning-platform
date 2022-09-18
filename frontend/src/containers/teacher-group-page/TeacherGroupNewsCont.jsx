import React, { useState } from 'react';
import gql from 'graphql-tag';
import toast from 'toasted-notes';
import { useMutation } from '@apollo/client';
import PropTypes, { number, string } from 'prop-types';
import TeacherGroupNewsComp from '../../components/teacher-group-page/TeacherGroupNewsComp';

const CHANGE_NEWS_MUTATION = gql`
  mutation EditGroupNews($groupId: ID!, $text: String!) {
    editGroupNews(groupId: $groupId, text: $text) {
      id
      news
      newsChangedDate
    }
  }
`;

export default function TeacherGroupNewsCont({ groupId }) {
  const [newsText, setNewsText] = useState('');

  const [changeNews] = useMutation(CHANGE_NEWS_MUTATION, {
    onCompleted: () => toast.notify('News changed successfully'),
    onError: () => toast.notify('Error'),
  });

  return (
    <TeacherGroupNewsComp
      newsText={newsText}
      onNewsTextChange={(value) => setNewsText(value.slice(0, 500))}
      changeDisabled={!newsText || !newsText.trim().length}
      onChangeNews={() =>
        changeNews({ variables: { groupId, text: newsText } })
      }
    />
  );
}

TeacherGroupNewsCont.propTypes = {
  groupId: PropTypes.oneOfType([number, string]).isRequired,
};
