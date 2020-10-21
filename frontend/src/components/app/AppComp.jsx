import React, { Suspense, lazy } from 'react';
import { Redirect, Route, Switch, useLocation } from 'react-router-dom';
import { useAuthentication } from '../../AuthService';

import LoadingComp from '../common/LoadingComp';
import NavBarComp from './NavBarComp';
import NotFoundPageComp from '../common/NotFoundPageComp';

const StudentPageCont = lazy(() =>
  import('../../containers/student-page/StudentPageCont')
);
const TeacherPageCont = lazy(() =>
  import('../../containers/teacher-page/TeacherPageCont')
);
const StudentGroupPageCont = lazy(() =>
  import('../../containers/student-group-page/StudentGroupPageCont')
);
const StudentLiveTestPageCont = lazy(() =>
  import('../../containers/student-livetest-page/StudentLiveTestPageCont')
);
const TeacherGroupPageCont = lazy(() =>
  import('../../containers/teacher-group-page/TeacherGroupPageCont')
);
const EditGroupPageCont = lazy(() =>
  import('../../containers/edit-group-page/EditGroupPageCont')
);
const EditTestPageCont = lazy(() =>
  import('../../containers/edit-test-page/EditTestPageCont')
);
const NewTaskPageCont = lazy(() =>
  import('../../containers/new-task-page/NewTaskPageCont')
);
const ParentPageCont = lazy(() =>
  import('../../containers/parent-page/ParentPageCont')
);
const TeacherLiveTestPageCont = lazy(() =>
  import('../../containers/teacher-livetest-page/TeacherLiveTestPageCont')
);
const LoginPageCont = lazy(() =>
  import('../../containers/login-signup-page/LoginPageCont')
);
const SignupPageCont = lazy(() =>
  import('../../containers/login-signup-page/SignupPageCont')
);

const style = {
  backgroundImage:
    'url(https://i.pinimg.com/originals/06/47/7e/06477ea4fbec33a0ad356f6095460775.gif)',
  height: '100%',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  backgroundSize: 'cover',
};

const AppComp = () => (
  <div className="bg-secondary min-vh-100 pb-3" style={style}>
    <NavBarComp />
    <Suspense fallback={<LoadingComp />}>
      <Switch>
        <PrivateRoute exact path="/">
          <Redirect to="/student" />
        </PrivateRoute>
        <PrivateRoute
          path="/student/group/:groupid/test/:testid"
          component={StudentLiveTestPageCont}
        />
        <PrivateRoute
          path="/student/group/:groupid"
          component={StudentGroupPageCont}
        />
        <PrivateRoute path="/student" component={StudentPageCont} />
        <PrivateRoute
          path="/teacher/group/:groupid/test/:testid/edit/tasks"
          component={NewTaskPageCont}
        />
        <PrivateRoute
          path="/teacher/group/:groupid/test/:testid/edit"
          component={EditTestPageCont}
        />
        <PrivateRoute
          path="/teacher/group/:groupid/test/:testid"
          component={TeacherLiveTestPageCont}
        />
        <PrivateRoute
          path="/teacher/group/:groupid/edit"
          component={EditGroupPageCont}
        />
        <PrivateRoute
          path="/teacher/group/:groupid"
          component={TeacherGroupPageCont}
        />
        <PrivateRoute path="/teacher" component={TeacherPageCont} />
        <PrivateRoute path="/parent" component={ParentPageCont} />
        <PublicRoute path="/login" component={LoginPageCont} />
        <PublicRoute path="/register" component={SignupPageCont} />
        <Route component={NotFoundPageComp} />
      </Switch>
    </Suspense>
  </div>
);

function PrivateRoute(props) {
  const location = useLocation();
  const { isLoggedIn } = useAuthentication();

  if (isLoggedIn) {
    return <Route {...props} />;
  }
  return (
    <Redirect
      to={{
        pathname: '/login',
        state: { from: location },
      }}
    />
  );
}

function PublicRoute(props) {
  const { isLoggedIn } = useAuthentication();

  if (!isLoggedIn) {
    return <Route {...props} />;
  }
  return <Redirect to="/student" />;
}

export default AppComp;
