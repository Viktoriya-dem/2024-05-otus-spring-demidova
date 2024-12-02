import { useEffect } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import BookList from "./pages/bookList/bookList";
import CreateBook from "./pages/createBook/createBook";
import { useAppDispatch } from "./hooks";
import { loadBooks } from "./store/bookReducer";
import { loadGenres } from "./store/genres";
import { loadAuthors } from "./store/authors";

function App() {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(loadBooks());
  }, []);

  useEffect(() => {
    dispatch(loadGenres());
  }, []);

  useEffect(() => {
    dispatch(loadAuthors());
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/:Books?/:bookId?" element={<BookList />}></Route>
        <Route path="/Create" element={<CreateBook />}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
