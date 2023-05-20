import "./App.css";
import { Header } from "./components/Header";
import { Footer } from "./components/Footer";
import { Route, Routes } from "react-router-dom";
import { Home } from "./pages/Home";
import { LoginPage } from "./pages/LoginPage";
import { AboutUsPage } from "./pages/AboutUsPage";
import { useState } from "react";

export type User = {
  firstName: string;
  lastName: string;
  email: string;
  loggedIn: boolean;
};

export type SetUser = React.Dispatch<
  React.SetStateAction<{
    firstName: string;
    lastName: string;
    email: string;
    sessionId: string;
    loggedIn: boolean;
  }>
>;

function App() {
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    sessionId: "",
    loggedIn: false,
  });

  return (
    <>
      <Header user={user} setUser={setUser} />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login">
          <Route index element={<LoginPage setUser={setUser} />} />
          <Route
            path="github"
            element={<LoginPage setUser={setUser} provider="github" />}
          />
          <Route
            path="google"
            element={<LoginPage setUser={setUser} provider="google" />}
          />
        </Route>
        <Route path="/about-us" element={<AboutUsPage />} />
      </Routes>
      <Footer />
    </>
  );
}

export default App;
