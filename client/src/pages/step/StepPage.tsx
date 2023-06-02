import { Card, Container } from "react-bootstrap";
import { useParams } from "react-router-dom";

export function StepPage() {
  const { id } = useParams();
  return (
    <Container>
      <Card>
        <Card.Title>{id}</Card.Title>
      </Card>
    </Container>
  );
}
