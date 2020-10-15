import React from 'react';
import {Spinner} from "react-bootstrap";

export default function LoadingComp({text}) {
    return(
        <div className="middle container">
            <div className="row justify-content-center">
                <Spinner animation="border" variant="secondary"/>
            </div>

            {text &&
            <div className="row justify-content-center my-1 text-secondary">
                <div>{text}</div>
            </div>
            }
        </div>
    );
}