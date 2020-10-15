import React from "react";
import {useHistory} from 'react-router-dom'

export default function NotFoundPageComp({text}) {
    const history = useHistory();

    return(
        <div onClick={() => history.goBack()}>
            <div className="middle text-secondary">
                <div>{text? text : 'Page not found!'}</div>
            </div>
        </div>
    );
}