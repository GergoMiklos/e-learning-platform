import React, { Component } from 'react'
import client from "../ApolloClient";
import gql from 'graphql-tag';

const USER = gql`    {
    user {
        id
        name
        code
        groups {
            id
            name
            news {
                id
                text
            }
        }
    }
}`;

class LearnListComp extends Component {
    constructor(props){
        super(props)
        this.state = {
            user: null
        }
    }

    componentDidMount() {
        this.refreshGroups();
    }

    refreshGroups = () => {
        client
            .query({
                query: USER
            })
            .then(result => {
                console.log(result);
                if(!result.data.user) {
                    console.log("GraphQL query no result");
                } else {
                    this.setState({user: result.data.user});
                }
            })
            .catch(errors => {
                console.log(errors);
            });
    }

    groupClicked = (id) => {
        console.log("Group " + id + " clicked!");
        this.props.history.push(`/learn/group/${id}`)
    }

    hideNews = () => {
        this.setState( {hideNews: !this.state.hideNews});
    }

    joinGroup = () => {
        console.log("Join group clicked!");
        this.refreshGroups();
    }


    render() {
        if(!this.state.user) {
            return (<div></div>)
        }
        return (
            <div className="container">
                <div className="row rounded shadow my-3 p-3" onClick={() => this.hideNews()}>
                    <h1 className="col-12">{this.state.user.name}</h1>
                    <h3 className="col-12">({this.state.user.code})</h3>
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
                        <button className="btn btn-outline-secondary" onClick={() => this.joinGroup()}>
                            Join
                        </button>
                    </div>
                </div>

                {this.state.user.groups &&
                <div className="row my-3">
                    <table className="col-12 table table-striped table-hover rounded shadow">
                        <tbody>
                        {
                            this.state.user.groups.map(
                                group =>
                                    <tr key={group.id} onClick={() => this.groupClicked(group.id)}>
                                        <td>
                                            <div className="container">
                                                <div>
                                                    <strong className="col-auto">{group.name}</strong>
                                                    {group.news &&
                                                    <span className="col-auto badge badge-pill bg-warning mx-2">{group.news.text}</span>
                                                    }
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div> }
            </div>
        );
    }

}

export default LearnListComp;