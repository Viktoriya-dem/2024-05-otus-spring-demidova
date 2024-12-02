interface TextAreaFieldProps {
  label: string;
  name: string;
  value: string;
  onChange: Function;
}

const TextAreaField = ({
  label,
  name,
  value,
  onChange,
}: TextAreaFieldProps): JSX.Element => {
  const handleChange = ({ target }: any) => {
    onChange({ name: target.name, value: target.value });
  };

  return (
    <div className="mb-4">
      <label htmlFor={name}> {label}</label>
      <div className="input-group has-validation">
        <textarea
          id={name}
          name={name}
          value={value}
          onChange={handleChange}
          className="form-control"
        />
      </div>
    </div>
  );
};

export default TextAreaField;
