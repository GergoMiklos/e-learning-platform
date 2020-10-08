import React, {Component} from 'react'
import {Redirect, Route, Switch} from 'react-router-dom'
import NavBarComp from "./NavBarComp";
import StudentPageComp from "./StudentPageComp";
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
import SignupPageComp from "./SignupPageComp";
import AuthService from "../AuthService";

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
            <div className="bg-secondary min-vh-100 pb-3" style={style}>
                <NavBarComp/>
                <Switch>
                    <AuthenticatedRoute exact path={["/", "/student"]} component={StudentPageComp}/>
                    <AuthenticatedRoute path="/student/group/:groupid/test/:testid" component={StudentLiveTestPageComp}/>
                    <AuthenticatedRoute path="/student/group/:groupid" component={StudentGroupPageComp}/>
                    <AuthenticatedRoute exact path="/teacher" component={TeacherPageComp}/>
                    <AuthenticatedRoute path="/teacher/group/:groupid/test/:testid/tasks" component={NewTaskPageComp}/>
                    <AuthenticatedRoute path="/teacher/group/:groupid/test/:testid/edit" component={EditTestPageComp}/>
                    <AuthenticatedRoute path="/teacher/group/:groupid/test/:testid" component={TeacherLiveTestPageComp}/>
                    <AuthenticatedRoute path="/teacher/group/:groupid/edit" component={EditGroupPageComp}/>
                    <AuthenticatedRoute path="/teacher/group/:groupid" component={TeacherGroupPageComp}/>
                    <AuthenticatedRoute exact path="/parent" component={ParentPageComp}/>
                    <Route exact path="/login" component={LoginPageComp}/>
                    <Route exact path="/register" component={SignupPageComp}/>
                    <Route component={ErrorComp}/>
                </Switch>
            </div>
        );
    }
}

function AuthenticatedRoute(props) {
    if (AuthService.isLoggedIn()) {
        return <Route {...props} />
    } else {
        return <Redirect to="/login" />
    }

}

function ErrorComp() {
    return <div>Oops! Something went wrong.</div>
}

export default AppComp;