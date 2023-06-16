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
import { AppContext, User } from "./utils/GetUserData";
import { Container } from "react-bootstrap";

function App() {
  const [user, setUser] = useState<User>({
    firstName: "",
    lastName: "",
    email: "",
    loggedIn: false,
  });

  return (
    <AppContext.Provider value={{ user: user, setUser: setUser }}>
      <Container fluid="xl" className="p-0 m-auto d-flex flex-column vh-100">
        <Header />
        <Routes>
          <Route path={import.meta.env.BASE_URL} element={<Home />} />
          <Route
            path={import.meta.env.BASE_URL + "login"}
            element={<LoginPage />}
          />
          <Route
            path={import.meta.env.BASE_URL + "about-us"}
            element={<AboutUsPage />}
          />
          <Route
            path={import.meta.env.BASE_URL + "dashboard/*"}
            element={
              <ProtectedRoute user={user}>
                <DashboardRouter />
              </ProtectedRoute>
            }
          />
          <Route
            path={import.meta.env.BASE_URL + "404"}
            element={<PageNotFound />}
          />
          <Route
            path="*"
            element={<Navigate to={import.meta.env.BASE_URL + "404"} />}
          />
        </Routes>
        <Footer />
      </Container>
    </AppContext.Provider>
  );
}

export default App;
