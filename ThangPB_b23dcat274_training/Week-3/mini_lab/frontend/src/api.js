import axios from "axios";
const API_URL = "http://localhost:3001";

export const getUsers = () => axios.get(`${API_URL}/users`);
export const addUser = (name) => axios.post(`${API_URL}/users`, { name });
export const updateUser = (id, name) => axios.put(`${API_URL}/users/${id}`, { name });
export const deleteUser = (id) => axios.delete(`${API_URL}/users/${id}`);