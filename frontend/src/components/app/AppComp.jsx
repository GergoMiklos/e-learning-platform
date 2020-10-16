import React, {Component} from 'react'
import {Redirect, Route, Switch, useLocation} from 'react-router-dom'
import {useAuthentication} from "../../AuthService";

import NavBarComp from "./NavBarComp";
import StudentPageCont from "../../containers/student-page/StudentPageCont";
import TeacherPageCont from "../../containers/teacher-page/TeacherPageCont";
import NotFoundPageComp from "../common/NotFoundPageComp";
import StudentGroupPageCont from "../../containers/student-group-page/StudentGroupPageCont";
import StudentLiveTestPageCont from "../../containers/student-livetest-page/StudentLiveTestPageCont";
import LoginPageCont from "../../containers/login-signup-page/LoginPageCont";
import SignupPageCont from "../../containers/login-signup-page/SignupPageCont";
import TeacherGroupPageCont from "../../containers/teacher-group-page/TeacherGroupPageCont";
import EditGroupPageCont from "../../containers/edit-group-page/EditGroupPageCont";
import EditTestPageCont from "../../containers/edit-test-page/EditTestPageCont";
import NewTaskPageCont from "../../containers/new-task-page/NewTaskPageCont";
import ParentPageCont from "../../containers/parent-page/ParentPageCont";
import TeacherLiveTestPageCont from "../../containers/teacher-livetest-page/TeacherLiveTestPageCont";

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
                    <PrivateRoute exact path="/">
                        <Redirect to="/student"/>
                    </PrivateRoute>
                    <PrivateRoute path="/student/group/:groupid/test/:testid" component={StudentLiveTestPageCont}/>
                    <PrivateRoute path="/student/group/:groupid" component={StudentGroupPageCont}/>
                    <PrivateRoute path="/student" component={StudentPageCont}/>
                    <PrivateRoute path="/teacher/group/:groupid/test/:testid/edit/tasks" component={NewTaskPageCont}/>
                    <PrivateRoute path="/teacher/group/:groupid/test/:testid/edit" component={EditTestPageCont}/>
                    <PrivateRoute path="/teacher/group/:groupid/test/:testid" component={TeacherLiveTestPageCont}/>
                    <PrivateRoute path="/teacher/group/:groupid/edit" component={EditGroupPageCont}/>
                    <PrivateRoute path="/teacher/group/:groupid" component={TeacherGroupPageCont}/>
                    <PrivateRoute path="/teacher" component={TeacherPageCont}/>
                    <PrivateRoute path="/parent" component={ParentPageCont}/>
                    <PublicRoute exact path="/login" component={LoginPageCont}/>
                    <PublicRoute exact path="/register" component={SignupPageCont}/>
                    <Route component={NotFoundPageComp}/>
                </Switch>
            </div>
        );
    }
}

function PrivateRoute(props) {
    let location = useLocation();
    const {isLoggedIn} = useAuthentication();

    if (isLoggedIn) {
        return <Route {...props} />
    } else {
        return <Redirect to={{
            pathname: "/login",
            state: {from: location},
        }}
        />
    }
}

function PublicRoute(props) {
    const {isLoggedIn} = useAuthentication();

    if (!isLoggedIn) {
        return <Route {...props} />
    } else {
        return <Redirect to="/student"/>;
    }
}


export default AppComp;