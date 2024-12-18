export interface MultiSelectFieldProps {
  options: any[];
  onChange: Function;
  name: string;
  label: string;
  defaultValue: any;
  isMulti?: boolean;
  titleDirty?: boolean;
  titleError?: string;
  onBlur?: any; 
}
