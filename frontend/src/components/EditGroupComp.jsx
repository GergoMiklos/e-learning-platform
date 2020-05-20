import React, { Component } from 'react'
import {ErrorMessage, Field, Form, Formik} from "formik";
import gql from "graphql-tag";
import client from "../ApolloClient";
import AuthenticationService from "../AuthenticationService";

const GROUP = gql`
    query Group($id: ID!) {
        group(id: $id) {
            id
            name
            description
            admins {
                id
                name
                code
            }
            users {
                id
                name
                code
            }
        }
    }`;

const EDITGROUP = gql`
    mutation EditGroup($id: ID!, $name: String!, $description: String!) {
        editGroup(id: $id, name: $name, description: $description) {
            id
            name
            description
        }
    }`;

const DELETEUSER = gql`
    mutation DeleteUserFromGroup($userId: ID!, $groupId: ID!) {
        deleteUserFromGroup(userId: $userId, groupId: $groupId)
    }`;

const DELETEADMIN = gql`
    mutation DeleteAdminFromGroup($userId: ID!, $groupId: ID!) {
        deleteAdminFromGroup(userId: $userId, groupId: $groupId)
    }`;

const ADDADMIN = gql`
    mutation AddAdminFromCodeToGroup($groupId: ID!, $userCode: String!) {
        addAdminFromCodeToGroup(groupId: $groupId, userCode: $userCode)  {
            name
        }
    }`;

const ADDUSER = gql`
    mutation AddUserFromCodeToGroup($groupId: ID!, $userCode: String!) {
        addUserFromCodeToGroup(groupId: $groupId, userCode: $userCode)  {
            name
        }
    }`;

class EditGroupComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            group: null,
            error: null,
            success: null,
            addCode: ''
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
            mutation: EDITGROUP,
            variables: {name: values.name, description: values.description, id: this.state.group.id}
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

    navigateBack = () => {
        this.props.history.push(`/teach/group/${this.props.match.params.groupid}`)
    }

    componentDidMount() {
        this.refreshGroup();
    }

    deleteUser = (userId) => {
        client.mutate({
            mutation: DELETEUSER,
            variables: {userId: userId, groupId: this.state.group.id}
        })
            .then(result => {
                console.log(result);
                this.setState({success: 'Deleted!', error: null});
                this.refreshGroup();
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: 'Error!', success: null});
            })
    }

    deleteAdmin = (userId) => {
        client.mutate({
            mutation: DELETEADMIN,
            variables: {userId: userId, groupId: this.state.group.id}
        })
            .then(result => {
                console.log(result);
                this.setState({success: 'Deleted!', error: null});
                this.refreshGroup();
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: 'Error!', success: null});
            })
    }

    handleInputChange = (event) => {
        this.setState(
            {
                addCode : event.target.value.toUpperCase().slice(0, 8)
            }
        );
    }

    addUser = () => {
        if(!this.state.addCode)
            return;
        client.mutate({
            mutation: ADDUSER,
            variables: {groupId: this.state.group.id, userCode: this.state.addCode}
        })
            .then(result => {
                console.log(result);
                if(result.data.addUserFromCodeToGroup) {
                    this.setState({success: 'User added: ' + result.data.addUserFromCodeToGroup.name, error: null, addCode: ''});
                    this.refreshGroup();
                }
                else {
                    this.setState({error: 'No user with code: ' + this.state.addCode, success: null});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: 'Error', success: null});
            })
    }

    addAdmin = () => {
        if(!this.state.addCode)
            return;
        client.mutate({
            mutation: ADDADMIN,
            variables: {groupId: this.state.group.id, userCode: this.state.addCode}
        })
            .then(result => {
                console.log(result);
                if(result.data.addAdminFromCodeToGroup) {
                    this.setState({success: 'Admin added: ' + result.data.addAdminFromCodeToGroup.name, error: null, addCode: ''});
                    this.refreshGroup();
                }
                else {
                    this.setState({error: 'No user with code: ' + this.state.addCode, success: null});
                }
            })
            .catch(errors => {
                console.log(errors);
                this.setState({error: 'Error', success: null});
            })
    }


    refreshGroup = () => {
        client
            .query({
                query: GROUP,
                variables: {id: this.props.match.params.groupid},
                fetchPolicy: 'network-only'
            })
            .then(result => {
                console.log(result);
                if(!result.data.group) {
                    console.log("GraphQL query no result");
                } else {
                    this.setState({group: result.data.group});
                }
            })
            .catch(errors => {
                console.log(errors);
            });
    }

    render() {
        if(!this.state.group) {
            return (<div></div>)
        }
        let name = this.state.group.name;
        let description = this.state.group.description;
        return (
            <div className="container">
                <button className="row btn btn-secondary mt-1" onClick={() => this.navigateBack()}>
                    Back
                </button>

                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-auto">Edit Group</h1>
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

                                <fieldset className="from-group my-1">
                                    <label>Name</label>
                                    <Field className="form-control" type="text" name="name" placeholder="Name"></Field>
                                </fieldset>
                                <fieldset className="from-group my-1">
                                    <label>Description</label>
                                    <Field className="form-control" type="text" name="description" placeholder="Description"></Field>
                                </fieldset>

                                <button type="submit" className="btn btn-primary my-1">Save</button>
                            </Form>
                        )
                    }
                </Formik>

                <div className="row input-group mt-5 p-3">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Add: </span>
                    </div>
                    <input type="text" className="form-control" placeholder="USER CODE" value={this.state.addCode} onChange={this.handleInputChange}/>
                    <div className="input-group-append">
                        <button className="btn btn-primary" onClick={() => this.addUser()}>
                            User
                        </button>
                        <button className="btn btn-outline-secondary" onClick={() => this.addAdmin()}>
                            Admin
                        </button>
                    </div>
                </div>

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Admins</h1>
                </div>

                {this.state.group.admins &&
                <div className="row my-3">
                    <table className="table table-sm table-striped rounded shadow">
                        <tbody>
                        {
                            this.state.group.admins.map(
                                user =>
                                    <tr key={user.id} className=" ">
                                        <td className="text-left">
                                            <strong className="ml-2">{user.name}</strong>
                                        </td>
                                        <td className="text-center">
                                            <div>{user.code}</div>
                                        </td>
                                        <td className="text-right">
                                            <button className="btn btn-danger btn-sm" onClick={() => this.deleteAdmin(user.id)}>
                                                Delete
                                            </button>
                                        </td>
                                    </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div>}

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">Users</h1>
                </div>

                {this.state.group.users &&
                <div className="row my-3">
                    <table className="table table-sm text-center table-striped rounded shadow">
                        <tbody>
                        {
                            this.state.group.users.map(
                                user =>
                                    <tr key={user.id} >
                                        <td className="text-left">
                                            <strong className="ml-2">{user.name}</strong>
                                        </td>
                                        <td className="text-center">
                                            <div>{user.code}</div>
                                        </td>
                                        <td className="text-right">
                                            <button className="btn btn-danger btn-sm" onClick={() => this.deleteUser(user.id)}>
                                                Delete
                                            </button>
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

    testClicked = (id) => {
        console.log("Test " + id + " clicked!");
    }

}

export default EditGroupComp;