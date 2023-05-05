import { Nav, Navbar, Container, Button } from "react-bootstrap";

export function Header() {
  return (
    <header>
      <Navbar bg="dark" variant="dark">
        <Container fluid>
          <Navbar.Brand href="#home">DeleGate</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link href="#home">Home</Nav.Link>
            <Nav.Link href="#link">Link</Nav.Link>
          </Nav>
          <Button variant="outline-info">Login</Button>
        </Container>
      </Navbar>
    </header>
  );
}
