import api from "./api";

export async function register({ username, email, password }) {
  const res = await api.post("/api/auth/register", { username, email, password });
  return res.data;
}

export async function login({ email, password }) {
  const res = await api.post("/api/auth/login", { email, password });
  return res.data; // expects { token, username }
}

export async function getHome() {
  const res = await api.get("/api/user/home");
  return res.data;
}
