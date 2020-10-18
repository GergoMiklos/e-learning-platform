import React from 'react'
import {Modal} from "react-bootstrap";
import PropTypes from "prop-types";
import StatusDetailsPageComp from "./StatusDetailsPageComp";

export default function ModalFormComp({onHide, studentStatus}) {

    return (
        <Modal
            centered
            onHide={() => onHide()}
            show={true}
            size="lg"
        >
            <StatusDetailsPageComp
                studentStatus={studentStatus}
            />
        </Modal>
    );
}

ModalFormComp.propTypes = {
    studentStatus: PropTypes.object.isRequired,
    onHide: PropTypes.func.isRequired,
}
