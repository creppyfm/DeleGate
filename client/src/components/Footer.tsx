import { Nav } from "react-bootstrap";

export function Footer() {
  return (
    <footer
      className={
        "d-flex justify-content-center align-items-center mt-auto py-4 w-100"
      }
    >
      <Nav as="ul" className="fs-5">
        <Nav.Item as="li">
          <Nav.Link href="https://github.com/creppyfm" target="_blank">
            <i className="bi bi-github text-light" />
          </Nav.Link>
        </Nav.Item>
        <Nav.Item as="li">
          <Nav.Link
            href="https://tinyurl.com/foliresume"
            target="_blank"
            className="text-info ps-0"
          >
            Foli Creppy
          </Nav.Link>
        </Nav.Item>
        <Nav.Item as="li">
          <Nav.Link
            className="ps-0"
            target="_blank"
            href="https://www.linkedin.com/in/creppyfm/"
          >
            <i className="bi bi-linkedin text-light" />
          </Nav.Link>
        </Nav.Item>
      </Nav>
      <i className="bi bi-arrow-left-right text-light mx-3" />
      <Nav as="ul" className="fs-5">
        <Nav.Item as="li">
          <Nav.Link href="https://github.com/kensonjohnson" target="_blank">
            <i className="bi bi-github text-light" />
          </Nav.Link>
        </Nav.Item>
        <Nav.Item as="li">
          <Nav.Link
            href="https://kensonjohnson.com"
            target="_blank"
            className="text-info ps-0"
          >
            Kenson Johnson
          </Nav.Link>
        </Nav.Item>
        <Nav.Item as="li">
          <Nav.Link
            className="ps-0"
            target="_blank"
            href="https://www.linkedin.com/in/kensonjohnson/"
          >
            <i className="bi bi-linkedin text-light" />
          </Nav.Link>
        </Nav.Item>
      </Nav>
    </footer>
  );
}
