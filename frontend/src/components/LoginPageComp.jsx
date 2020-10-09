import React from 'react';
import {ErrorMessage, Field, Form, Formik} from "formik";
import gql from "graphql-tag";
import {useMutation} from "@apollo/client";
import {useHistory} from "react-router-dom";
import toast from "toasted-notes";
import AuthService from "../AuthService";

const LOGIN_MUTATION = gql`
    mutation Login($input: LoginInput!) {
        login(input: $input) {
            token
            userId
        }
    }`;


export default function LoginPageComp(props) {
    let history = useHistory();

    const [login] = useMutation(LOGIN_MUTATION, {
        onCompleted: (data) => {
            AuthService.setLogin({token: data.login.token, userId: data.login.userId})
            history.push(`/student`);
        },
        onError: () => toast.notify(`Invalid username or password`),
    });

    return (
        <div className="container">
            <div className="row justify-content-center">
                <Formik
                    initialValues={{username: '', password: ''}}
                    onSubmit={values => login({
                        variables: {
                            input: {
                                username: values.username,
                                password: values.password
                            }
                        }
                    })}
                    validateOnChange={true}
                    validateOnBlur={true}
                    validate={validate}
                    enableReinitialize={true}
                >
                    {({isValid}) => (
                        <Form className="col-12 col-md-6 col-lg-4 my-3 bg-light rounded shadow">
                            <fieldset className="from-group my-3">
                                <label>Username</label>
                                <Field className="form-control"
                                       type="text"
                                       name="username"
                                       placeholder="Username"
                                />
                                <ErrorMessage className="text-danger" name="username" component="div"/>
                            </fieldset>
                            <fieldset className="from-group my-3">
                                <label>Password</label>
                                <Field className="form-control"
                                       type="password"
                                       name="password"
                                       placeholder="Password"
                                />
                                <ErrorMessage className="text-danger" name="password" component="div"/>
                            </fieldset>
                            <div>
                                <button
                                    type="submit"
                                    className="btn btn-primary btn-block my-3"
                                    disabled={!isValid}
                                >
                                    Log In
                                </button>
                            </div>
                        </Form>
                    )}
                </Formik>
            </div>
            <div className="row justify-content-center">
                <div className="col-12 col-md-6 col-lg-4 my-3">
                    <div
                        className="btn btn-light btn-block"
                        onClick={() => props.history.push(`/register`)}
                    >
                        Sign up
                    </div>
                </div>
            </div>
        </div>
    );
}

const validate = (values) => {
    let errors = {};
    // if (!values.username || values.username.length > 50 || values.username.length < 5) {
    //     errors.username = 'Valid user name is required!';
    // }
    // if (!values.password || values.password.length > 50 || values.password.length < 5) {
    //     errors.passsword = 'Valid password is required!';
    // }
    return errors;
}
