import { Nav, Navbar, Container, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
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
            <Link to={"/"} className="nav-link">
              Home
            </Link>
            <Link to={"/about-us"} className="nav-link">
              About Us
            </Link>
          </Nav>
          <Button
            variant="outline-success"
            as="a"
            href="/dashboard"
            className="me-3"
          >
            Dashboard
          </Button>
          {user.loggedIn ? (
            <Button variant="outline-danger" onClick={logoutUser}>
              Logout
            </Button>
          ) : (
            <Link to={"/login"} className="me-2 btn btn-outline-info">
              Login
            </Link>
          )}
        </Container>
      </Navbar>
    </header>
  );
}
