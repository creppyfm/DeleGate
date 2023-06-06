import { Nav } from "react-bootstrap";
import styles from "./Footer.module.css";

export function Footer() {
  return (
    <footer
      className={`d-flex justify-content-start align-items-center mt-auto pb-3 w-100 ${styles.footer}`}
    >
      <div className="ms-5">
        <a
          href="#"
          className="mb-3 mb-md-0 text-body-secondary text-decoration-none lh-1"
        >
          <svg
            className="bd-placeholder-img bd-placeholder-img-lg featurette-image img-fluid mx-auto"
            width="25"
            height="25"
            xmlns="http://www.w3.org/2000/svg"
            role="img"
            aria-label="Placeholder: 25x25"
            preserveAspectRatio="xMidYMid slice"
            focusable="false"
          >
            <title>Placeholder</title>
            <rect width="100%" height="100%" fill="#eee" />
          </svg>
        </a>
        <span className="ms-2 mb-3 mb-md-0 text-light">© 2023 Foli Creppy</span>
      </div>
      <Nav as="ul">
        <Nav.Item as="li">
          <Nav.Link href="https://github.com/creppyfm">
            <img src="/GitHub.svg" alt="GitHub Badge" />
          </Nav.Link>
        </Nav.Item>
        <Nav.Item as="li">
          <Nav.Link href="https://www.linkedin.com/in/creppyfm/">
            <img src="/LinkedIn.svg" alt="LinkedIn Badge" />
          </Nav.Link>
        </Nav.Item>
      </Nav>
    </footer>
  );
}
