import React, {Component} from 'react'
import {ErrorMessage, Field, Form, Formik} from 'formik'
import gql from "graphql-tag";
import client from "../ApolloClient";
import toaster from "toasted-notes";

const EDIT_GROUP = gql`
    mutation EditGroup($groupId: ID!, $name: String!, $description: String!) {
        editGroup(groupId: $groupId, name: $name, description: $description) {
            id
        }
    }`;

class EditGroupDetailsComp extends Component {
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

    onSubmit = (values, {resetForm}) => {
        client.mutate({
            mutation: EDIT_GROUP,
            variables: {name: values.name, description: values.description, groupId: this.props.groupId},
        })
            .then(() => {
                this.showNotification({
                    text: 'Group details edited successfully',
                    type: 'success',
                })
            })
            .catch(errors => {
                console.log(errors);
                resetForm({name: this.props.name, description: this.props.description});
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
            <Formik
                initialValues={{name: this.props.name, description: this.props.description}}
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
                                   placeholder="Name"
                            />
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
                        </div>
                    </Form>
                )}
            </Formik>
        );
    }

}

export default EditGroupDetailsComp;