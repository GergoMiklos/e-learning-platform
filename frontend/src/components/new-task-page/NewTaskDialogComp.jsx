import React from 'react';
import {ErrorMessage, Field, Form, Formik} from 'formik';
import {Modal} from "react-bootstrap";
import PropTypes from "prop-types";


export default function NewTaskDialogComp({onSubmit, onValidate, onNavigateBack}) {

    return (
        <Modal
            centered
            onHide={() => onNavigateBack()}
            show={true}
        >
            <div className="container">
                <div className="row bg-primary text-light shadow p-3">
                    <h1 className="col-10">Create Task</h1>
                </div>

                <Formik
                    initialValues={{question: '', correct: '', bad1: '', bad2: '', bad3: ''}}
                    onSubmit={onSubmit}
                    validateOnChange={true}
                    validateOnBlur={true}
                    validate={onValidate}
                    enableReinitialize={true}
                >
                    {({isValid, isSubmitting}) => (
                        <Form>
                            <fieldset className="from-group m-3">
                                <label>Question</label>
                                <Field className="form-control"
                                       as="textarea"
                                       type="text"
                                       name="question"
                                       placeholder="Question"/>
                            </fieldset>
                            <fieldset className="from-group m-3">
                                <label>Correct answer:</label>
                                <Field className="form-control"
                                       type="text"
                                       name="correct"
                                       placeholder="The correct answer"/>
                            </fieldset>
                            <fieldset className="from-group m-3">
                                <label>Incorrect answers:</label>
                                <Field className="form-control"
                                       type="text"
                                       name="bad1"
                                       placeholder="Incorrect answer 1"/>
                                <Field className="form-control my-2"
                                       type="text"
                                       name="bad2"
                                       placeholder="Incorrect answer 2"/>
                                <Field className="form-control"
                                       type="text"
                                       name="bad3"
                                       placeholder="Incorrect answer 3"/>
                                <ErrorMessage className="text-danger" name="question" component="div"/>
                            </fieldset>
                            <div className="btn-group my-2">
                                <button
                                    type="submit" className="btn btn-primary"
                                    disabled={!isValid || isSubmitting}
                                >
                                    Save
                                </button>
                            </div>
                        </Form>
                    )}
                </Formik>
            </div>
        </Modal>
    );
}

NewTaskDialogComp.propTypes = {
    onSubmit: PropTypes.func.isRequired,
    onValidate: PropTypes.bool.isRequired,
    onNavigateBack: PropTypes.func.isRequired,
}