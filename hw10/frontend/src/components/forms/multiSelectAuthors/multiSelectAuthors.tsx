interface MultiSelect {
  options: any[];
  onChange: Function;
  name: string;
  label: string;
  value: any;
  authorDirty?: boolean;
  authorError?: string;
  onBlur?: any;
}

const MultiSelectFieldAuthors = ({
  options,
  onChange,
  name,
  label,
  value,
  authorDirty,
  authorError,
  onBlur,
}: MultiSelect): JSX.Element => {
  const optionsArray =
    !Array.isArray(options) && typeof options === "object"
      ? Object.values(options)
      : options;  

  const handleChange = ({ target }: any) => {
    onChange({ name: target.name, value: target.value }); 
  };  

  const getInputClasses = () => {
    return "form-select" + (authorError ? " is-invalid" : "");
  };

  return (
    <div className="mb-4">
      <label className="form-label">{label}</label>
      <div className="input-group has-validation">
        <select
          className={getInputClasses()}
          id={name}
          name={name}
          value={value}
          onChange={handleChange}
          onBlur={onBlur}
          multiple={false}
          required
        >
          <option disabled value="">
            {"Выберите автора"}
          </option>
          {optionsArray.length > 0 &&
            optionsArray.map((option) => (
              <option value={option.id} key={option.id}>
                {option.fullName}
              </option>
            ))}
        </select>
        {authorDirty && authorError && (
          <div className="invalid-feedback">{authorError}</div>
        )}
      </div>
    </div>
  );
};

export default MultiSelectFieldAuthors;
