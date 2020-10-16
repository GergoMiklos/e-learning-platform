import React, {useState} from "react";
import client from "../../ApolloClient";
import toast from "toasted-notes";
import gql from "graphql-tag";
import {useMutation} from "@apollo/client";
import {useHistory} from "react-router-dom";
import {Collapse} from "react-bootstrap";

const DELETE_TASK_MUTATION = gql`
    mutation DeleteTaskFromTest($testTaskId: ID!) {
        deleteTaskFromTest(testTaskId: $testTaskId)
    }`;


export default function EditTestElementComp({
                                                testTask, isSelected, levelState, explanationState, levels,
                                                onExplanationChange, onLevelChange, onEdit, isEditDisabled, onDelete
                                            }) {

    return (
        <div className="container">
            <article className="row justify-content-between">
                <strong className="col-11 col-lg-8 mb-1">
                    {testTask.task.question}
                </strong>
                <span className="col-9 col-lg-2">
                    Correct/All: {testTask.allSolutions === 0 ? 0 : Math.floor((testTask.correctSolutions / testTask.allSolutions) * 100)}%
                </span>
                <span className="col-auto">
                    <div
                        className={`badge badge-${(levelState.changed || explanationState.changed) ? 
                            'warning' : 'primary'} p-2`}
                    >
                        {levelState.level}
                    </div>
                </span>
            </article>

            <Collapse in={isSelected}>
                <article>
                    <div className="row rounded bg-secondary my-2 py-1"> {testTask.task.answers.map(
                        (answer, i) =>
                            <div
                                key={answer.number}
                                className={(answer.number === testTask.task.solutionNumber) ?
                                    'font-weight-bold col-12' : 'font-weight-light col-12'}
                            >
                                {`${i + 1}. ${answer.answer}`}
                            </div>
                    )}
                    </div>

                    <div className="row input-group">
                        <div className="input-group-prepend">
                            <label className="input-group-text">Explanation:</label>
                        </div>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Add explanation for the solution"
                            value={explanationState.explanation}
                            onChange={event => onExplanationChange(event.target.value.slice(0, 500))}
                        />
                        <div className="input-group-append">
                            <select
                                className="col-auto"
                                value={levelState.level}
                                onChange={event => onLevelChange(event.target.value)}
                            >
                                {levels.map((level) =>
                                    <option value={level} key={level}>
                                        Level: {level}
                                    </option>
                                )}
                            </select>
                            <button
                                className="btn btn-primary"
                                onClick={() => onEdit()}
                                disabled={isEditDisabled}
                            >
                                Save
                            </button>
                            <button
                                className="btn btn-outline-warning btn-sm"
                                onClick={() => onDelete()}
                            >
                                Delete
                            </button>
                        </div>
                    </div>
                </article>
            </Collapse>
        </div>
    );
}
