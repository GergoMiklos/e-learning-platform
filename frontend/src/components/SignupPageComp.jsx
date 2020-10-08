import React from 'react';
import {ErrorMessage, Field, Form, Formik} from "formik";
import {useMutation} from "@apollo/client";
import gql from "graphql-tag";
import toast from "toasted-notes";
import client from "../ApolloClient";
import {useHistory} from "react-router-dom";

const USERNAME_ALREADY_EXISTS_QUERY = gql`
    query IsUsernameAlreadyRegistered($username: String!) {
        isUsernameAlreadyRegistered(username: $username)
    }`;

const REGISTER_MUTATION = gql`
    mutation Register($input: RegisterInput!) {
        register(input: $input)
    }`;

export default function SignupPageComp(props) {
    const [register] = useMutation(REGISTER_MUTATION, {
        onCompleted: (data) => {
            toast.notify(`Signed up successfully`)
            props.history.push(`/login`)
        },
        onError: () => toast.notify(`Error`),
    });

    async function isUsernameExists(username) {
        const result = await client.query({
            query: USERNAME_ALREADY_EXISTS_QUERY,
            variables: {username},
            fetchPolicy: 'cache-first'
        })
        return result.data?.isUsernameAlreadyRegistered;
    }

    async function validate(values) {
        let errors = {};
        if (!values.name || values.name.length > 50 || values.name.length < 5) {
            errors.name = 'Valid name is required!';
        }
        if (!values.username || values.username.length > 50 || values.username.length < 5) {
            errors.username = 'Valid user name is required!';
        }
        else {
            if(await isUsernameExists(values.username)) {
                errors.username = 'Username already exits!';
            }
        }
        if (!values.password || values.password.length > 50 || values.password.length < 5) {
            errors.password = 'Valid password is required!';
        } else if (!values.confirm || values.confirm !== values.password) {
            errors.confirm = 'Invalid password confirmation!';
        }
        return errors;
    }

    return (
        <div className="container">
            <div className="row justify-content-center">

                <Formik
                    initialValues={{name: '', username: '', password: '', confirm: ''}}
                    onSubmit={values => register({
                        variables: {
                            input: {
                                name: values.name,
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
                    {({isValid, validateForm}) => (
                        <Form className="col-12 col-md-6 col-lg-4 my-3 bg-light rounded shadow">
                            <fieldset className="from-group my-3">
                                <label>Full name</label>
                                <Field className="form-control"
                                       type="text"
                                       name="name"
                                       placeholder="Full name"
                                />
                                <ErrorMessage className="text-danger" name="name" component="div"/>
                            </fieldset>
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
                            <fieldset className="from-group my-3">
                                <label>Confirm Password</label>
                                <Field className="form-control"
                                       type="password"
                                       name="confirm"
                                       placeholder="Confirm Password"
                                />
                                <ErrorMessage className="text-danger" name="confirm" component="div"/>
                            </fieldset>
                            <div>
                                <button
                                    type="submit"
                                    className="btn btn-primary btn-block my-3"
                                    disabled={!isValid}
                                >
                                    Sign Up
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
                        onClick={() => props.history.push(`/login`)}
                    >
                        Log in
                    </div>
                </div>
            </div>
        </div>
    );
}



