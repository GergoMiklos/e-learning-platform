import React from 'react';
import { Collapse } from 'react-bootstrap';
import PropTypes, { number } from 'prop-types';

export default function NewTaskSearchElementComp({
  task,
  isSelected,
  selectedLevel,
  onSelectLevel,
  levels,
  onAddTask,
}) {
  return (
    <div className="container">
      <article className="row justify-content-between">
        <strong className="col-10">{task.question}</strong>
        <span className="col-auto">{task.usage}</span>
      </article>

      <Collapse in={isSelected}>
        <article>
          <div className="row rounded bg-secondary my-2 py-1">
            {task.answers.map((answer, i) => (
              <div
                key={answer.number}
                className={
                  answer.number === task.solutionNumber
                    ? 'font-weight-bold col-12'
                    : 'font-weight-light col-12'
                }
              >
                {`${i + 1}. ${answer.answer}`}
              </div>
            ))}
          </div>
          <div className="row justify-content-end">
            <select
              value={selectedLevel}
              onChange={(event) => onSelectLevel(event.target.value)}
            >
              {levels.map((level) => (
                <option value={level} key={level}>
                  Level: {level}
                </option>
              ))}
            </select>
            <button
              className="btn btn-primary btn-sm"
              onClick={() => onAddTask()}
            >
              Add
            </button>
          </div>
        </article>
      </Collapse>
    </div>
  );
}

NewTaskSearchElementComp.propTypes = {
  task: PropTypes.object.isRequired,
  isSelected: PropTypes.bool.isRequired,
  levels: PropTypes.arrayOf(number).isRequired,
  selectedLevel: PropTypes.number.isRequired,
  onSelectLevel: PropTypes.func.isRequired,
  onAddTask: PropTypes.func.isRequired,
};
