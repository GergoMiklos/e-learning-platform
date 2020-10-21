import React from 'react';
import { ErrorMessage, Field, Form, Formik } from 'formik';
import PropTypes from 'prop-types';

export default function LoginPageComp({
  onSubmit,
  onValidate,
  onSignup,
  initialUsername,
}) {
  return (
    <div className="container">
      <section className="row justify-content-center">
        <Formik
          initialValues={{ username: initialUsername ?? '', password: '' }}
          onSubmit={onSubmit}
          validateOnChange
          validateOnBlur
          validate={onValidate}
          enableReinitialize
        >
          {({ isValid }) => (
            <Form className="col-12 col-md-6 col-lg-4 my-3 bg-light rounded shadow">
              <fieldset className="from-group my-3">
                <label>Username</label>
                <Field
                  className="form-control"
                  type="text"
                  name="username"
                  placeholder="Username"
                />
                <ErrorMessage
                  className="text-danger"
                  name="username"
                  component="div"
                />
              </fieldset>
              <fieldset className="from-group my-3">
                <label>Password</label>
                <Field
                  className="form-control"
                  type="password"
                  name="password"
                  placeholder="Password"
                />
                <ErrorMessage
                  className="text-danger"
                  name="password"
                  component="div"
                />
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
      </section>

      <section className="row justify-content-center">
        <div className="col-12 col-md-6 col-lg-4 my-3">
          <button
            onClick={() => onSignup()}
            className="btn btn-light btn-block"
          >
            Sign up
          </button>
        </div>
      </section>
    </div>
  );
}

LoginPageComp.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  onValidate: PropTypes.func.isRequired,
  onSignup: PropTypes.func.isRequired,
  initialUsername: PropTypes.string,
};
