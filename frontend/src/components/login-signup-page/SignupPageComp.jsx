import React from 'react';
import {ErrorMessage, Field, Form, Formik} from "formik";

export default function SignupPageComp({onSubmit, onValidate, onLogin, initialName, initialUsername}) {

    return (
        <div className="container">
            <section className="row justify-content-center">

                <Formik
                    initialValues={{
                        name: initialName ?? '',
                        username: initialUsername ?? '',
                        password: '',
                        confirm: ''
                    }}
                    onSubmit={onSubmit}
                    validateOnChange={true}
                    validateOnBlur={true}
                    validate={onValidate}
                    enableReinitialize={true}
                >
                    {({isValid}) => (
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
            </section>

            <section className="row justify-content-center">
                <div className="col-12 col-md-6 col-lg-4 my-3">
                    <div onClick={() => onLogin()} className="btn btn-light btn-block">
                        Log in
                    </div>
                </div>
            </section>
        </div>
    );
}



