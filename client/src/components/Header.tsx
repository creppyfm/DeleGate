import { Nav, Navbar, Container, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import styles from "./Header.module.css";

import { User, SetUser } from "../App";

type HeaderProps = {
  user: User;
  setUser: SetUser;
};

export function Header({ user, setUser }: HeaderProps) {
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
          {user.loggedIn ? (
            <Button variant="outline-info" onClick={logoutUser}>
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
