import { useState } from "react";
import { useAppDispatch } from "../../hooks";
import { removeBook } from "../../store/bookReducer";
import ModalWindow from "../modalWindow/modalWindow";
import ModalFormEdit from "../../pages/modalFormEdit/modalFormEdit";
import { Link } from "react-router-dom";
import CardBody from "../cardBody/cardBody";

interface CardProps {
  title: string;
  authorName: string;
  genres: [];
  id: number;
}

const Card = ({ title, authorName, genres, id }: CardProps): JSX.Element => {
  const [isModalActive, setModalActive] = useState(false);
  const handleModalOpen = () => {
    setModalActive(true);
  };
  const handleModalClose = () => {
    setModalActive(false);
  };

  const dispatch = useAppDispatch();
  return (
    <div className="card text-center position-relative">
      <Link to={`/Books/${id}`}>
        <CardBody title={title} authorName={authorName} genres={genres} />
      </Link>
      <button
        type="button"
        className="btn position-absolute top-0 start-0"
        onClick={handleModalOpen}
      >
        <i className="bi bi-pencil-square"></i>
      </button>
      <button
        type="button"
        className="btn position-absolute top-0 end-0"
        onClick={() => dispatch(removeBook(id))}
      >
        <i className="bi bi-x-circle-fill"></i>
      </button>
      <div>
        {isModalActive && (
          <ModalWindow title="Отредактируйте книгу" onClose={handleModalClose}>
            <ModalFormEdit
              bookId={id}
              title={title}
              authorName={authorName}
              genresArr={genres}
            />
          </ModalWindow>
        )}
      </div>
    </div>
  );
};

export default Card;
