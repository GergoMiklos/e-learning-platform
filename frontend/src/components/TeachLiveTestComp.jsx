import React, { Component } from 'react';
import gql from "graphql-tag";
import client from "../ApolloClient";

// const state = {
//     NOT_STARTED: "Not started",
//     IN_PROGRESS: "In Progress",
//     PROBLEM: "Problem",
//     WARNING: "Warning",
//     FINISHED: "Finished"
// }

const status = {
    NOT_STARTED: "secondary",
    IN_PROGRESS: "success",
    PROBLEM: "danger",
    INACTIVE: "warning",
    FINISHED: "info",
};

class TeachLiveTestComp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            liveTest: {
                test: {name: "Maths 2.A - 1. practice", discription: "Addition, subtraction and multiplication revision"},
                liveTestStates: [
                    {id: 1, state: status.IN_PROGRESS, user: {name: "Elton John", code: "AE50TVR9"}},
                    {id: 2, state: status.NOT_STARTED, user: {name: "BoJack HorseMan", code: "B5AST6CV"}},
                    {id: 3, state: status.IN_PROGRESS, user: {name: "Gyulai Levente", code: "SHI0RFCD"}},
                    {id: 4, state: status.IN_PROGRESS, user: {name: "Jack Joe", code: "O25PT8R9"}},
                    {id: 5, state: status.PROBLEM, user: {name: "Elizabeth Samanta", code: "PE53TKC4"}},
                    {id: 6, state: status.INACTIVE, user: {name: "Vladimir Putin", code: "AT55T872"}},
                    {id: 7, state: status.FINISHED, user: {name: "Donald Trump John", code: "LE50TL23"}},
                    {id: 4, state: status.IN_PROGRESS, user: {name: "Szirmai Csilla", code: "CXSPT9R9"}},
                    {id: 4, state: status.IN_PROGRESS, user: {name: "Szirmai Rozália", code: "555PTQR9"}},
                    {id: 4, state: status.IN_PROGRESS, user: {name: "Szirmai Kinga", code: "RET78R3S"}},
                ]
            }
        };
    }

    navigateBack = () => {
        this.props.history.push(`/teach/group/${this.props.match.params.groupid}`);
    }

    componentDidMount() {

    }

    render() {
        if(!this.state.liveTest) {
            return (<div/>);
        }
        return (
            <div className="container">
                <button className="row btn btn-secondary mt-1" onClick={() => this.navigateBack()}>
                    Back
                </button>

                <div className="row bg-primary text-light rounded shadow my-3 p-3">
                    <h1 className="col-10">{this.state.liveTest.test.name}</h1>
                </div>

                <div className="btn-group btn-block">
                    <div className="btn btn-sm btn-secondary disabled">Not Started</div>
                    <div className="btn btn-sm btn-success disabled">In Progress</div>
                    <div className="btn btn-sm btn-warning disabled">Inactive</div>
                    <div className="btn btn-sm btn-danger disabled">Problem</div>
                </div>

                <div className="row my-3">
                    <table className="col-12 table table-striped rounded shadow">
                        <thead>
                            <th>Name</th>
                            <th className="text-center">Code</th>
                            <th className="text-center">Started</th>
                            <th className="text-center">State</th>
                        </thead>
                        <tbody>
                        {
                            this.state.liveTest.liveTestStates.map(
                                lts =>
                                    <tr key={lts.id}>
                                        <td className="font-weight-bold">
                                            {lts.user.name}
                                        </td>
                                        <td className="text-center">
                                            {lts.user.code}
                                        </td>
                                        <td className="text-center">
                                                {lts.state == status.NOT_STARTED && 'Not Started' || Math.floor(Math.random() * 20)+10 + ' mins ago'}
                                        </td>
                                        <td className="text-center">
                                            <strong className={`col-12 rounded-pill bg-${lts.state} btn disabled text-center p-1`}>
                                                {lts.state == status.NOT_STARTED && 'Not Started' || Math.floor(Math.random() * 10) + ' mins'}
                                            </strong>
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

}

export default TeachLiveTestComp;