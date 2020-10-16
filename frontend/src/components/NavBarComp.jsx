import React, {useState} from 'react';
import {useAuthentication} from "../AuthService";
import {NavLink, Link} from "react-router-dom";
import {Collapse, Navbar} from "react-bootstrap";

export default function NavBarComp() {
    const [isCollapsed, setCollapsed] = useState(true)
    const {isLoggedIn, setLogout} = useAuthentication()

    return (
        <div>
            <Navbar expand="lg" className="navbar-dark bg-primary">
                <Link className="navbar-brand" to="/student">LearnWell</Link>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />

                {isLoggedIn &&
                <Navbar.Collapse id="basic-navbar-nav">
                    <ul className="navbar-nav mr-auto">
                        <li className="nav-item">
                            <NavLink activeClassName="active" className="nav-link" to="/student">Student<span className="sr-only">(current)</span></NavLink>
                        </li>
                        <li className="nav-item">
                            <NavLink activeClassName="active" className="nav-link" to="/teacher">Teacher</NavLink>
                        </li>
                        <li className="nav-item">
                            <NavLink activeClassName="active" className="nav-link" to="/parent">Parent</NavLink>
                        </li>
                    </ul>
                    <span className="navbar-nav">
                        <Link className="nav-link" to="/login" onClick={() => setLogout()}>Logout</Link>
                    </span>
                </Navbar.Collapse>
                }
            </Navbar>
        </div>
    )
}


