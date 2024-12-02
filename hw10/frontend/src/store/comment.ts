import { PayloadAction, createAction, createSlice } from "@reduxjs/toolkit";

interface CommentInitial {
  id: number;
  text: string;
  bookId: number;
}

interface CommentState {
  entities: CommentInitial[];
  isLoading: boolean;
  error: null;
}

const initialState: CommentState = {
  entities: [],
  isLoading: true,
  error: null,
};

const commentsSlice = createSlice({
  name: "comments",
  initialState,
  reducers: {
    commentsRequested: (state) => {
      state.isLoading = true;
    },
    commentsReceived: (state, action: PayloadAction<any>) => {
      state.entities = action.payload;
      state.isLoading = false;
    },
    commentCreate: (state, action: PayloadAction<any>) => {
      state.entities.push(action.payload);
    },
    commentRemove: (state, action: PayloadAction<any>) => {
      state.entities = state.entities.filter((c) => c.id !== action.payload);
    },
    commentsRequestFailed: (state, action: PayloadAction<any>) => {
      state.error = action.payload;
      state.isLoading = false;
    },
  },
});

const { reducer: commentsReducer, actions } = commentsSlice;

const {
  commentsRequested,
  commentsReceived,
  commentCreate,
  commentRemove,
  commentsRequestFailed,
} = actions;

const addCommentRequested = createAction("comments/addCommentRequested");
const removeCommentRequested = createAction("comments/removeCommentRequested");

export const loadComments =
  (bookId: number | string) => async (dispatch: Function) => {
    dispatch(commentsRequested());
    try {
      const response = await fetch(
        `/api/comments/book/${bookId}`
      ).then((response) => response.json());
      dispatch(commentsReceived(response));
    } catch (error: any) {
      dispatch(commentsRequestFailed(error.message));
    }
  };

export const createComment =
  (payload: any) => async (dispatch: Function, getState: any) => {
    dispatch(addCommentRequested(payload));
    try {
      const comment = {
        ...payload,
      };
      console.log(comment);

      await fetch(`/api/comments`, {
        headers: {
          "Content-Type": "application/json",
        },
        method: "POST",
        body: JSON.stringify(payload),
      });
      dispatch(commentCreate(comment));
    } catch (error: any) {
      dispatch(commentsRequestFailed(error.message));
    }
  };

export const removeComment = (id: string) => async (dispatch: Function) => {
  dispatch(removeCommentRequested());

  try {
    await fetch(`/api/comments/${id}`, {
      method: "DELETE",
    });
    dispatch(commentRemove(id));
  } catch (error: any) {
    dispatch(commentsRequestFailed(error.message));
  }
};

export const getComment = () => (state: any) => {
  return state.comments.entities;
};

export default commentsReducer;
