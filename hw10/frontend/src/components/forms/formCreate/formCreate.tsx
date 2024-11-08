import { useState, useEffect } from "react";
import { ComponentProps } from "react";
import TitleField from "../titleField/titleField";
import { useAppDispatch, useAppSelector } from "../../../hooks";
import { createBook, getBooks } from "../../../store/bookReducer";
import { getGenres } from "../../../store/genres";
import { getAuthors } from "../../../store/authors";
import MultiSelectField from "../multiSelectField/multiSelectField";
import MultiSelectFieldAuthors from "../multiSelectAuthors/multiSelectAuthors";
import { v4 as uuidv4 } from "uuid";

const FormCreate = (): JSX.Element => {
  const [data, setData] = useState({
    title: "",
    author: "",
    genres: [],
  });  

  const [titleDirty, setTitleDirty] = useState<boolean>(false);
  const [titleError, setTitleError] = useState<string>(
    "Заголовок не может быть пустым"
  );

  const [authorDirty, setAuthorDirty] = useState<boolean>(false);
  const [authorError, setAuthorError] = useState<string>(
    "Вы не выбрали автора"
  );

  const [formValid, setFormValid] = useState<boolean>(false);


  useEffect(() => {
    if (titleError || authorError) {
      setFormValid(false);
    } else {
      setFormValid(true);
    }
  }, [titleError, authorError]);

  const titleHandler = (target: any) => {
    setData((prevState) => ({
      ...prevState,
      [target.name]: target.value,
    }));

    if (!target.value) {
      setTitleError("Заголовок не может быть пустым");
    } else {
      setTitleError("");
    }
  };

  const authorHandler = (target: any) => {
    setData((prevState) => ({
      ...prevState,
      [target.name]: target.value,
    }));   
    if (!target.value) {
      setAuthorError("Вы не выбрали автора");
    } else {
      setAuthorError("");
    }
  };

  const handleBlur = ({ target }: any) => {
    switch (target.name) {
      case "title":
        setTitleDirty(true);
        break;
      case "authorId":
        setAuthorDirty(true);
        break;
    }
  };  

  const genres = useAppSelector(getGenres());
  const authors = useAppSelector(getAuthors());   


  const genresList = genres.map((g: any) => ({
    label: g.name,
    value: g.id,
  }));

  const dispatch = useAppDispatch();

  const handleChange = (target: any) => {
    setData((prevState) => ({
      ...prevState,
      [target.name]: target.value,
    }));    
  };  
  

  const handleSubmit = (e: any) => {
    e.preventDefault();
    const newData = {
      id: uuidv4(),
      title: data.title,
      author: authors.filter((el: any) => el.id === data.author)[0],
      genres: data.genres.map((el: any) => ({
        id: el.value,
        name: el.label,
      })),
    };
    dispatch(createBook(newData));
  };  

  return (
    <>
      <form
        style={{ width: "600px" }}
        className="was-validated"
        onSubmit={handleSubmit}
      >
        <TitleField
          label="Название книги"
          type="text"
          name="title"
          value={data.title}
          onChange={titleHandler}
          onBlur={handleBlur}
          titleDirty={titleDirty}
          titleError={titleError}
        />
        <MultiSelectFieldAuthors
          value={data.author}
          options={authors}
          onChange={authorHandler}
          onBlur={handleBlur}
          name="author"
          label="Автор"
          authorDirty={authorDirty}
          authorError={authorError}
        />
        <MultiSelectField
          defaultValue={data.genres}
          options={genresList}
          onChange={handleChange}
          name="genres"
          label="Жанр"
          isMulti={true}
        />
        <button type="submit" className="btn btn-primary" disabled={!formValid}>
          Добавить
        </button>
      </form>
    </>
  );
};

export default FormCreate;
