import React, { Component } from 'react'

class LearnListComp extends Component {
    constructor(props){
        super(props)
        this.state = {
            name: 'Béla Aladár',
            code: 'ASD123Q',
            groups : [
                    {id: 1, description: 'Learn React', name: 'Group 1', news: {text: "hellóbeló g1"}},
                    {id: 2, description: 'Learn about India', name: 'Group 2', news: {text: "hír g2"}},
                    {id: 3, description: 'Learn Dance', name: 'Group 3', news: {text: "g3 "}}
            ]
        }
    }


    render() {
        return (
            <div className="container">
                <div className="row rounded shadow my-3 p-3" onClick={() => this.hideNews()}>
                    <h1 className="col-12">{this.state.name}</h1>
                    <h3 className="col-12">({this.state.code})</h3>
                </div>


                <div className="row rounded shadow my-3 p-3">
                    <h1 className="col-12">Groups</h1>
                </div>
                <div className="row input-group mb-3">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Join group</span>
                    </div>
                    <input type="text" className="form-control" placeholder="GROUP CODE"/>
                    <div className="input-group-append">
                        <button className="btn btn-outline-secondary">Join</button>
                    </div>
                </div>
                <div className="row my-3">
                    <table className="col-12 table table-striped table-hover rounded shadow">
                        <tbody>
                        {
                            this.state.groups.map(
                                group =>
                                    <tr onClick={() => this.groupClicked(group.id)}>
                                        <td>
                                            <div className="container">
                                                <div>
                                                    <strong className="col-auto">{group.name}</strong>
                                                    <span className="col-auto badge badge-pill bg-warning mx-2">{group.news.text}</span>
                                                </div>
                                                <div className="row">{group.description}</div>
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

    groupClicked = (id) => {
        console.log("Group " + id + " clicked!");
        this.props.history.push(`/learn/group/${id}`)
    }

    hideNews = () => {
        this.setState( {hideNews: !this.state.hideNews});
    }
}

export default LearnListComp;