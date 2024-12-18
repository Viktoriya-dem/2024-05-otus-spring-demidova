import { useAppSelector } from "../../hooks";
import { getBooks, getCurrentPage } from "../../store/bookReducer";
import { pagesArray, paginate } from "../../utils/paginate";
import Card from "../card/card";
import Pagination from "../pagination/pagination";

const Cards = (): JSX.Element => {
  const books = useAppSelector(getBooks());
  const currentPage = useAppSelector(getCurrentPage())
 
// console.log(books);

  const pageSize: number = 4

  
  const pagesCount: number = Math.ceil(books.length / pageSize);
  const pages = pagesArray(pagesCount);

  const bookCrop = paginate(books, currentPage, pageSize);

  return (
    <>
      <div className="grid text-center ">
        {books ? (
          bookCrop.map((book: any) => (
            <div key={book.id} className="g-col-6">
              <Card
                id={book.id}
                title={book.title}
                authorName={book.author.fullName}
                genres={book.genres}
              />
            </div>
          ))
        ) : (
          <div>Loading</div>
        )}
      </div>
      <div className="position-static mt-5">
        <Pagination pages={pages} currentPage={currentPage} />
      </div>
    </>
  );
};

export default Cards;
