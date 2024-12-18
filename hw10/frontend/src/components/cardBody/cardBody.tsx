interface CardBodyProps {
  title: string;
  authorName: string;
  genres: [];
}

const CardBody = ({ title, authorName, genres }: CardBodyProps) => {
  return (
    <div>
      <div className="card-header">{title}</div>
      <div className="card-body">
        <h5 className="card-title">{authorName}</h5>
        <p className="card-text">{genres.map((el: any) => el.name)}</p>
      </div>      
    </div>
  );
};

export default CardBody;
