export interface SelectedFieldProps {
  label: string;
  value: string | number | any[];
  error?: string;
  onChange: Function;
  name: string;
  options: any[];
  defaultOption: string;
}
