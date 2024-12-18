
import { PaginationProps } from "./pagination.props";
import { useAppDispatch } from "../../hooks";
import { handlePageBook } from "../../store/bookReducer";
import { pagesArray } from "../../utils/paginate";


const Pagination = ({ currentPage, pages }: PaginationProps): JSX.Element => {
  const dispatch = useAppDispatch();




  return (
    <nav aria-label="Page navigation example">
      <ul className="pagination d-flex justify-content-center">
        <li className="page-item">
          <a className="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        {pages.map((page) => (
          <li
            key={"page " + page}
            className="page-item"
            onClick={() => dispatch(handlePageBook(page))}
          >
            <a className="page-link" href="#">
              {page}
            </a>
          </li>
        ))}
        <li className="page-item">
          <a className="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  );
};

export default Pagination;
