import { Card, Container } from "react-bootstrap";

export function ProjectPage() {
  return (
    <Container>
      <Card bg="light" className="rounded-4">
        <Card.Header as="h3">This is the card header!</Card.Header>
        <Card.Body>
          <Card.Title>Special title treatment</Card.Title>
          <Card.Text>
            With supporting text below as a natural lead-in to additional
            content.
          </Card.Text>
        </Card.Body>
      </Card>
    </Container>
  );
}
