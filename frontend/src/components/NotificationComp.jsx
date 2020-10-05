import React, {Component} from "react";
import {Toast} from 'react-bootstrap';


//Todo storeból kapja az adatokat, AppCompba lesz beágyazva
class NotificationComp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            show: this.props.show,
            text: '',
            type: '',
        };
    }

    showNotification = ({text, type}) => {
        this.setState({
            show: true,
            text: text,
            type: type,
        })
    }

    render() {
        return (
            <Toast className={`alert alert-${this.props.type}`}
                   show={this.state.show}
                   onClose={() => this.setState({show: false})}
                   style={{
                       position: 'absolute',
                       top: '1em',
                       right: '1em',
                   }}
                   autohide>
                {this.props.text}
            </Toast>


    );
    }

    }

    export default NotificationComp;