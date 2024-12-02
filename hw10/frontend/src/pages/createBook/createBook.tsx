import BtnBack from "../../components/btnBack/btnBack";
import FormCreate from "../../components/forms/formCreate/formCreate";

const CreateBook = (): JSX.Element => {
  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-center mb-2">
        <h4>Добавьте свою книгу</h4>
      </div>
      <div className="d-flex justify-content-center">
        <FormCreate />
      </div>
      <div className="d-flex justify-content-center mt-5">
        <BtnBack push="books" />
      </div>
    </div>
  );
};

export default CreateBook;
