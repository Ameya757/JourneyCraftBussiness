// store/authSlice.js
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  userId: null,
  role: null,
  email: null,
  username: null,
  isAuthenticated: false,
  message: null,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginSuccess: (state, action) => {
      const { userId, role, email, username } = action.payload;
      state.userId = userId;
      state.role = role;
      state.email = email;
      state.username = username;
      state.isAuthenticated = true;
      
    },
    logout: (state) => {
      state.userId = null;
      state.role = null;
      state.email = null;
      state.username = null;
      state.isAuthenticated = false;
        
    },
  },
});

export const { loginSuccess, logout } = authSlice.actions;
export default authSlice.reducer;
