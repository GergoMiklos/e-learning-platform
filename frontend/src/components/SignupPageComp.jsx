import React from 'react';
import {ErrorMessage, Field, Form, Formik} from "formik";

export default function SignupPageComp(props) {
    return (
        <div className="container">
            <div className="row justify-content-center">
                <Formik
                    initialValues={{Username: '', Password: ''}}
                    onSubmit={this.onSubmit}
                    validateOnChange={true}
                    validateOnBlur={true}
                    validate={this.validate}
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
                    <div className="btn btn-light btn-block">Log in</div>
                </div>
            </div>
        </div>
    );
}

const validate = (values) => {
    let errors = {};
    if (!values.username || values.username.length > 50 || values.username.length < 5) {
        errors.username = 'Valid user name is required!';
    }
    if (!values.password || values.password.length > 50 || values.password.length < 5) {
        errors.password = 'Valid password is required!';
    } else if (!values.confirm || values.confirm !== values.password) {
        errors.confirm = 'Invalid password confirmation!';
    }
    return errors;
}

const onSubmit = (values) => {
    // let userId = AuthenticationService.getUserId();
    // client.mutate({
    //     mutation: CREATE_GROUP,
    //     variables: {input: {description: values.description, name: values.name}, userId: userId},
    // })
    //     .then(() => {
    //         this.showNotification({
    //             text: 'Group created successfully',
    //             type: 'success',
    //         })
    //         this.props.onHide();
    //     })
    //     .catch(errors => {
    //         console.log(errors);
    //         this.showNotification({text: 'Something went wrong', type: 'danger'});
    //     })
}