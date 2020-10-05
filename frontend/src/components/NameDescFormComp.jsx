import React from 'react'
import {ErrorMessage, Field, Form, Formik} from 'formik'


export default function NameDescFormComp(props) {
    return (
        <Formik
            initialValues={{name: props.initialName || '', description: props.initialDescription || ''}}
            onSubmit={(values, {setSubmitting}) => {
                props.onSubmit(values);
                setSubmitting(false);
            }}
            validateOnChange={true}
            validateOnBlur={true}
            validate={validate}
            enableReinitialize={true}
        >
            {({isValid, isSubmitting}) => (
                <Form>
                    <fieldset className="from-group m-3">
                        <label>Name</label>
                        <Field className="form-control"
                               type="text"
                               name="name"
                               placeholder="Name"/>
                        <ErrorMessage className="text-danger" name="name" component="div"/>
                    </fieldset>
                    <fieldset className="from-group m-3">
                        <label>Description</label>
                        <Field className="form-control"
                               type="text"
                               as="textarea"
                               name="description"
                               placeholder="Description"/>
                        <ErrorMessage className="text-danger" name="description" component="div"/>
                    </fieldset>
                    <div className="btn-group my-2">
                        <button type="submit" className="btn btn-primary" disabled={!isValid || isSubmitting}>
                            Save
                        </button>
                    </div>
                </Form>
            )}
        </Formik>
    );
}

const validate = (values) => {
    let errors = {};
    if (!values.name) {
        errors.name = 'Name is required!';
    } else if (!values.name || values.name.length > 150 || values.name.length < 5) {
        errors.name = 'Name should be between min 5 and max 150 characters!';
    }
    if (!values.description) {
        errors.description = 'Description is required!';
    } else if (values.description.length > 500 || values.description.length < 5) {
        errors.description = 'Description should be between min 5 and max 500 characters!';
    }
    return errors;
}