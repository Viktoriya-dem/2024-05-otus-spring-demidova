import Select from "react-select";
import makeAnimated from "react-select/animated";
import { MultiSelectFieldProps } from "./multiSelectField.props";

const animatedComponents = makeAnimated();
const MultiSelectField = ({
  options,
  onChange,
  name,
  label,
  defaultValue,
  isMulti,
  titleDirty,
  titleError,  
}: MultiSelectFieldProps) => {
  const optionsArray =
    !Array.isArray(options) && typeof options === "object"
      ? Object.values(options)
      : options;

  const handleChange = (value: any) => {
    onChange({ name: name, value });
  };
  
  return (
    <div className="mb-4">
      <label className="form-label">{label}</label>
      <Select
        isMulti={isMulti}
        closeMenuOnSelect={false}
        components={animatedComponents}
        defaultValue={defaultValue}
        options={optionsArray}
        className="basic-multi-select"
        classNamePrefix="Выберите жанры"
        onChange={handleChange}
        name={name}
        required
      />
      {titleDirty && titleError && (
        <div className="invalid-feedback">{titleError}</div>
      )}
    </div>
  );
};

export default MultiSelectField;
