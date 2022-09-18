import React from 'react';
import { Modal } from 'react-bootstrap';
import PropTypes from 'prop-types';
import FormCont from '../../containers/common/FormCont';

export default function ModalFormComp({
  title,
  initialName,
  initialDescription,
  onSubmit,
  onHide,
  show,
}) {
  return (
    <Modal centered onHide={() => onHide()} show={show ?? true}>
      <section className="container">
        <div className="row bg-primary text-light p-3">
          <h1>{title}</h1>
        </div>

        <FormCont
          onSubmit={onSubmit}
          initialName={initialName}
          initialDescription={initialDescription}
        />
      </section>
    </Modal>
  );
}

ModalFormComp.propTypes = {
  title: PropTypes.string,
  initialName: PropTypes.string,
  initialDescription: PropTypes.string,
  show: PropTypes.bool,
  onHide: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
};
