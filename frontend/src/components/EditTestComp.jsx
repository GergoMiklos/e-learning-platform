import React, { Component } from 'react'
import {ErrorMessage, Field, Form, Formik} from "formik";
import gql from "graphql-tag";
import client from "../ApolloClient";

const TEST = gql`
    query Test($id: ID!) {
        test(id: $id) {
            id
            name
            description
            tasks {
                id
                question
                answers
                level
            }
        }
    }`;

class EditTestComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            selected: null,
            test: null
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

    componentDidMount() {
        this.refreshTest();
    }

    refreshTest = () => {
        client
            .query({
                query: TEST,
                variables: {id: this.props.match.params.testid}
            })
            .then(result => {
                console.log(result);
                if(!result.data.test) {
                    console.log("GraphQL query no result");
                } else {
                    this.setState({test: result.data.test});
                }
            })
            .catch(errors => {
                console.log(errors);
            });
    }

    render() {
        if(!this.state.test) {
            return (<div></div>)
        }
        let name = this.state.test.name;
        let description = this.state.test.description;
        return (
            <div className="container">
                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Edit test</h1>
                </div>

                <Formik
                    initialValues={{name: name, description: description}}
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
                    <h1 className="col-10">Tasks</h1>
                    <button className="col-2 btn btn-warning btn" onClick={() => this.newTask()}>New</button>
                </div>

                {this.state.test.tasks &&
                <div className="row my-3">
                    <table className="col-12 table table-striped rounded shadow">
                        <tbody>
                        {
                            this.state.test.tasks.map(
                                task =>
                                    <tr key={task.id} onClick={() => this.taskClicked(task.id)}>
                                        <td>
                                            <div className="container">
                                                <div className="row">
                                                    <strong className="col-10">{task.question}</strong>
                                                    <button className="col-2 btn btn-danger btn">Delete</button>
                                                </div>
                                                {(this.state.selected === task.id) && task.answers.map(
                                                    answer =>
                                                        <div className="row">{answer}</div>
                                                )

                                                }
                                            </div>
                                        </td>
                                    </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div>}
            </div>
        );
    }

    taskClicked = (id) => {
        console.log("Test " + id + " clicked!");
        if(this.state.selected === id) {
            this.setState({selected: null});
        } else {
            this.setState({selected: id});
        }
    }

    newTask= () => {
        this.props.history.push(`/teach/test/${this.state.test.id}/tasks`)
    }

}

export default EditTestComp;