import React from 'react';
import PropTypes from 'prop-types';
import FormComp from '../../components/common/FormComp';

export default function FormCont({
  initialName,
  initialDescription,
  onSubmit,
}) {
  return (
    <FormComp
      onSubmit={onSubmit}
      validate={validate}
      initialName={initialName}
      initialDescription={initialDescription}
    />
  );
}

FormCont.propTypes = {
  initialName: PropTypes.string,
  initialDescription: PropTypes.string,
  onSubmit: PropTypes.func.isRequired,
};

const validate = (values) => {
  const errors = {};
  if (!values.name) {
    errors.name = 'Name is required!';
  } else if (
    !values.name ||
    values.name.length > 150 ||
    values.name.length < 5
  ) {
    errors.name = 'Name should be between min 5 and max 150 characters!';
  }
  if (!values.description) {
    errors.description = 'Description is required!';
  } else if (values.description.length > 500 || values.description.length < 5) {
    errors.description =
      'Description should be between min 5 and max 500 characters!';
  }
  return errors;
};
