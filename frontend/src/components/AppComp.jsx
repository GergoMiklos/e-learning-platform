import React, {Component} from 'react'
import {Redirect, Route, Switch, useLocation, useHistory} from 'react-router-dom'
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
import {useAuthentication} from "../AuthService";
import NotFoundPageComp from "./NotFoundPageComp";

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
                    <PrivateRoute path="/student/group/:groupid/test/:testid" component={StudentLiveTestPageComp}/>
                    <PrivateRoute path="/student/group/:groupid" component={StudentGroupPageComp}/>
                    <PrivateRoute path="/student" component={StudentPageComp}/>
                    <PrivateRoute path="/teacher/group/:groupid/test/:testid/edit/tasks" component={NewTaskPageComp}/>
                    <PrivateRoute path="/teacher/group/:groupid/test/:testid/edit" component={EditTestPageComp}/>
                    <PrivateRoute path="/teacher/group/:groupid/test/:testid" component={TeacherLiveTestPageComp}/>
                    <PrivateRoute path="/teacher/group/:groupid/edit" component={EditGroupPageComp}/>
                    <PrivateRoute path="/teacher/group/:groupid" component={TeacherGroupPageComp}/>
                    <PrivateRoute path="/teacher" component={TeacherPageComp}/>
                    <PrivateRoute path="/parent" component={ParentPageComp}/>
                    <PublicRoute exact path="/login" component={LoginPageComp}/>
                    <PublicRoute exact path="/register" component={SignupPageComp}/>
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