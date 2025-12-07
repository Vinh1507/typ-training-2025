import { configureStore } from "@reduxjs/toolkit";
import rootReducer from "./reducers"; // tất cả các reducer

const store = configureStore({
  reducer: rootReducer
});

export default store;