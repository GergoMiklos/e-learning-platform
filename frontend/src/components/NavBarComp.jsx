import React, {useState} from 'react';
import AuthService from "../AuthService";
import {NavLink, Link} from "react-router-dom";

export default function NavBarComp() {
    const [isCollapsed, setCollapsed] = useState(true)

    return (
        <div>
            <nav className="navbar navbar-expand-md navbar-dark bg-primary">
                <Link className="navbar-brand" to="/student">LearnWell</Link>
                <button className="navbar-toggler" onClick={() => setCollapsed(!isCollapsed)}>
                    <span className="navbar-toggler-icon"/>
                </button>
                //todo
                {AuthService.isLoggedIn() &&
                <collapse className={`navbar-collapse ${isCollapsed? 'collapse' : ''}`}>
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
                        <Link className="nav-link" to="/login" onClick={() => AuthService.logout()}>Logout</Link>
                    </span>
                </collapse>
                }
            </nav>
        </div>
    )
}


