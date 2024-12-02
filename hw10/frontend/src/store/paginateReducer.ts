import { createSlice } from "@reduxjs/toolkit";

interface paginateState {
  entities: number;
  pageSize: number;
}

const initialState: paginateState = {
  entities: 1,
  pageSize: 4,
};

const paginateSlice = createSlice({
  name: "paginate",
  initialState,
  reducers: {},
});

const { actions, reducer: paginateReducer } = paginateSlice;

export default paginateReducer;
