import { useState, useEffect } from "react";
import {
  Card,
  Col,
  Container,
  ListGroup,
  Row,
  Spinner,
  Stack,
} from "react-bootstrap";
import { NavLink, useParams } from "react-router-dom";
import {
  Project,
  Step,
  useProjectContext,
} from "../../../utils/GetProjectData";
import styles from "../default/Dashboard.module.css";

export function ProjectPage() {
  const [loading, setLoading] = useState(true);
  const { id } = useParams();
  const { project, setProject } = useProjectContext();

  async function fetchProject() {
    try {
      const response = await fetch(
        `${import.meta.env.VITE_BACKEND_SERVER_URI}/projects/${id}`,
        { credentials: "include", mode: "cors" }
      );
      if (response.ok) {
        const data: Project = await response.json();
        setProject(data);
        setLoading(false);
      }
    } catch (error) {
      console.log(error);
    }
  }

  useEffect(() => {
    if ((!project && id) || (project && project.id !== id)) {
      fetchProject();
    } else {
      setLoading(false);
    }
  }, [project]);

  return (
    <Container
      className={`d-flex flex-column my-3 ${styles["dynamic-height"]}`}
    >
      <Row className="text-start">
        <NavLink
          to={import.meta.env.BASE_URL + "/dashboard"}
          className="text-success text-decoration-none fs-3"
        >
          <i className="bi bi-chevron-left" /> Dashboard
        </NavLink>
      </Row>
      <Row>
        <h1 className="text-center text-light mb-5">
          {project && !loading ? project.title : "Loading Project..."}
        </h1>
      </Row>

      <Row className="mb-1 d-flex overflow-auto h-100">
        <Col lg={12} xl={6}>
          <Stack direction="vertical" className="mb-4" gap={4}>
            <Card className="rounded-4">
              <Card.Header as="h3">Summary</Card.Header>
              <Card.Body>
                <Card.Title></Card.Title>
                <Card.Text>
                  {project && !loading
                    ? project.description
                    : "Loading Project..."}
                </Card.Text>
              </Card.Body>
              <Card.Footer>
                <div className="d-flex justify-content-between">
                  <span>Phase: {project && !loading && project.phase}</span>
                  <span>
                    Updated:{" "}
                    {project &&
                      !loading &&
                      new Date(project.updated).toLocaleString()}
                  </span>
                </div>
              </Card.Footer>
            </Card>

            <ListGroup variant="flush" className="rounded-4">
              {project && !loading ? (
                project.stepList.map((step) => {
                  const { id, title }: Step = step;
                  return (
                    <ListGroup.Item className="p-0" key={id}>
                      <NavLink
                        to={`${import.meta.env.BASE_URL}/dashboard/step/${id}`}
                        className="text-decoration-none"
                      >
                        <div className="w-100 h-100 p-2 ps-3">{title}</div>
                      </NavLink>
                    </ListGroup.Item>
                  );
                })
              ) : (
                <ListGroup.Item>
                  <div className="d-flex text-warning">
                    <Spinner
                      animation="border"
                      role="status"
                      className="me-3"
                    />
                    <h3>Loading... </h3>
                  </div>
                </ListGroup.Item>
              )}
            </ListGroup>
          </Stack>
        </Col>
        <Col className="d-flex flex-column" lg={12} xl={6}>
          <ListGroup variant="flush" className="rounded-4 overflow-auto">
            <ListGroup.Item>
              <h3>Tasks</h3>
            </ListGroup.Item>
            {project?.taskList.map((task) => {
              return (
                <ListGroup.Item key={task.id} className="p-0" action>
                  <NavLink
                    to={`${import.meta.env.BASE_URL}/dashboard/task/${task.id}`}
                    className="text-decoration-none"
                  >
                    <div className="w-100 h-100 p-2 ps-3">{task.title}</div>
                  </NavLink>
                </ListGroup.Item>
              );
            })}
          </ListGroup>
        </Col>
      </Row>
    </Container>
  );
}
