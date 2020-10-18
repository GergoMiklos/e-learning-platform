import React from 'react';
import {Link, Route, useRouteMatch, useHistory} from 'react-router-dom';
import StatusElementCont from "../../containers/common/StatusElementCont";
import PercentageComp from "../common/PercentageComp";
import PropTypes from "prop-types";
import StatusDetailsPageCont from "../../containers/status-details-page/StatusDetailsPageCont";

export default function ParentElementComp({student, onDelete}) {
    let match = useRouteMatch();
    let history = useHistory();

    return (
        <div className="row my-3">
            <section className="col-12 rounded shadow my-3 p-3 d-flex justify-content-between bg-light">
                <h1>{student.name}</h1>
                <button onClick={() => onDelete()} className="btn btn-outline-warning">
                    Unfollow
                </button>
            </section>

            <Route path={`${match.url}/details/:studentstatusid`} render={(props) =>
                (<StatusDetailsPageCont {...props} />)
            }/>

            <section className="col-12 table-responsive-sm px-1">
                {!student.studentStatuses?.length ? "No Statuses" :
                    <table className="col-12 table table-striped table-hover bg-light">
                        <thead>
                        <tr>
                            <th>Test</th>
                            <th className="text-center">Tasks (Solved/All)</th>
                            <th className="text-center">Answers (Correct/All)</th>
                            <th className="text-center">Status</th>
                        </tr>
                        </thead>

                        <tbody>
                        {student.studentStatuses.map(status =>
                            <tr
                                onClick={() => history.push(`${match.url}/details/${status.id}`)}
                                key={status.id}
                            >
                                <td className="font-weight-bold">
                                    {status.test.name}
                                </td>
                                <td className="text-center">
                                    <PercentageComp
                                        correct={status.solvedTasks}
                                        all={status.test.allTasks}
                                    />
                                </td>
                                <td className="text-center">
                                    <PercentageComp
                                        correct={status.correctSolutions}
                                        all={status.allSolutions}
                                    />
                                </td>
                                <td className='text-center'>
                                    <StatusElementCont
                                        status={status.status}
                                        changedTime={status.statusChangedTime}
                                    />
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                }
            </section>
        </div>
    );
}

ParentElementComp.propTypes = {
    student: PropTypes.object.isRequired,
    onDelete: PropTypes.func.isRequired,
}


