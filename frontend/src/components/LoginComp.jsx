import React, { Component } from 'react';

class LoginComp extends Component {
    render() {
        return (
            <div className="">
                <div className="container">
                    <div className="row justify-content-center">
                        <div className="col-md-4 col-12 mt-5">
                            <form>
                                <div className="form-group">
                                    <label htmlFor="email">Email</label>
                                    <input type="text" className="form-control" id="email" name="email"/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="password">Password</label>
                                    <input type="password" className="form-control" id="password" name="password"/>
                                </div>
                                <div className="btn btn-primary btn-block" >Log in</div>
                            </form>
                        </div>
                    </div>
                    <div className="row justify-content-center">
                        <div className=" col-md-4 col-12 mt-3"Y>
                            <div className="btn btn-light btn-block" >Forgot password</div>
                        </div>
                    </div>
                    <div className="row justify-content-center">
                        <div className=" col-md-4 col-12 mt-5">
                            <div className="btn btn-primary btn-block" >Sign up</div>
                        </div>
                    </div>
                </div>
            </div>

        )
    }
}

export default LoginComp;