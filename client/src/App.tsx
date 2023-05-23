import "./App.css";
import { Header } from "./components/Header";
import { Footer } from "./components/Footer";
import { Route, Routes } from "react-router-dom";
import { Home } from "./pages/Home";
import { LoginPage } from "./pages/LoginPage";
import { AboutUsPage } from "./pages/AboutUsPage";
import { useState, useEffect } from "react";
import { AppContext } from "./utils/SessionContext";

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
    loggedIn: boolean;
  }>
>;

export interface AppContextProps {
  user: User;
  setUser: (user: User) => void;
}

function App() {
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    loggedIn: false,
  });

  useEffect(() => {});

  return (
    <AppContext.Provider value={{ user, setUser }}>
      <Header user={user} setUser={setUser} />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/about-us" element={<AboutUsPage />} />
      </Routes>
      <Footer />
    </AppContext.Provider>
  );
}

export default App;
