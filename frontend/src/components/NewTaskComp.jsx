import React, { Component } from 'react'
import {ErrorMessage, Field, Form, Formik} from "formik";

class NewTaskComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            task:                 {
                id: 2,
                question: 'Mi a jó már megint?',
                answers: ['A válasz', 'B válasz', 'C válasz', 'Dddd válasz']
            }
        };
    }

    validate = (values) => {
        let errors = {}
        if(!values.question || !values.bad1 || !values.bad2|| !values.bad3) {
            errors.question = 'Fill all fields!'
        }
        return errors
    }

    onSubmit = (values) => {

    }

    render() {
        return (
            <div className="container">
                <div className="row rounded bg-primary text-light shadow my-3 p-3">
                    <h1 className="col-10">New Task</h1>
                </div>

                <Formik
                    initialValues={{question: '', correct: '', bad1: '', bad2: '', bad3: ''}}
                    onSubmit={this.onSubmit}
                    validateOnChange={false}
                    validateOnBlur={true}
                    validate={this.validate}
                    enableReinitialize={true}
                >
                    {
                        (props) => (
                            <Form>
                                <ErrorMessage name="question" component="div" className="alert alert-warning"></ErrorMessage>

                                <fieldset className="from-group">
                                    <label>Correct Answear:</label>
                                    <Field className="form-control" type="text" name="question" ></Field>
                                </fieldset>
                                <fieldset className="from-group">
                                    <label>Incorrect Answears:</label>
                                    <Field className="form-control" type="text" name="bad1" ></Field>
                                </fieldset>
                                <fieldset className="from-group">
                                    <Field className="form-control" type="text" name="bad2" ></Field>
                                </fieldset>
                                <fieldset className="from-group">
                                    <Field className="form-control" type="text" name="bad3" ></Field>
                                </fieldset>
                                <div className="btn-group my-2">
                                    <button type="submit" className="btn btn-primary">Save</button>
                                    <button className="btn btn-light">Back</button>
                                </div>
                            </Form>
                        )
                    }
                </Formik>
            </div>
        );
    }

    testClicked = (id) => {
        console.log("Test " + id + " clicked!");
    }

}

export default NewTaskComp;