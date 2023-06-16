import { Nav, Navbar, Container, Button, Image } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import styles from "./Header.module.css";
import { useAppContext } from "../utils/GetUserData";
import { LinkContainer } from "react-router-bootstrap";

export function Header() {
  const { user, setUser } = useAppContext();

  function logoutUser() {
    fetch(`${import.meta.env.VITE_BACKEND_SERVER_URI}/users/logout`, {
      credentials: "include",
      mode: "cors",
    });
    setUser({
      firstName: "",
      lastName: "",
      email: "",
      loggedIn: false,
    });
  }

  return (
    <header className={styles.header}>
      <Navbar bg="dark" variant="dark">
        <Container fluid>
          <Navbar.Brand className="ms-2" href="#home">
            <Image
              src={`${import.meta.env.BASE_URL}brand_dark_trans_thin.png`}
              fluid
            />
          </Navbar.Brand>
          <Nav className="me-auto">
            <NavLink to={import.meta.env.BASE_URL} className="nav-link">
              Home
            </NavLink>
            <NavLink
              to={import.meta.env.BASE_URL + "about-us"}
              className="nav-link"
            >
              About Us
            </NavLink>
          </Nav>
          {import.meta.env.DEV && user.loggedIn && (
            <NavLink
              target="_blank"
              to={
                import.meta.env.VITE_BACKEND_SERVER_URI +
                "/swagger-ui/index.html"
              }
              className="btn btn-outline-info me-3"
            >
              Docs
            </NavLink>
          )}
          {user.loggedIn && (
            <NavLink
              to={import.meta.env.BASE_URL + "dashboard"}
              className="btn btn-outline-success me-3"
            >
              Dashboard
            </NavLink>
          )}

          {user.loggedIn ? (
            <Button
              variant="outline-danger"
              className="me-2"
              onClick={logoutUser}
            >
              Logout
            </Button>
          ) : (
            <LinkContainer to={import.meta.env.BASE_URL + "login"}>
              <Button variant="outline-warning" className="me-2">
                Login
              </Button>
            </LinkContainer>
          )}
        </Container>
      </Navbar>
    </header>
  );
}
