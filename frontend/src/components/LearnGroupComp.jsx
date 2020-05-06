import React, { Component } from 'react'

class LearnGroupComp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            group: {
                id: 1, description: 'Learn React', name: 'Group 1', code: "L54FVJ2",
                news: {id: 2, text: "kobaka g2"},
                tests: [{id: 1, name: "Test 1", text: "hellóbeló g1"},
                    {id: 2, name: "Test 2 (Nehéz)", description: "kobaka g2"}]
            }
        };
    }


    render() {
        return (
            <div className="container">
                <div className="row rounded shadow my-3 p-3">
                    <h3 className="col-12">{this.state.group.name}</h3>
                    <h5 className="col-10">({this.state.group.code})</h5>
                    <button className="col-2 btn btn-warning btn">Leave</button>
                    {this.state.group.description}
                </div>

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">News</h1>
                </div>

                <div className="row alert alert-warning my-3">
                    {this.state.group.news.text}
                </div>

                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">Tests</h1>
                </div>

                <div className="row my-3">
                    <table className="col-12 table table-striped table-hover rounded shadow">
                        <tbody>
                        {
                            this.state.group.tests.map(
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

    testClicked = (id) => {
        console.log("Test " + id + " clicked!");
        this.props.history.push(`/learn/group/${this.props.match.params.groupid}/test/${id}`)
    }

}

export default LearnGroupComp;