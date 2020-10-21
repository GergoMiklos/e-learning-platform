import React from 'react';
import { useMutation } from '@apollo/client';
import gql from 'graphql-tag';
import toast from 'toasted-notes';
import { useHistory } from 'react-router-dom';
import client from '../../ApolloClient';
import SignupPageComp from '../../components/login-signup-page/SignupPageComp';

const USERNAME_ALREADY_EXISTS_QUERY = gql`
  query IsUsernameAlreadyRegistered($username: String!) {
    isUsernameAlreadyRegistered(username: $username)
  }
`;

const REGISTER_MUTATION = gql`
  mutation Register($input: RegisterInput!) {
    register(input: $input)
  }
`;

export default function SignupPageCont() {
  const history = useHistory();

  const [register] = useMutation(REGISTER_MUTATION, {
    onCompleted: () => {
      toast.notify('Signed up successfully');
      history.push('/login');
    },
    onError: () => toast.notify('Error'),
  });

  return (
    <SignupPageComp
      onSubmit={(values) =>
        register({
          variables: {
            input: {
              name: values.name,
              username: values.username,
              password: values.password,
            },
          },
        })
      }
      onLogin={() => history.push('/login')}
      onValidate={validate}
    />
  );
}

async function validate(values) {
  const errors = {};
  if (!values.name || values.name.length > 50 || values.name.length < 5) {
    errors.name = 'Valid name is required!';
  }
  if (
    !values.username ||
    values.username.length > 50 ||
    values.username.length < 5
  ) {
    errors.username = 'Valid user name is required!';
  } else if (await isUsernameExists(values.username)) {
    errors.username = 'Username already exits!';
  }
  if (
    !values.password ||
    values.password.length > 50 ||
    values.password.length < 5
  ) {
    errors.password = 'Valid password is required!';
  } else if (!values.confirm || values.confirm !== values.password) {
    errors.confirm = 'Invalid password confirmation!';
  }
  return errors;
}

async function isUsernameExists(username) {
  const result = await client.query({
    query: USERNAME_ALREADY_EXISTS_QUERY,
    variables: { username },
    fetchPolicy: 'cache-first',
  });
  return result.data?.isUsernameAlreadyRegistered;
}
