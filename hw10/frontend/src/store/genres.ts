import { PayloadAction } from "@reduxjs/toolkit";
import { createSlice } from "@reduxjs/toolkit";

interface GenresInitial {
  id: number;
  name: string;
}

interface GenresState {
  entities: GenresInitial[];
  isLoading: boolean;
  error: null;
}

const initialState: GenresState = {
  entities: [],
  isLoading: true,
  error: null,
};

const genresSlice = createSlice({
  name: "genres",
  initialState,
  reducers: {
    genresRecived(state, action: PayloadAction<any>) {
      state.entities = action.payload;
      state.isLoading = false;
    },
    genresRequestFailed(state, action: PayloadAction<any>) {
      state.error = action.payload;
      state.isLoading = false;
    },
  },
});

const { actions, reducer: genresReducer } = genresSlice;
const { genresRecived, genresRequestFailed } = actions;

export const loadGenres = () => async (dispatch: Function) => {
  try {
    const response = await fetch(`/api/genres`).then(
      (response) => response.json()
    );

    dispatch(genresRecived(response));
  } catch (error: any) {
    dispatch(genresRequestFailed(error.message));
  }
};

export const getGenres = () => (state: any) => state.genres.entities;

export default genresReducer;
