import React, { Component } from 'react'

class TeachListComp extends Component {
    constructor(props){
        super(props)
        this.state = {
            hideNews : false,
            groups : [
                {id: 1, description: 'Learn React', name: 'My Group 1'},
                {id: 2, description: 'Learn about India', name: 'My Group 2'},
                {id: 3, description: 'Learn Dance', name: 'My Group 3'}
            ],
            tests: [{id: 1, name: "Test 1", description: "hellóbeló g1"},
                    {id: 2, name: "Test 2 (Nehéz)", description: "kobaka g2"}]
        }
    }

    render() {
        return (
            <div className="container">
                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">My Groups</h1>
                    <button className="col-2 btn btn-primary" onClick={() => this.newGroup()}>
                        New
                    </button>
                </div>

                <div className="row my-3">
                    <table className="col-12 table table-striped table-hover rounded shadow">
                        <tbody>
                        {
                            this.state.groups.map(
                                group =>
                                    <tr onClick={() => this.groupClicked(group.id)}>
                                        <td>
                                            <strong>{group.name}</strong>
                                            <div>{group.description}</div>
                                        </td>
                                    </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div>

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">My Tests</h1>
                    <button className="col-2 btn btn-primary" onClick={() => this.newTest()}>
                        New
                    </button>
                </div>

                <div className="row my-3">
                    <table className="col-12 table table-striped table-hover rounded shadow">
                        <tbody>
                        {
                            this.state.tests.map(
                                test =>
                                    <tr onClick={() => this.testClicked(test.id)}>
                                        <td>
                                            <strong>{test.name}</strong>
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

    newGroup = () => {
        this.props.history.push(`/teach/group/new`)
    }

    newTest = () => {
        this.props.history.push(`/teach/test/new`)
    }

    groupClicked = (id) => {
        this.props.history.push(`/teach/group/${id}`)
    }

    testClicked = (id) => {
        this.props.history.push(`/teach/test/${id}/edit`)
    }


}

export default TeachListComp;