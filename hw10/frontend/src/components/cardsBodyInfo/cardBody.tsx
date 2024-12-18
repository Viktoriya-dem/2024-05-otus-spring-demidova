interface CardBodyProps {
  title: string;
  authorName: string;
  genres: [];
}

const CardBodyInfo = ({ title, authorName, genres }: CardBodyProps) => {
  return (
    <div>
      <div className="card-header mb-3">
        <h6>Название книги:</h6>
        <div>{title}</div>
      </div>
      <div className="card-body">
        <div className="card-title mb-3">
          <h6>Автор</h6>
          <div> {authorName}</div>
        </div>
        <div className="card-text">
          <h6>Жанр:</h6>
          <div>{genres.map((el: any) => el.name)}</div>
        </div>
      </div>
    </div>
  );
};

export default CardBodyInfo;
