import React, {Component} from 'react'
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import NavBarComp from "./NavBarComp";
import LearnListComp from "./LearnListComp";
import TeachListComp from "./TeachListComp";
import LearnGroupComp from "./LearnGroupComp";
import TeachGroupComp from "./TeachGroupComp";
import LearnLiveTestComp from "./LearnLiveTestComp";
import EditGroupComp from "./EditGroupComp";
import NewTaskSearchComp from "./NewTaskSearchComp"
import NewTaskComp from "./NewTaskComp";
import NewGroupComp from "./NewGroupComp";
import NewTestComp from "./NewTestComp";
import EditTestComp from "./EditTestComp";
import NewLiveTestComp from "./NewLiveTestComp";
import TeachLiveTestComp from "./TeachLiveTestComp";
import {Toast} from "react-bootstrap";

class AppComp extends Component {
    render() {
        //todo store-ból hívjuk majd?
        return (
            <div>
                <NavBarComp></NavBarComp>
                <Router>
                    <Switch>
                        <Route path="/" exact component={LearnListComp}/>
                        <Route path="/learn" exact component={LearnListComp}/>
                        <Route path="/learn/group/:groupid/test/:testid" component={LearnLiveTestComp}/>
                        <Route path="/learn/group/:groupid" component={LearnGroupComp}/>
                        <Route path="/teach" exact component={TeachListComp}/>
                        <Route path="/teach/group/new" component={NewGroupComp}/>
                        <Route path="/teach/group/:groupid/livetest/new" component={NewLiveTestComp}/>
                        <Route path="/teach/group/:groupid/test/new" component={NewTestComp}/>
                        <Route path="/teach/group/:groupid/test/:testid/edit" component={EditTestComp}/>
                        <Route path="/teach/group/:groupid/test/:testid" component={TeachLiveTestComp}/>
                        <Route path="/teach/group/:groupid/edit" component={EditGroupComp}/>
                        <Route path="/teach/group/:groupid" component={TeachGroupComp}/>
                        <Route path="/teach/test/:testid/tasks/new" component={NewTaskComp}/>
                        <Route path="/teach/test/:testid/tasks" component={NewTaskSearchComp}/>
                        <Route path="/group/test/:id" component={NewGroupComp}/>
                        <Route component={ErrorComp}/>
                    </Switch>
                </Router>
            </div>
        );
    }
}

function ErrorComp() {
    return <div>Oops! Something went wrong.</div>
}

export default AppComp;