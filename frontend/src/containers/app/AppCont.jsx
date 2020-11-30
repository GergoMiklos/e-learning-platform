import React, { Suspense, lazy } from 'react';
import { Redirect, Route, Switch, useLocation } from 'react-router-dom';
import { useAuthentication } from '../../AuthService';

import LoadingComp from '../../components/common/LoadingComp';
import NavBarComp from '../../components/app/NavBarComp';
import NotFoundPageComp from '../../components/common/NotFoundPageComp';

const StudentPageCont = lazy(() =>
  import('../student-page/StudentPageCont')
);
const TeacherPageCont = lazy(() =>
  import('../teacher-page/TeacherPageCont')
);
const StudentGroupPageCont = lazy(() =>
  import('../student-group-page/StudentGroupPageCont')
);
const StudentLiveTestPageCont = lazy(() =>
  import('../student-livetest-page/StudentLiveTestPageCont')
);
const TeacherGroupPageCont = lazy(() =>
  import('../teacher-group-page/TeacherGroupPageCont')
);
const EditGroupPageCont = lazy(() =>
  import('../edit-group-page/EditGroupPageCont')
);
const EditTestPageCont = lazy(() =>
  import('../edit-test-page/EditTestPageCont')
);
const NewTaskPageCont = lazy(() =>
  import('../new-task-page/NewTaskPageCont')
);
const ParentPageCont = lazy(() =>
  import('../parent-page/ParentPageCont')
);
const TeacherLiveTestPageCont = lazy(() =>
  import('../teacher-livetest-page/TeacherLiveTestPageCont')
);
const LoginPageCont = lazy(() =>
  import('../login-signup-page/LoginPageCont')
);
const SignupPageCont = lazy(() =>
  import('../login-signup-page/SignupPageCont')
);

const AppCont = () => (
  <div className="bg-secondary min-vh-100 pb-3">
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
        <PrivateRoute 
          path="/teacher" 
          component={TeacherPageCont}
        />
        <PrivateRoute 
          path="/parent" 
          component={ParentPageCont} 
        />
        <PublicRoute 
          path="/login" 
          component={LoginPageCont} 
        />
        <PublicRoute 
          path="/register" 
          component={SignupPageCont} 
        />
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

export default AppCont;
