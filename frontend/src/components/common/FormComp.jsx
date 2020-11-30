import React from 'react';
import { ErrorMessage, Field, Form, Formik } from 'formik';
import PropTypes from 'prop-types';

export default function FormComp({
  initialName,
  initialDescription,
  onSubmit,
  validate,
}) {
  return (
    <Formik
      initialValues={{
        name: initialName ?? '',
        description: initialDescription ?? '',
      }}
      onSubmit={(values, { setSubmitting }) => {
        onSubmit(values);
        setSubmitting(false);
      }}
      validateOnChange
      validateOnBlur
      validate={validate}
      enableReinitialize
    >
      {({ isValid, isSubmitting }) => (
        <Form>
          <fieldset className="from-group m-3">
            <label>Name</label>
            <Field
              className="form-control"
              type="text"
              name="name"
              placeholder="Name"
            />
            <ErrorMessage 
              className="text-danger" 
              name="name" 
              component="div" 
            />
          </fieldset>
          <fieldset className="from-group m-3">
            <label>Description</label>
            <Field
              className="form-control"
              type="text"
              as="textarea"
              name="description"
              placeholder="Description"
            />
            <ErrorMessage
              className="text-danger"
              name="description"
              component="div"
            />
          </fieldset>
          <div className="btn-group my-2">
            <button
              type="submit"
              className="btn btn-primary"
              disabled={!isValid || isSubmitting}
            >
              Save
            </button>
          </div>
        </Form>
      )}
    </Formik>
  );
}

FormComp.propTypes = {
  initialName: PropTypes.string,
  initialDescription: PropTypes.string,
  onSubmit: PropTypes.func.isRequired,
  validate: PropTypes.func.isRequired,
};
