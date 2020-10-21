import React from 'react';
import { Collapse } from 'react-bootstrap';
import PropTypes from 'prop-types';
import PercentageComp from '../common/PercentageComp';

export default function StudentLiveTestPageComp({
  testTask,
  solution,
  isAnswered,
  chosenAnswerNumber,
  onSolution,
  onNextTask,
  onNavigateBack,
}) {
  return (
    <div className="container">
      <button
        onClick={() => onNavigateBack()}
        className="row btn btn-secondary mt-1"
      >
        Back
      </button>

      <section className="row rounded shadow bg-light my-3">
        <h2 className="col-12 m-1 p-3">{testTask.task.question}</h2>

        <div className="col-12 my-3">
          <ul className="list-group col-12">
            {testTask.task.answers.map(({ answer, number, id }) => (
              <button
                key={id}
                onClick={() => onSolution(number)}
                disabled={isAnswered}
                className={`m-2 btn btn-lg text-left btn-${calculateAnswerColor(
                  {
                    number,
                    chosen: chosenAnswerNumber,
                    correct: testTask.task.solutionNumber,
                  }
                )}`}
              >
                {answer}
              </button>
            ))}
          </ul>
        </div>
      </section>

      <Collapse in={isAnswered}>
        <section className="row">
          {testTask.explanation && (
            <article className="col-12 rounded shadow bg-primary my-3">
              <h3 className="col-12 m-1 p-3 text-light">
                {testTask.explanation}
              </h3>
            </article>
          )}

          {solution && (
            <div className="col-12 d-flex justify-content-between">
              <SolutionComp solution={solution} />

              <button
                onClick={() => onNextTask()}
                className="btn btn-primary btn-lg p-3"
              >
                Next
              </button>
            </div>
          )}
        </section>
      </Collapse>
    </div>
  );
}

function SolutionComp({ solution }) {
  return (
    <div>
      <div className="d-flex justify-content-start">
        Tasks:
        <PercentageComp
          correct={solution.solvedTasks}
          all={solution.allTasks}
        />
      </div>
      <div className="d-flex justify-content-start">
        Answers
        <PercentageComp
          correct={solution.correctSolutions}
          all={solution.allSolutions}
        />
      </div>
    </div>
  );
}

const calculateAnswerColor = ({ number, chosen, correct }) => {
  if (!chosen) {
    return 'primary';
  }
  if (number === correct) {
    return 'success';
  }
  if (number === chosen && number !== correct) {
    return 'warning';
  }
  return 'light';
};

SolutionComp.propTypes = {
  solution: PropTypes.object.isRequired,
};

StudentLiveTestPageComp.propTypes = {
  testTask: PropTypes.object.isRequired,
  solution: PropTypes.object,
  isAnswered: PropTypes.bool.isRequired,
  chosenAnswerNumber: PropTypes.number,
  onNavigateBack: PropTypes.func.isRequired,
  onNextTask: PropTypes.func.isRequired,
  onSolution: PropTypes.func.isRequired,
};
