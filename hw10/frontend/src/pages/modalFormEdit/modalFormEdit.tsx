import { useEffect, useState } from "react";
import MultiSelectField from "../../components/forms/multiSelectField/multiSelectField";
import TitleField from "../../components/forms/titleField/titleField";
import { useAppDispatch, useAppSelector } from "../../hooks";
import { getGenres } from "../../store/genres";
import { getAuthors } from "../../store/authors";
import { editBookContent } from "../../store/bookReducer";

interface ModalProps {
  title: string;
  authorName: string;
  genresArr?: [];
  bookId?: number;
}

const ModalFormEdit = ({
  bookId,
  title,
  authorName,
}: ModalProps): JSX.Element => {
  const [data, setData] = useState<any>(false);

  const dispatch = useAppDispatch();

  const getBookData = async () => {
    const response = await fetch(`/api/books/${bookId}`).then(
      (response) => response.json()
    );
    setData(response);
  };

  useEffect(() => {
    getBookData();
  }, []);

  const genres = useAppSelector(getGenres());
  const authors = useAppSelector(getAuthors());

  const genresList = genres.map((g: any) => ({
    label: g.name,
    value: g.id,
  }));

  const getGenresDefault = (data: any) => {
    if (data) {
      return data.genres.map((el: any) => ({
        label: el.name,
        value: el.id,
      }));
    }
    return "Loading";
  };

  const getAuthorsDefault = (data: any) => {
    if (data) {
      let array = Object.values(data.author);
      return { label: array[1], value: array[0] };
    }
    return "Loading";
  };

  const authorsList = authors.map((a: any) => ({
    label: a.fullName,
    value: a.id,
  }));

  const handleSubmit = (e: any) => {
    e.preventDefault();
    const newData = {
      id: data.id,
      title: data.title,
      author: {
        id: data.author.value || data.author.id,
        fullName: data.author.label || data.author.fullName,
      },
      genres: data.genres.map((el: any) => ({
        id: el.id || el.value,
        name: el.name || el.label,
      })),
    };

    dispatch(editBookContent(newData));    
  };

  const handleChange = (target: any) => {
    setData((prevState: any) => ({
      ...prevState,
      [target.name]: target.value,
    }));
  };

  return (
    <div>
      {data ? (
        <form
          onSubmit={handleSubmit}
          style={{
            padding: "60px",
          }}
        >
          <TitleField
            htmlFor="exampleInputEmail1"
            label="Название книги"
            type={"text"}
            name={"title"}
            value={data.title}
            onChange={handleChange}
          />
          <MultiSelectField
            defaultValue={getAuthorsDefault(data)}
            options={authorsList}
            onChange={handleChange}
            name="author"
            label="Автор"
            isMulti={false}
          />
          <MultiSelectField
            defaultValue={getGenresDefault(data)}
            options={genresList}
            onChange={handleChange}
            name="genres"
            label="Жанр"
            isMulti={true}
          />
          <button type="submit" className="btn btn-primary">
            Изменить
          </button>
        </form>
      ) : (
        "loading"
      )}
    </div>
  );
};

export default ModalFormEdit;
