// import styles from "./Home.module.css";
import { Container, Row } from "react-bootstrap";
import { ProjectCard } from "./ProjectCard";
import { v4 as uuidv4 } from "uuid";

export function DashboardPage() {
  return (
    <Container className="mt-3">
      <Row xs={1} md={2} xl={3} className="g-4">
        {Array.from({ length: 4 }).map((_) => (
          <ProjectCard key={uuidv4()} />
        ))}
      </Row>
    </Container>
  );
}
