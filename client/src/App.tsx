import "./App.css";
import { Header } from "./components/Header";
import { Footer } from "./components/Footer";
import { Navigate, Route, Routes } from "react-router-dom";
import { Home } from "./pages/Home";
import { LoginPage } from "./pages/LoginPage";
import { AboutUsPage } from "./pages/AboutUsPage";
import { ProtectedRoute } from "./utils/routeProtection";
import { useState } from "react";
import { DashboardRouter } from "./pages/dashboard/DashboardRouter";
import { PageNotFound } from "./pages/PageNotFound";
import { AppContext } from "./utils/GetUserData";

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

  return (
    <AppContext.Provider value={{ user: user, setUser: setUser }}>
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/about-us" element={<AboutUsPage />} />
        <Route
          path="/dashboard/*"
          element={
            <ProtectedRoute user={user}>
              <DashboardRouter />
            </ProtectedRoute>
          }
        />
        <Route path="/404" element={<PageNotFound />} />
        <Route path="*" element={<Navigate to="/404" />} />
      </Routes>
      <Footer />
    </AppContext.Provider>
  );
}

export default App;
