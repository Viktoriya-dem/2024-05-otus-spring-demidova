interface TextFieldProps {
  label: string;
  htmlFor?: string;
  type: string;
  name: string;
  value: string | number;
  onChange: Function;
  placeholder?: any;
  error?: any;
  onBlur?: any;
  titleDirty?: boolean;
  titleError?: string;
}

const TitleField = ({
  label,
  type,
  name,
  value,
  onChange,
  onBlur,
  titleDirty,
  titleError,
}: TextFieldProps): JSX.Element => {
  const handleChange = ({ target }: any) => {
    onChange({
      name: target.name,
      value: target.value,
    });
  };

  const getInputClasses = () => {
    return "form-control" + (titleError ? " is-invalid" : "");
  };

  return (
    <div className="mb-4">
      <label className="form-label">{label}</label>
      <div className="input-group has-validation">
        <input
          type={type}
          id="validationDefault01"
          name={name}
          value={value}
          onChange={handleChange}
          className={getInputClasses()}
          onBlur={onBlur}
          required
        />
        {titleDirty && titleError && (
          <div className="invalid-feedback">{titleError}</div>
        )}
      </div>
    </div>
  );
};

export default TitleField;
