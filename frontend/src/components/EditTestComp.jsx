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

const EDITTEST = gql`
    mutation EditTest($id: ID!, $name: String!, $description: String!) {
        editTest(id: $id, name: $name, description: $description) {
            id
            name
            description
        }
    }`;

const DELETETASK = gql`
    mutation DeleteTaskFromTest($testTaskId: ID!) {
        deleteTaskFromTest(testTaskId: $testTaskId)
    }`;

class EditTestComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            test: null,
            selected: null,
            error: null,
            success: null
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
        client.mutate({
            mutation: EDITTEST,
            variables: {name: values.name, description: values.description, id: this.state.test.id}
        })
            .then(result => {
                console.log(result);
                this.setState({success: 'Saved!', error: null});
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: 'Error!'});
            })
    }

    deleteTask = (taskId) => {
        client.mutate({
            mutation: DELETETASK,
            variables: {testTaskId: taskId}
        })
            .then(result => {
                console.log(result);
                this.setState({success: 'Deleted!', error: null});
                this.refreshTest();
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: 'Error!', success: null});
            })
    }

    navigateBack = () => {
        this.props.history.push(`/teach/group/${this.props.match.params.groupid}`)
    }

    taskClicked = (id) => {;
        if(this.state.selected === id) {
            this.setState({selected: null});
        } else {
            this.setState({selected: id});
        }
    }

    newTask= () => {
        this.props.history.push(`/teach/test/${this.state.test.id}/tasks`)
    }

    componentDidMount() {
        this.refreshTest();
    }

    refreshTest = () => {
        client
            .query({
                query: TEST,
                variables: {id: this.props.match.params.testid},
                fetchPolicy: 'network-only'
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
                <button className="row btn btn-secondary mt-1" onClick={() => this.navigateBack()}>
                    Back
                </button>

                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-auto">Edit test</h1>
                </div>

                {this.state.error && <div className="alert alert-danger">{this.state.error}</div>}
                {this.state.success && <div className="alert alert-success">{this.state.success}</div>}

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

                                <button type="submit" className="btn btn-primary my-1">Save</button>
                            </Form>
                        )
                    }
                </Formik>

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Tasks</h1>
                    <button className="col-2 btn btn-primary btn" onClick={() => this.newTask()}>New</button>
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
                                                    {(this.state.selected === task.id) &&
                                                    <button className="col-2 btn btn-danger" onClick={() => this.deleteTask(task.id)}>
                                                        Delete
                                                    </button>}
                                                </div>
                                                {(this.state.selected === task.id) && task.answers.map(
                                                    (answer, i) =>
                                                        <div key={i} className="row">{answer}</div>
                                                )}
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

}

export default EditTestComp;