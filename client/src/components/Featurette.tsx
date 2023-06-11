import styles from "./Featurette.module.css";
import { Container, Row, Col, Image } from "react-bootstrap";

type FeaturetteProps = {
  heading?: string;
  subHeading?: string;
  content?: string;
  reversed?: boolean;
  imageURL?: string;
};

export function Featurette({
  heading,
  subHeading,
  content,
  reversed,
  imageURL,
}: FeaturetteProps) {
  const imageColumn = (
    <Col lg={5}>
      <div className="profile-image-container d-flex justify-content-center my-5">
        {imageURL ? (
          <Image src={imageURL} height={500} width={500} fluid />
        ) : (
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
        )}
      </div>
    </Col>
  );

  const textColumn = (
    <Col className="me-3 mt-5">
      {heading && (
        <h2
          className={`${styles["featurette-heading"]} ${styles["featurette-heading-dynamic"]} fw-normal lh-1 ps-3 text-light`}
        >
          {heading}
        </h2>
      )}
      {subHeading && (
        <h2 className={`${styles["featurette-heading"]} ps-3 text-secondary`}>
          {subHeading}
        </h2>
      )}
      {content && <p className="lead text-light">{content}</p>}
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
