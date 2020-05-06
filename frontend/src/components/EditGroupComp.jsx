import React, { Component } from 'react'
import {ErrorMessage, Field, Form, Formik} from "formik";

class TeachGroupComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            group: {
                id: 1, description: 'Learn React', name: 'Group 1', code: 'ASD123',
                news: [{id: 1, description: "hellóbeló g1"}, {id: 2, description: "kobaka g2"}],
                tests: [{id: 1, name: "Test 1", description: "hellóbeló g1"},
                    {id: 2, name: "Test 2 (Nehéz)", description: "kobaka g2"}],
                users: [{id: 1, name: "Aladár Béla", code: "ASD123"},
                    {id: 2, name: "Szirmai Réka", code: "VFG78D"},
                    {id: 3, name: "Szirmai CSóka", code: "RFGCCC"}],
                admins: [{id: 1, name: "Aladár Béla", code: "ASD123"},
                    {id: 2, name: "Szirmai Réka", code: "VFG78D"}]
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
                    <h1 className="col-10">Edit group</h1>
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

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Admins</h1>
                </div>

                <div className="row input-group mb-3">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Add admin</span>
                    </div>
                    <input type="text" className="form-control" placeholder="USER CODE"/>
                    <div className="input-group-append">
                        <button className="btn btn-outline-secondary">Add</button>
                    </div>
                </div>

                <div className="row my-3">
                    <table className="col-12 table table-striped rounded shadow">
                        <tbody>
                        {
                            this.state.group.users.map(
                                user =>
                                    <tr>
                                        <td>
                                            <strong>{user.name}</strong>
                                        </td>
                                        <td>
                                            <div>{user.code}</div>
                                        </td>
                                        <td>
                                            <button className="btn btn-danger">Delete</button>
                                        </td>
                                    </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div>

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">Users</h1>
                </div>

                <div className="row input-group mb-3">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Add user</span>
                    </div>
                    <input type="text" className="form-control" placeholder="USER OR GROUP CODE"/>
                    <div className="input-group-append">
                        <button className="btn btn-outline-secondary">Add</button>
                    </div>
                </div>

                <div className="row my-3">
                    <table className="col-12 table table-striped rounded shadow">
                        <tbody>
                        {
                            this.state.group.users.map(
                                user =>
                                    <tr>
                                        <td>
                                            <strong>{user.name}</strong>
                                        </td>
                                        <td>
                                            <div>{user.code}</div>
                                        </td>
                                        <td>
                                            <button className="btn btn-danger">Delete</button>
                                        </td>
                                    </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }

    testClicked = (id) => {
        console.log("Test " + id + " clicked!");
    }

}

export default TeachGroupComp;