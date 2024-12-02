import { SelectedFieldProps } from "./selectedField.props";

const SelectedField = ({
  label,
  value,
  onChange,
  options,
  error,
  name,
  defaultOption,
}: SelectedFieldProps) => {
  const handleChange = ({ target }: any) => {
    onChange({ name: target.name, value: target.value }); 
  };

  const optionsArray =
    !Array.isArray(options) && typeof options === "object"
      ? Object.values(options)
      : options;
  return (
    <div className="mb-3">
      <label htmlFor={name} className="form-label">
        {label}
      </label>
      <select
        className="form-select"
        id={name}
        name={name}
        value={value}
        onChange={handleChange}
      >
        <option disabled value="">
          {defaultOption}
        </option>
        {optionsArray.length > 0 &&
          optionsArray.map((option) => (
            <option value={option.value} key={option.value}>
              {option.label}
            </option>
          ))}
      </select>
      {error && <div className="invalid-feedback">{error}</div>}
    </div>
  );
};

export default SelectedField;
