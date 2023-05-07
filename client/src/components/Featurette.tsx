import styles from "./Featurette.module.css";
import { Container, Row, Col } from "react-bootstrap";

type FeaturetteProps = { reversed: boolean };

export function Featurette({ reversed }: FeaturetteProps) {
  const imageColumn = (
    <Col lg={5}>
      <div className="profile-image-container d-flex justify-content-center rounded-circle my-5">
        <svg
          className="bd-placeholder-img bd-placeholder-img-lg featurette-image img-fluid mx-auto"
          width="500"
          height="500"
          xmlns="http://www.w3.org/2000/svg"
          role="img"
          aria-label="Placeholder: 500x500"
          preserveAspectRatio="xMidYMid slice"
          focusable="false"
        >
          <title>Placeholder</title>
          <rect width="100%" height="100%" fill="#aaa" />
          <text x="50%" y="50%" fill="#eee" dy=".3em">
            500x500
          </text>
        </svg>
      </div>
    </Col>
  );

  const textColumn = (
    <Col className="me-5">
      <h2
        className={`${styles["featurette-heading"]} fw-normal lh-1 ps-3 text-light`}
      >
        Some Content <br />
        <span className="text-secondary">With some more text.</span>
      </h2>
      <p className="lead ps-3 text-secondary">
        Some great placeholder content for the first featurette here. Imagine
        some exciting prose here.
      </p>
    </Col>
  );

  return (
    <section>
      <Container>
        <Row>
          {reversed ? textColumn : imageColumn}
          {reversed ? imageColumn : textColumn}
        </Row>
      </Container>
    </section>
  );
}
