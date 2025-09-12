import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080", // backend Spring Boot server
  headers: {
    "Content-Type": "application/json",
  },
});

// Add JWT token from localStorage to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
