import { Col, Card, NavLink } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";

type Project = {
  projectId: string;
  title: string;
  subtitle: string | null | undefined;
  description: string;
  phase: string;
  projectMembers: string[];
  updated: string;
};

export function ProjectCard({ project }: { project: Project }) {
  const {
    // projectId,
    title,
    subtitle,
    description,
    phase,
    projectMembers,
    updated,
  } = project;
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
              {subtitle && <Card.Subtitle>{subtitle}</Card.Subtitle>}
              <Card.Text className="fs-5">{description}</Card.Text>
            </Card.Body>
            <Card.Footer as="div" className="d-flex fs-5">
              <span className="text-primary">
                <i className="bi-clock-history" />{" "}
                <span className="fw-bold">{updated}</span>
              </span>
              <span className="ms-auto fs-4">
                <i className="bi-person text-primary"></i>{" "}
                <span className=" text-warning">{projectMembers.length}</span>
              </span>
            </Card.Footer>
          </Card>
        </NavLink>
      </LinkContainer>
    </Col>
  );
}
