import React, {Component} from 'react'
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import NavBarComp from "./NavBarComp";
import StudentPage from "./StudentPage";
import TeacherPageComp from "./TeacherPageComp";
import StudentGroupPageComp from "./StudentGroupPageComp";
import TeacherGroupPageComp from "./TeacherGroupPageComp";
import StudentLiveTestPageComp from "./StudentLiveTestPageComp";
import EditGroupPageComp from "./EditGroupPageComp";
import NewTaskPageComp from "./NewTaskPageComp"
import EditTestPageComp from "./EditTestPageComp";
import TeacherLiveTestPageComp from "./TeacherLiveTestPageComp";
import ParentPageComp from "./ParentPageComp";
import LoginPageComp from "./LoginPageComp";
import RegisterPageComp from "./RegisterPageComp";

const style = {
    backgroundImage: 'url(https://i.pinimg.com/originals/06/47/7e/06477ea4fbec33a0ad356f6095460775.gif)',
    height: '100%',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
    backgroundSize: 'cover',
}

class AppComp extends Component {

    render() {
        return (
            <div className="bg-secondary min-vh-100" style={style}>
                <NavBarComp></NavBarComp>
                <Router>
                    <Switch>
                        <Route path="/" exact component={StudentPage}/>
                        <Route path="/learn" exact component={StudentPage}/>
                        <Route path="/learn/group/:groupid/test/:testid" component={StudentLiveTestPageComp}/>
                        <Route path="/learn/group/:groupid" component={StudentGroupPageComp}/>
                        <Route path="/teach" exact component={TeacherPageComp}/>
                        <Route path="/teach/group/:groupid/test/:testid/edit" component={EditTestPageComp}/>
                        <Route path="/teach/group/:groupid/test/:testid" component={TeacherLiveTestPageComp}/>
                        <Route path="/teach/group/:groupid/edit" component={EditGroupPageComp}/>
                        <Route path="/teach/group/:groupid" component={TeacherGroupPageComp}/>
                        <Route path="/teach/test/:testid/tasks" component={NewTaskPageComp}/>
                        <Route path="/parent" component={ParentPageComp}/>
                        <Route path="/login" component={LoginPageComp}/>
                        <Route path="/register" component={RegisterPageComp}/>
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