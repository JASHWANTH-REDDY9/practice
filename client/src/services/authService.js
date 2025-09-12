import api from "./api";

// Register
export async function register({ username, email, password }) {
  const res = await api.post("/api/auth/register", { username, email, password });
  return res.data;
}

// Login
// export async function login({ email, password }) {
//   const res = await api.post("/api/auth/login", { email, password });
//   // Save token for later requests
//   if (res.data.token) {
//     localStorage.setItem("token", res.data.token);
//     localStorage.setItem("username", res.data.username);
//   }
//   return res.data;
// }
export const login = async ({ email, password }) => {
  const res = await fetch("http://localhost:8080/api/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email: email.trim(), password: password.trim() }) // trim inputs
  });

  const data = await res.json();

  if (!res.ok) {
    throw { response: { data } }; // so your Login component can show error
  }

  return data; // { token, username }
};
// Protected Home
export async function getHome() {
  const res = await api.get("/api/user/home");
  return res.data;
}

// Logout
export function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("username");
}
