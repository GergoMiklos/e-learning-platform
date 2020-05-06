import React, { Component } from 'react'
import {BrowserRouter as Router, Route, Switch, Link} from 'react-router-dom'
import NavBarComp from "./NavBarComp";
import LearnListComp from "./LearnListComp";
import TeachListComp from "./TeachListComp";
import LearnGroupComp from "./LearnGroupComp";
import TeachGroupComp from "./TeachGroupComp";
import LearnLiveTestComp from "./LearnLiveTestComp";
import EditGroupComp from "./EditGroupComp";
import TaskListComp from "./TaskListComp"
import NewTaskComp from "./NewTaskComp";
import NewGroupComp from "./NewGroupComp";
import NewTestComp from "./NewTestComp";
import EditTestComp from "./EditTestComp";
import TeachNewLiveTestComp from "./TeachNewLiveTestComp";

class AppComp extends Component {
    render() {
        return (
            <Router>
                <NavBarComp></NavBarComp>
                <Switch>
                    <Route path="/" exact component={LearnListComp}/>
                    <Route path="/learn" exact component={LearnListComp}/>
                    <Route path="/learn/group/:groupid/test/:testid" component={LearnLiveTestComp}/>
                    <Route path="/learn/group/:groupid" component={LearnGroupComp}/>
                    <Route path="/teach" exact component={TeachListComp}/>
                    <Route path="/teach/group/new" component={NewGroupComp}/>
                    <Route path="/teach/group/:groupid/test/new" component={TeachNewLiveTestComp}/>
                    <Route path="/teach/group/:groupid/edit" component={EditGroupComp}/>
                    <Route path="/teach/group/:groupid" component={TeachGroupComp}/>
                    <Route path="/teach/test/new" component={NewTestComp}/>
                    <Route path="/teach/test/:id/edit" component={EditTestComp}/>
                    <Route path="/teach/test/:testid/tasks/new" component={NewTaskComp}/>
                    <Route path="/teach/test/:testid/tasks" component={TaskListComp}/>
                    <Route path="/group/test/:id" component={NewGroupComp}/>
                    <Route component={ErrorComp}/>
                </Switch>
            </Router>
        );
    }
}

function ErrorComp() {
    return <div>Oops! The link does not exist.</div>
}

export default AppComp;