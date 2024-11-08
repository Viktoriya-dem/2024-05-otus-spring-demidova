import { useAppDispatch } from "../../hooks";
import { removeComment } from "../../store/comment";
import styles from "./commentsList.module.css";

interface CommentsListProps {
  comments: any;
  onRemove: Function;
}

const CommentsList = ({
  comments,
  onRemove,
}: CommentsListProps): JSX.Element => {
  const dispatch = useAppDispatch();

  return (
    <ul className={styles.border}>
      {comments.map((el: any) => (
        <li className="mb-3 d-flex justify-content-between" key={el.id}>
          {el.text}
          <button onClick={()=>onRemove(el.id)} className="btn btn-danger">Удалить</button>
        </li>
      ))}
    </ul>
  );
};

export default CommentsList;
