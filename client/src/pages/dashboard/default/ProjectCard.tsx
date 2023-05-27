import { Col, Card, NavLink } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";

// export type Project = {
//   projectId: string;
//   title: string;
//   subtitle: string | null | undefined;
//   description: string;
//   phase: string;
//   projectMembers: string[];
//   updated: string;
// };

export type Project = {
  projectId: string;
  title: String;
  phase: string;
  updated: string;
};

export function ProjectCard({ project }: { project: Project }) {
  const { projectId, title, phase, updated } = project;
  return (
    <Col className="text-light">
      <LinkContainer to="/dashboard/project">
        <NavLink>
          <Card
            bg="secondary"
            border="secondary"
            className="h-100 mx-auto rounded-4"
          >
            <Card.Header as="h3">{title}</Card.Header>
            <Card.Body className="bg-light bg-opacity-25">
              <Card.Title className="text-info">{phase}</Card.Title>
            </Card.Body>
            <Card.Footer as="div" className="d-flex fs-5">
              <span className="text-primary">
                <i className="bi-clock-history" />{" "}
                <span className="fw-bold">{updated}</span>
              </span>
              <span className="ms-auto fs-4">
                <i className="bi-person text-primary"></i>{" "}
              </span>
            </Card.Footer>
          </Card>
        </NavLink>
      </LinkContainer>
    </Col>
  );
}
