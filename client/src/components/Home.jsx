import React, { useEffect, useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { getHome } from "../services/authService";

export default function Home() {
  const { user, logout } = useAuth();
  const [message, setMessage] = useState("");

  useEffect(() => {
    async function fetch() {
      try {
        const res = await getHome();
        setMessage(res.message || JSON.stringify(res));
      } catch (err) {
        setMessage("Unable to fetch home");
      }
    }
    fetch();
  }, []);

  return (
    <div className="card">
      <h2>Home</h2>
      <p>{message}</p>
      <p>Signed in as: {user?.username}</p>
      <button onClick={() => { logout(); }}>Logout</button>
    </div>
  );
}
