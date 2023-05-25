import { Nav, Navbar, Container, Button } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import styles from "./Header.module.css";
import { useAppContext } from "../utils/SessionContext";

export function Header() {
  const { user, setUser } = useAppContext();
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
          <NavLink to="/dashboard" className="me-3 btn btn-outline-success">
            Dashboard
          </NavLink>
          {user.loggedIn ? (
            <Button variant="outline-danger" onClick={logoutUser}>
              Logout
            </Button>
          ) : (
            <NavLink to={"/login"} className="me-2 btn btn-outline-info">
              Login
            </NavLink>
          )}
        </Container>
      </Navbar>
    </header>
  );
}
