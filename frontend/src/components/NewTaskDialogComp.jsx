import React, {Component} from 'react'
import {Formik, Form, Field, ErrorMessage} from 'formik'
import gql from "graphql-tag";
import client from "../ApolloClient";
import {Modal} from "react-bootstrap";
import toaster from "toasted-notes";

const CREATE_TASK = gql`
    mutation CreateTask($taskInput: TaskInput!) {
        createTask(taskInput: $taskInput) {
            id
        }
    }`;

const ADD_TASK = gql`
    mutation AddTaskToTest($testId: ID!, $taskId: ID!, $level: Int) {
        addTaskToTest(testId: $testId, taskId: $taskId, level: $level) {
            id
        }
    }`;

class NewTaskDialogComp extends Component {
    constructor(props) {
        super(props);
    }

    validate = (values) => {
        let errors = {};
        [values.question, values.correct, values.bad1, values.bad2, values.bad3].forEach(field => {
            if (!field || field.toString().length > 250 || field.toString().length < 1) {
                errors.question = 'All fields should be between min 1 and max 250 characters!';
            }
        })
        return errors;
    }

    onSubmit = (values) => {
        this.createTask({
                question: values.question,
                correctAnswer: values.correct,
                incorrectAnswers: [values.bad1, values.bad2, values.bad3]
            }
        );
    }

    createTask = (taskInput) => {
        client.mutate({
            mutation: CREATE_TASK,
            variables: {taskInput: taskInput},
        })
            .then((result) => {
                if (result.data && result.data.createTask) {
                    this.addTaskToTest(result.data.createTask.id)
                } else {
                    this.showNotification({text: 'Something went wrong', type: 'danger'});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'});
            })
    }


    addTaskToTest = (taskId) => {
        client.mutate({
            mutation: ADD_TASK,
            variables: {taskId: taskId, testId: this.props.testId, level: 0},
        })
            .then(() => {
                this.showNotification({
                    text: 'Task added successfully',
                    type: 'success',
                })
                this.props.onHide();
            })
            .catch(errors => {
                console.log(errors);
                this.showNotification({text: 'Something went wrong', type: 'danger'});
            })
    }

    showNotification = ({text, type}) => {
        toaster.notify(() => (
            <div className={`alert alert-${type}`}>
                {text}
            </div>
        ))
    }

    render() {
        return (
            <Modal
                show={this.props.show}
                centered
                onHide={() => this.props.onHide()}
            >
                <div className="container">

                    <div className="row bg-primary text-light shadow p-3">
                        <h1 className="col-10">New Task</h1>
                    </div>

                    <Formik
                        initialValues={{question: '', correct: '', bad1: '', bad2: '', bad3: ''}}
                        onSubmit={this.onSubmit}
                        validateOnChange={true}
                        validateOnBlur={true}
                        validate={this.validate}
                        enableReinitialize={true}
                    >
                        {({isValid}) => (
                            <Form>
                                <fieldset className="from-group m-3">
                                    <label>Question</label>
                                    <Field className="form-control"
                                           type="text"
                                           name="question"
                                           placeholder="Question"/>
                                </fieldset>
                                <fieldset className="from-group m-3">
                                    <label>Correct answer:</label>
                                    <Field className="form-control"
                                           type="text"
                                           name="correct"
                                           placeholder="the correct answer"/>
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
                                    <button type="submit" className="btn btn-primary" disabled={!isValid}>
                                        Save & Add
                                    </button>
                                    <button className="btn btn-light" onClick={() => this.props.onHide()}>
                                        Back
                                    </button>
                                </div>
                            </Form>
                        )}
                    </Formik>
                </div>
            </Modal>
        );
    }

}

export default NewTaskDialogComp;