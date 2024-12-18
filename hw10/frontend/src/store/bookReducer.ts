import { PayloadAction, createAction, createSlice } from "@reduxjs/toolkit";

interface BookInitial {
  id: number;
  title: string;
  authorName: string;
  genres: string | number[];
}

interface BookState {
  entities: BookInitial[];
  isLoading: boolean;
  error: null;
  pagination: number[] | number;
}

const initialState: BookState = {
  entities: [],
  isLoading: true,
  error: null,
  pagination: 1,
};

const bookSlice = createSlice({
  name: "book",
  initialState,
  reducers: {
    recived(state, action: PayloadAction<any>) {
      state.entities = action.payload;
      state.isLoading = false;
    },
    bookCreated(state, action: PayloadAction<any>) {
      state.entities.push(action.payload);
    },
    bookRequestFailed(state, action: PayloadAction<any>) {
      state.error = action.payload;
      state.isLoading = false;
    },
    bookRemove(state, action: PayloadAction<any>) {
      state.entities = state.entities.filter((c) => c.id !== action.payload);
    },
    paginationBooks(state, action: PayloadAction<any>) {
      state.pagination = action.payload;
    },
    bookEdit(state, action: PayloadAction<any>) {
      state.entities[
        state.entities.findIndex((u) => u.id === action.payload.id)
      ] = action.payload;
    },
  },
});

const { actions, reducer: booksReducer } = bookSlice;
const {
  recived,
  bookCreated,
  bookRequestFailed,
  bookRemove,
  paginationBooks,
  bookEdit,
} = actions;

const bookCreateRequested = createAction("book/bookCreateRequested");
const createBookFailed = createAction("book/createBookFailed");
const editBookRequest = createAction("book/editBookRequest");
const editeBookFailed = createAction("book/editeBookFailed");

export const loadBooks = () => async (dispatch: Function) => {
  try {
    const response = await fetch(`/api/books`).then(
      (response) => response.json()
    );

    dispatch(recived(response));
  } catch (error: any) {
    dispatch(bookRequestFailed(error.message));
  }
};

export const handlePageBook = (pageIndex: number) => (dispatch: Function) => {
  dispatch(paginationBooks(pageIndex));
};

export const createBook = (payload: any) => async (dispatch: Function) => {
  dispatch(bookCreateRequested());
  try {
    await fetch(`/api/books`, {
      headers: {
        "Content-Type": "application/json",
      },
      method: "POST",
      body: JSON.stringify({
        ...payload,
      }),
    });
    dispatch(bookCreated(payload));
  } catch (error: any) {
    dispatch(createBookFailed(error.message));
  }
};

export const editBookContent = (payload: any) => async (dispatch: Function) => {
  dispatch(editBookRequest());
  try {
    await fetch(`/api/books/${payload.id}`, {
      headers: {
        "Content-Type": "application/json",
      },
      method: "PATCH",
      body: JSON.stringify({
        ...payload,
      }),
    });
    dispatch(bookEdit(payload));
  } catch (error: any) {
    dispatch(editeBookFailed(error.message));
  }
};

export const createBookStore = (content: any) => (dispatch: Function) => {};

export const removeBook = (id: number) => (dispatch: Function) => {
  dispatch(bookRemove(id));
  fetch(`/api/books/${id}`, {
    method: "DELETE",
  });
};

export const getBooks = () => (state: any) => {
  return state.books.entities;
};
export const getBookById = (bookId: string) => (state: any) => {
  if (state.books.entities) {
    return state.books.entities.find((book: any) => book.id == bookId);
  }
};

export const getCurrentPage = () => (state: any) => {
  return state.books.pagination;
};

export default booksReducer;
