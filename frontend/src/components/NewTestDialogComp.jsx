import React, {Component} from 'react'
import {Formik, Form, Field, ErrorMessage} from 'formik'
import gql from "graphql-tag";
import client from "../ApolloClient";
import AuthenticationService from "../AuthenticationService";
import {Modal} from "react-bootstrap";
import toaster from "toasted-notes";

const CREATE_TEST = gql`
    mutation CreateTest($groupId: ID!, $name: String!, $description: String!) {
        createTest(groupId: $groupId, name: $name, description: $description) {
            name
        }
    }`;

class NewTestDialogComp extends Component {
    constructor(props) {
        super(props);
    }

    validate = (values) => {
        let errors = {};
        if (!values.name) {
            errors.name = 'Name is required!';
        } else if (!values.name || values.name.toString().length > 50 || values.name.toString().length < 5) {
            errors.name = 'Name should be between min 5 and max 50 characters!';
        }
        if (!values.description) {
            errors.description = 'Description is required!';
        } else if (values.description.toString().length > 500 || values.description.toString().length < 5) {
            errors.description = 'Description should be between min 5 and max 500 characters!';
        }
        return errors;
    }

    onSubmit = (values) => {
        client.mutate({
            mutation: CREATE_TEST,
            variables: {name: values.name, description: values.description, groupId: this.props.groupId},
        })
            .then(() => {
                this.showNotification({
                    text: 'Test created successfully',
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
                onHide = {() => this.props.onHide()}
            >
                <div className="container">

                    <div className="row bg-primary text-light shadow p-3">
                        <h1 className="col-10">New Test</h1>
                    </div>

                    <Formik
                        initialValues={{name: '', description: ''}}
                        onSubmit={this.onSubmit}
                        validateOnChange={true}
                        validateOnBlur={true}
                        validate={this.validate}
                        enableReinitialize={true}
                    >
                        {({isValid}) => (
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
                                               name="description"
                                               placeholder="Description"/>
                                        <ErrorMessage className="text-danger" name="description" component="div"/>
                                    </fieldset>
                                    <div className="btn-group my-2">
                                        <button type="submit" className="btn btn-primary" disabled={!isValid}>
                                            Save
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

export default NewTestDialogComp;