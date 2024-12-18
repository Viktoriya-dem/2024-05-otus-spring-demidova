import { configureStore } from "@reduxjs/toolkit";
import booksReducer from "./bookReducer";
import genresReducer from "./genres";
import authorsReducer from "./authors";
import commentsReducer from "./comment";

const store = configureStore({
  reducer: {
    books: booksReducer,
    genres: genresReducer,
    authors: authorsReducer,
    comments: commentsReducer,
  },
});

export default store;

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
