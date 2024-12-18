import { useEffect } from "react";
import BtnBack from "../../components/btnBack/btnBack";
import CardBodyInfo from "../../components/cardsBodyInfo/cardBody";
import AddCommentForm from "../../components/common/comments/addCommentForm";
import { useAppDispatch, useAppSelector } from "../../hooks";
import { getBookById } from "../../store/bookReducer";
import { createComment, getComment, loadComments, removeComment } from "../../store/comment";
import { v4 as uuidv4 } from "uuid";
import CommentsList from "../../components/commentsList/commentsList";

interface BookInfoProps {
  idBook: string;
}

const BookInfo = ({ idBook }: BookInfoProps) => {
  const dispatch = useAppDispatch();
  const books = useAppSelector(getBookById(idBook));
  const comments = useAppSelector(getComment());

  console.log(comments);

  useEffect(() => {
    dispatch(loadComments(idBook));
  }, [idBook]);

  const handleSubmit = (data: any) => {
    dispatch(createComment({ id: uuidv4(), bookId: idBook, ...data }));
    // console.log({ bookid: idBook, ...data });
  };

  const handleRemoveComments = (id: string) => {
    dispatch(removeComment(id));
  };

  return (
    <div className="mt-5">
      <div className="d-flex flex-column">
        <div className="d-flex justify-content-between">
          {books ? (
            <div className="me-5">
              <CardBodyInfo
                title={books.title}
                authorName={books.author.fullName}
                genres={books.genres}
              />
            </div>
          ) : (
            <div>Loading</div>
          )}
        </div>

        <div className="mt-5">
          <AddCommentForm onSubmit={handleSubmit} />
        </div>

        <CommentsList comments={comments} onRemove={handleRemoveComments} />
      </div>

      <div className="mt-5">
        <BtnBack push={"Books"} />
      </div>
    </div>
  );
};

export default BookInfo;
