import React from "react";
import {Link} from "react-router-dom";
import {Collapse} from "react-bootstrap";
import PropTypes from "prop-types";

export default function TeacherGroupElementComp({test, onChangeStatus, isSelected, statusPath, editPath}) {

    return (
        <div>
            <div className="d-flex justify-content-between">
                <div className="font-weight-bold">
                    {test.name}
                </div>

                {!test.active && !isSelected &&
                <span className="badge badge-pill bg-secondary mx-2 px-2 py-2">
                    INACTIVE
                </span>
                }

                {isSelected &&
                <span>
                    {test.active &&
                    <Link to={statusPath} className="btn btn-primary btn-sm">
                        Statuses
                    </Link>
                    }
                    <Link to={editPath} className="btn btn-outline-warning btn-sm">
                        Edit
                    </Link>
                    <button onClick={() => onChangeStatus()} className="btn btn-secondary btn-sm">
                        {test.active ? 'Inactivate' : 'Activate'}
                    </button>
                </span>
                }
            </div>

            <Collapse in={isSelected}>
                <div className='font-weight-light'>
                    {test.description}
                </div>
            </Collapse>
        </div>
    );
}

TeacherGroupElementComp.propTypes = {
    test: PropTypes.object.isRequired,
    isSelected: PropTypes.bool.isRequired,
    statusPath: PropTypes.string.isRequired,
    editPath: PropTypes.string.isRequired,
}