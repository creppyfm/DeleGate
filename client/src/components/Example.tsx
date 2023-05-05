import Alert from "react-bootstrap/Alert";
import { Container, Row, Col, Button } from "react-bootstrap";

export function Example() {
  return (
    <>
      <Alert dismissible variant="primary">
        <Alert.Heading>Oh snap! You got an alert!</Alert.Heading>
        <p>Now you get to read this cool message.</p>
      </Alert>

      <Container>
        <Row>
          <Col>
            <p className="text-primary fs-5">.text-primary</p>
          </Col>
          <Col>
            <p>
              <Button variant="primary">primary</Button>
            </p>
          </Col>
        </Row>
        <Row>
          <Col>
            <p className="text-secondary fs-5">.text-secondary</p>
          </Col>
          <Col>
            <p>
              <Button variant="secondary">secondary</Button>
            </p>
          </Col>
        </Row>
        <Row>
          <Col>
            <p className="text-success fs-5">.text-success</p>
          </Col>
          <Col>
            <p>
              <Button variant="success">success</Button>
            </p>
          </Col>
        </Row>
        <Row>
          <Col>
            <p className="text-danger fs-5">.text-danger</p>
          </Col>
          <Col>
            <p>
              <Button variant="danger">danger</Button>
            </p>
          </Col>
        </Row>
        <Row>
          <Col>
            <p className="text-warning fs-5">.text-warning</p>
          </Col>
          <Col>
            <p>
              <Button variant="warning">warning</Button>
            </p>
          </Col>
        </Row>
        <Row>
          <Col>
            <p className="text-info fs-5">.text-info</p>
          </Col>
          <Col>
            <p>
              <Button variant="info">info</Button>
            </p>
          </Col>
        </Row>
        <Row className="bg-black">
          <Col>
            <p className="text-light fs-5">.text-light</p>
          </Col>
          <Col>
            <p>
              <Button variant="light">light</Button>
            </p>
          </Col>
        </Row>
        <Row>
          <Col>
            <p className="text-dark fs-5">.text-dark</p>
          </Col>
          <Col>
            <p>
              <Button variant="dark">dark</Button>
            </p>
          </Col>
        </Row>
      </Container>
    </>
  );
}
