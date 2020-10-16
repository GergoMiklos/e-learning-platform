import React, {useState} from 'react'
import {useHistory} from 'react-router-dom'
import {Collapse} from "react-bootstrap";

export default function StudentLiveTestPageComp({testTask, solution, isAnswered, chosenAnswerNumber, onSolution, onNextTask, onNavigateBack}) {

    return (
        <div className="container">
            <button
                className="row btn btn-secondary mt-1"
                onClick={() => onNavigateBack()}
            >
                Back
            </button>

            <section className="row rounded shadow bg-light my-3">
                <h2 className="col-12 m-1 p-3">
                    {testTask.task.question}
                </h2>

                <div className="col-12 my-3">
                    <ul className="list-group col-12">
                        {testTask.task.answers.map(
                            ({answer, number, id}) =>
                                <li
                                    key={id}
                                    className={`m-2 btn btn-lg text-left btn-${calculateAnswerColor({
                                        number,
                                        chosen: chosenAnswerNumber,
                                        correct: testTask.solutionNumber
                                    })}`}
                                    onClick={() => onSolution(number)}
                                    disabled={isAnswered}
                                >
                                    {answer}
                                </li>
                        )}
                    </ul>
                </div>
            </section>

            <Collapse in={isAnswered}>
                <section className="row">

                    {testTask.explanation &&
                    <article className="col-12 rounded shadow bg-primary my-3">
                        <h3 className="col-12 m-1 p-3 text-light">
                            {testTask.explanation}
                        </h3>
                    </article>
                    }

                    {solution &&
                    <div className="col-12 d-flex justify-content-between">
                        <SolutionComp solution={solution}/>

                        <button onClick={() => onNextTask()} className="btn btn-primary btn-lg p-3">
                            Next
                        </button>
                    </div>
                    }
                </section>
            </Collapse>
        </div>
    );
}


function SolutionComp({solution}) {
    return (
        <div>
            <div>
                {solution.allTasks === 0 ? 0 : Math.floor((solution.solvedTasks / solution.allTasks) * 100)}%
                &nbsp;Tasks
                ({solution.solvedTasks}/{solution.allTasks})
            </div>
            <div>
                {solution.allSolutions === 0 ? 0 : Math.floor((solution.correctSolutions / solution.allSolutions) * 100)}%
                &nbsp;Answers
                ({solution.correctSolutions}/{solution.allSolutions})
            </div>
        </div>
    );
}


const calculateAnswerColor = ({number, chosen, correct}) => {
    if (!chosen) {
        return 'primary';
    }
    if (number === correct) {
        return 'success';
    }
    if (number === chosen && number !== correct) {
        return 'warning';
    } else {
        return 'light';
    }
}
