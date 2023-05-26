import { Card, Container } from "react-bootstrap";
import { User } from "../../../App";

type ProjectPageProps = {
  user: User;
};

export function ProjectPage({ user }: ProjectPageProps) {
  console.log("Project Page user present: ", user.loggedIn);
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
