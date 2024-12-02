import { useNavigate } from "react-router-dom";

export interface BtnBackProps {
  push: string;
}


const BtnBack = ({ push }: BtnBackProps): JSX.Element => {
  const navigate = useNavigate();

  const handleBack = () => {
    navigate(`/${push}`);
  };

  return (
    <button type="button" className="btn btn-info" onClick={() => handleBack()}>
      Вернуться назад
    </button>
  );
};

export default BtnBack;
