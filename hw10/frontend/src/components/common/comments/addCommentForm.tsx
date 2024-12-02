import { useState } from "react";
import TextAreaField from "../../forms/textAreaField";

interface AddCommentFormProps {
  onSubmit: Function;
}

const AddCommentForm = ({ onSubmit }: AddCommentFormProps) => {
  const [data, setData] = useState<any>({});    

  const handleChange = (target: any) => {
    setData((prevState: any) => ({
      ...prevState,
      [target.name]: target.value,
    }));
  };

  const clearForm = () => {
    setData({});
  };

  const handleSubmit = (e: any) => {
    e.preventDefault();
    onSubmit(data);
    clearForm();
  };

  return (
    <div>
      <h2>New comment</h2>
      <form onSubmit={handleSubmit}>
        <TextAreaField
          value={data.text || ""}
          onChange={handleChange}
          name="text"
          label="Сообщение"
        />
        <div className="d-flex justify-content-end">
          <button className="btn btn-primary">Опубликовать</button>
        </div>
      </form>
    </div>
  );
};

export default AddCommentForm;
