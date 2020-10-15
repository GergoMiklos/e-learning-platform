import React from 'react'
import {ErrorMessage, Field, Form, Formik} from 'formik'
import gql from "graphql-tag";
import {Modal} from "react-bootstrap";
import toast from "toasted-notes";
import {useMutation} from "@apollo/client";
import {useHistory} from "react-router-dom";

const CREATE_TASK_MUTATION = gql`
    mutation CreateTask($taskInput: TaskInput!) {
        createTask(taskInput: $taskInput) {
            question
        }
    }`;

export default function NewTaskDialogComp(props) {
    let history = useHistory();

    const [createTask] = useMutation(CREATE_TASK_MUTATION, {
        onCompleted: (data) => {
            toast.notify(`Task created: ${data.createTask.question}`);
            history.goBack();
        },
        onError: () => toast.notify(`Error :(`),
    });

    return (
        <Modal
            centered
            onHide={() => history.goBack()}
            show={true}
        >
            <div className="container">
                <div className="row bg-primary text-light shadow p-3">
                    <h1 className="col-10">Create Task</h1>
                </div>

                <Formik
                    initialValues={{question: '', correct: '', bad1: '', bad2: '', bad3: ''}}
                    onSubmit={values => createTask({
                            variables: {
                                taskInput: {
                                    question: values.question,
                                    correctAnswer: values.correct,
                                    incorrectAnswers: [values.bad1, values.bad2, values.bad3]
                                }
                            }
                        }
                    )}
                    validateOnChange={true}
                    validateOnBlur={true}
                    validate={(values) => {
                        let errors = {};
                        [values.question, values.correct, values.bad1, values.bad2, values.bad3].forEach(field => {
                            if (!field || field.toString().length > 250 || field.toString().length < 1) {
                                errors.question = 'All fields should be between min 1 and max 250 characters!';
                            }
                        })
                        return errors;
                    }}
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