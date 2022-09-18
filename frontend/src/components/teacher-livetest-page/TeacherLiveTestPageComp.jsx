import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import { Route, useHistory, useRouteMatch } from 'react-router-dom';
import StatusElementCont from '../../containers/common/StatusElementCont';
import PercentageComp from '../common/PercentageComp';
import StatusDetailsPageCont from '../../containers/status-details-page/StatusDetailsPageCont';

export default function TeacherLiveTestPageComp({
  test,
  onNavigateBack,
  onSubscribe,
}) {
  const match = useRouteMatch();
  const history = useHistory();

  useEffect(() => {
    onSubscribe();
  }, [onSubscribe]);

  return (
    <div className="container">
      <button
        onClick={() => onNavigateBack()}
        className="row btn btn-secondary mt-1"
      >
        Back
      </button>

      <section className="row bg-primary text-light rounded shadow my-3 p-3">
        <h1 className="col-12">{test.name}</h1>
      </section>

      <section className="row btn-group btn-block m-1">
        <div className="col-3 btn btn-info disabled">Not Started</div>
        <div className="col-3 btn btn-success disabled">In Progress</div>
        <div className="col-3 btn btn-warning disabled">Inactive</div>
        <div className="col-3 btn btn-danger disabled">Problem</div>
      </section>

      <Route
        path={`${match.url}/details/:studentstatusid`}
        render={(props) => <StatusDetailsPageCont {...props} />}
      />

      <section className="my-3 table-responsive-sm">
        {!test.studentStatuses?.length ? (
          'No Statuses'
        ) : (
          <table className="table table-striped bg-light">
            <thead>
              <tr>
                <th>Name</th>
                <th className="text-center">Code</th>
                <th className="text-center">Tasks (Solved/All)</th>
                <th className="text-center">Answers (Correct/All)</th>
                <th className="text-center">Status</th>
              </tr>
            </thead>
            <tbody>
              {test.studentStatuses.map((status) => (
                <tr
                  onClick={() =>
                    history.push(`${match.url}/details/${status.id}`)
                  }
                  key={status.id}
                >
                  <td className="font-weight-bold">{status.user.name}</td>
                  <td className="text-center">{status.user.code}</td>
                  <td className="text-center">
                    <PercentageComp
                      correct={status.solvedTasks}
                      all={test.allTasks}
                    />
                  </td>
                  <td className="text-center">
                    <PercentageComp
                      correct={status.correctSolutions}
                      all={status.allSolutions}
                    />
                  </td>
                  <td className="text-center">
                    <StatusElementCont
                      status={status.status}
                      changedTime={status.statusChangedTime}
                      solutionTime={status.lastSolutionTime}
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </div>
  );
}

TeacherLiveTestPageComp.propTypes = {
  test: PropTypes.object.isRequired,
  onNavigateBack: PropTypes.func.isRequired,
  onSubscribe: PropTypes.func.isRequired,
};
