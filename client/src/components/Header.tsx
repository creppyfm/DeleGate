import { Nav, Navbar, Container, Button } from "react-bootstrap";
import styles from "./Header.module.css";

export function Header() {
  return (
    <header className={styles.header}>
      <Navbar bg="dark" variant="dark">
        <Container fluid>
          <Navbar.Brand className="ms-2" href="#home">
            DeleGate
          </Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link href="#home">Home</Nav.Link>
            <Nav.Link href="#link">Link</Nav.Link>
          </Nav>
          <Button className="me-2" variant="outline-info">
            Login
          </Button>
        </Container>
      </Navbar>
    </header>
  );
}
