import React from 'react';
import { Link, NavLink } from 'react-router-dom';
import { Navbar } from 'react-bootstrap';
import { useAuthentication } from '../../AuthService';

export default function NavBarComp() {
  const { isLoggedIn, setLogout } = useAuthentication();

  return (
    <div>
      <Navbar expand="lg" className="navbar-dark bg-primary">
        <Link className="navbar-brand" to="/student">
          LearnWell
        </Link>
        <Navbar.Toggle aria-controls="navbar-nav" />

        {isLoggedIn && (
          <Navbar.Collapse id="navbar-nav">
            <ul className="navbar-nav mr-auto">
              <li className="nav-item">
                <NavLink
                  activeClassName="active"
                  className="nav-link"
                  to="/student"
                >
                  Student
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink
                  activeClassName="active"
                  className="nav-link"
                  to="/teacher"
                >
                  Teacher
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink
                  activeClassName="active"
                  className="nav-link"
                  to="/parent"
                >
                  Parent
                </NavLink>
              </li>
            </ul>
            <span className="navbar-nav">
              <Link
                className="nav-link"
                to="/login"
                onClick={() => setLogout()}
              >
                Logout
              </Link>
            </span>
          </Navbar.Collapse>
        )}
      </Navbar>
    </div>
  );
}
