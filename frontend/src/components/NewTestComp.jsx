import React, { Component } from 'react'
import {ErrorMessage, Field, Form, Formik} from "formik";
import gql from "graphql-tag";
import AuthenticationService from "../AuthenticationService";
import client from "../ApolloClient";

const CREATETEST = gql`
    mutation CreateTest($groupId: ID!, $name: String!, $description: String!) {
        createTest(groupId: $groupId, name: $name, description: $description) {
            id
            name
            description
        }
    }`;

class NewTestComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            error: null
        };
    }

    validate = (values) => {
        let errors = {}
        if(!values.name) {
            errors.name = 'Enter a name!'
        } else if(values.name.toString().length > 50 || values.name.toString().length < 5) {
            errors.name = 'Enter a name has min 5 and max 50 characters!'
        } else if(!values.description) {
            errors.description = 'Enter a description!'
        } else if(values.name.toString().length > 150 || values.name.toString().length < 5) {
            errors.description = 'Enter a description has min 5 and max 150 characters!'
        }
        return errors
    }

    onSubmit = (values) => {
        let userId = AuthenticationService.getUserId();
        client.mutate({
            mutation: CREATETEST,
            variables: {name: values.name, description: values.description, groupId: this.props.match.params.groupid}
        })
            .then(result => {
                console.log(result);
                this.goBack();
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: 'Error!'});
            });
    }

    goBack = () => {
        this.props.history.push(`/teach/group/${this.props.match.params.groupid}`)
    }

    render() {
        return (
            <div className="container">
                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-10">New Test</h1>
                </div>

                {this.state.error && <div class="alert alert-danger">{this.state.error}</div>}

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
                                    <button className="btn btn-light" onClick={() => this.goBack()}>
                                        Back
                                    </button>                                </div>
                            </Form>
                        )
                    }
                </Formik>
            </div>
        );
    }

}

export default NewTestComp;