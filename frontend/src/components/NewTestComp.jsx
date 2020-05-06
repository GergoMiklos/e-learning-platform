import React, { Component } from 'react'
import {ErrorMessage, Field, Form, Formik} from "formik";

class NewTestComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            test: {
                id: 1, description: 'Learn React', name: 'Group 1'
            }
        };
    }

    validate = (values) => {
        let errors = {}
        if(!values.name) {
            errors.name = 'Enter a name!'
        }
        else if(!values.description) {
            errors.description = 'Enter a description!'
        }
        return errors
    }

    onSubmit = (values) => {

    }


    render() {
        return (
            <div className="container">
                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">New test</h1>
                </div>

                <Formik
                    initialValues={{name: '', description: ''}}
                    onSubmit={this.onSubmit}
                    validateOnChange={false}
                    validateOnBlur={true}
                    validate={this.validate}
                    enableReinitialize={true}
                >
                    {
                        (props) => (
                            <Form>
                                <ErrorMessage name="name" component="div" className="alert alert-warning"></ErrorMessage>
                                <ErrorMessage name="description" component="div" className="alert alert-warning"></ErrorMessage>

                                <fieldset className="from-group">
                                    <label>Name</label>
                                    <Field className="form-control" type="text" name="name" placeholder="Name"></Field>
                                </fieldset>
                                <fieldset className="from-group">
                                    <label>Description</label>
                                    <Field className="form-control" type="text" name="description" placeholder="Description"></Field>
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

}

export default NewTestComp;