import { Col, Card, NavLink } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";

export function ProjectCard() {
  return (
    <Col>
      <LinkContainer to="/dashboard/project">
        <NavLink>
          <Card bg="info" className="h-100 mx-auto">
            <Card.Header as="h3">
              Some kind of heading or project name
            </Card.Header>
            <Card.Body>
              <Card.Title>Card title</Card.Title>
              <Card.Subtitle>Subtitle</Card.Subtitle>
              <Card.Text>
                This is a longer card with supporting text below as a natural
                lead-in to additional content. This content is a little bit
                longer.
              </Card.Text>
            </Card.Body>
            <Card.Footer>Card Footer</Card.Footer>
          </Card>
        </NavLink>
      </LinkContainer>
    </Col>
  );
}
