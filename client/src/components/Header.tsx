import { Nav, Navbar, Container, Button } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import styles from "./Header.module.css";
import { useAppContext } from "../utils/SessionContext";
import { LinkContainer } from "react-router-bootstrap";

export function Header() {
  const { user, setUser } = useAppContext();

  function fakeLogin() {
    setUser({
      firstName: "Kenson",
      lastName: "Johnson",
      email: "kenson.johnson@outlook.com",
      loggedIn: true,
    });
  }

  function logoutUser() {
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
            DeleGate
          </Navbar.Brand>
          <Nav className="me-auto">
            <NavLink to={"/"} className="nav-link">
              Home
            </NavLink>
            <NavLink to={"/about-us"} className="nav-link">
              About Us
            </NavLink>
          </Nav>
          {user.loggedIn ? (
            <NavLink to="/dashboard" className="btn btn-outline-success me-3">
              Dashboard
            </NavLink>
          ) : (
            <Button
              variant="outline-success"
              className="me-3"
              onClick={fakeLogin}
            >
              Fake Login
            </Button>
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
            <LinkContainer to="/login">
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
