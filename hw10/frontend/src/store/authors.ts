import { PayloadAction } from "@reduxjs/toolkit";
import { createSlice } from "@reduxjs/toolkit";

interface AuthorsInitial {
  id: number;
  fullName: string;
}

interface AuthorsState {
  entities: AuthorsInitial[];
  isLoading: boolean;
  error: null;
}

const initialState: AuthorsState = {
  entities: [],
  isLoading: true,
  error: null,
};

const authorsSlice = createSlice({
  name: "authors",
  initialState,
  reducers: {
    authorsRecived(state, action: PayloadAction<any>) {
      state.entities = action.payload;
      state.isLoading = false;
    },
    authorsRequestFailed(state, action: PayloadAction<any>) {
      state.error = action.payload;
      state.isLoading = false;
    },
  },
});

const { actions, reducer: authorsReducer } = authorsSlice;
const { authorsRecived, authorsRequestFailed } = actions;

export const loadAuthors = () => async (dispatch: Function) => {
  try {
    const response = await fetch(`/api/authors`).then(
      (response) => response.json()
    );

    dispatch(authorsRecived(response));
  } catch (error: any) {
    dispatch(authorsRequestFailed(error.message));
  }
};

export const getAuthors = () => (state: any) => state.authors.entities;

export default authorsReducer;
