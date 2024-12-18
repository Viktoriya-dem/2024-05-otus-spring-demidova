import BtnCreate from "../../components/btnCreate/btnCreate";
import Cards from "../../components/cards/cards";
import { Link, useParams } from "react-router-dom";
import BookInfo from "../bookInfo/boolInfo";

const BookList = (): JSX.Element => {
  const params = useParams();
  const { bookId } = params;

  return (
    <div className="container">
      {bookId ? (
        <BookInfo idBook={bookId} />
      ) : (
        <div className="row">
          <div className="col-10">
            <Link to="/Create">
              <div className="mb-3 mt-3 d-flex justify-content-end">
                <BtnCreate />
              </div>
            </Link>
          </div>
          <div className="col-4">GroupList</div>
          <div className="col-6">
            <div>
              <Cards />
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default BookList;
