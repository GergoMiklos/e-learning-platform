import React from 'react';
import PropTypes from 'prop-types';
import StatusElementCont from '../../containers/common/StatusElementCont';
import PercentageComp from '../common/PercentageComp';

export default function StatusDetailsPageComp({ studentStatus }) {
  return (
    <div className="container bg-secondary">
      <h1 className="row bg-light shadow mt-3 p-3">
        {studentStatus.user?.name}
      </h1>
      <h3 className="row justify-content-end m-1">
        {studentStatus.test?.group?.name}
      </h3>
      <h1 className="row bg-primary text-light rounded shadow mb-3 p-3 m-1">
        {studentStatus.test?.name}
      </h1>

      <section className="row p-1">
        <dl className="col-4">
          <dt className="text-center">Tasks (Solved/All)</dt>
          <PercentageComp
            correct={studentStatus.solvedTasks}
            all={studentStatus.test?.allTasks}
          />
        </dl>
        <dl className="col-4">
          <dt className="text-center">Answers (Correct/All)</dt>
          <PercentageComp
            correct={studentStatus.correctSolutions}
            all={studentStatus.allSolutions}
          />
        </dl>
        <dl className="col-4">
          <dt className="text-center">Status</dt>
          <StatusElementCont
            status={studentStatus.status}
            changedTime={studentStatus.statusChangedTime}
            solutionTime={studentStatus.lastSolutionTime}
          />
        </dl>
      </section>

      <section className="my-3 table-responsive-sm">
        {!studentStatus.studentTaskStatuses?.length ? (
          'No tasks'
        ) : (
          <table className="table table-striped bg-light">
            <thead>
              <tr>
                <th className="text-center">Task</th>
                <th className="text-center">Student (Correct/All)</th>
                <th className="text-center">Others (Correct/All)</th>
                <th className="text-center">Last Solution</th>
              </tr>
            </thead>
            <tbody>
              {studentStatus.studentTaskStatuses.map((status) => (
                <tr key={status.id}>
                  <td
                    className={
                      studentStatus.currentTestTask?.id === status.testTask?.id
                        ? 'font-weight-bold'
                        : ''
                    }
                  >
                    {status.testTask?.task?.question}
                  </td>
                  <td className="text-center">
                    <PercentageComp
                      correct={status.correctSolutions}
                      all={status.allSolutions}
                    />
                  </td>
                  <td className="text-center">
                    <PercentageComp
                      correct={status.testTask?.correctSolutions}
                      all={status.testTask?.allSolutions}
                      onlyPercentage
                    />
                  </td>
                  <td className="justify-content-center d-flex text-center">
                    <div
                      className={
                        status.wrongSolutionsInRow > 0
                          ? `bg-warning rounded-circle mt-1 p-${Math.min(
                              status.wrongSolutionsInRow,
                              2
                            )}`
                          : 'bg-primary rounded-circle mt-1 p-1'
                      }
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

StatusDetailsPageComp.propTypes = {
  studentStatus: PropTypes.object.isRequired,
};
