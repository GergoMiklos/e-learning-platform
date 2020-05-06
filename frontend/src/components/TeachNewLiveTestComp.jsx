import React, { Component } from 'react'

class TeachNewLiveTestComp extends Component {
    constructor(props){
        super(props)
        this.state = {
            tests: [{id: 1, name: "Test 1", description: "hellóbeló g1"},
                {id: 2, name: "Test 2 (Nehéz)", description: "kobaka g2"}]
        }
    }

    render() {
        return (
            <div className="container">

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-10">Add Test</h1>
                </div>

                <div className="row my-3">
                    <table className="col-12 table table-striped rounded shadow">
                        <tbody>
                        {
                            this.state.tests.map(
                                test =>
                                    <tr>
                                        <td>
                                            <div className="container">
                                                <div className="row">
                                                    <strong className="col-10">{test.name}</strong>
                                                    <button className="col-2 btn btn-warning btn" onClick={() => this.addTest(test.id)}>
                                                        Add
                                                    </button>
                                                </div>
                                            </div>
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


    addTest = (id) => {
        this.props.history.push(`/teach/group/${this.props.match.params.groupid}`)
    }


}

export default TeachNewLiveTestComp;