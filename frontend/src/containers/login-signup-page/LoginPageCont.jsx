import React from 'react';
import gql from "graphql-tag";
import {useMutation} from "@apollo/client";
import {useHistory, useLocation} from "react-router-dom";
import toast from "toasted-notes";
import {useAuthentication} from "../../AuthService";
import LoginPageComp from "../../components/login-signup-page/LoginPageComp";

const LOGIN_MUTATION = gql`
    mutation Login($input: LoginInput!) {
        login(input: $input) {
            token
            user {
                id
                name
                code
            }
        }
    }`;


export default function LoginPageCont() {
    let history = useHistory();
    let {from} = useLocation().state || {from: {pathname: "/"}};
    let {setLogin} = useAuthentication();

    const [login] = useMutation(LOGIN_MUTATION, {
        onCompleted: (data) => {
            setLogin({token: data.login.token, userId: data.login.user.id})
            history.replace(from);
        },
        onError: () => toast.notify(`Invalid username or password`),
    });

    return (
        <LoginPageComp
            onSubmit={values => login({
                variables: {
                    input: {
                        username: values.username,
                        password: values.password
                    }
                }
            })}

            onValidate={validate}
            onSignup={() => history.push(`/register`)}
        />
    );
}


const validate = (values) => {
    let errors = {};
    if (!values.username || values.username.length > 50 || values.username.length < 5) {
        errors.username = 'Valid user name is required!';
    }
    if (!values.password || values.password.length > 50 || values.password.length < 5) {
        errors.passsword = 'Valid password is required!';
    }
    return errors;
}
