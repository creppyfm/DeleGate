import {
  Button,
  Card,
  Col,
  Container,
  ListGroup,
  Row,
  Spinner,
} from "react-bootstrap";
import { NavLink, useParams } from "react-router-dom";
import {
  Project,
  Step,
  Task,
  useProjectContext,
} from "../../../../utils/GetProjectData";
import { useState } from "react";
import styles from "../../default/Dashboard.module.css";

export function StepPage() {
  const { id } = useParams();
  const { project, setProject } = useProjectContext();
  const [step, setStep] = useState(
    project?.stepList.find((step) => {
      return step.id === id;
    }) as Step
  );
  const [loading, setLoading] = useState(false);

  const taskListWithData = step.taskList.map((taskId) => {
    return project?.taskList.find((task) => task.id === taskId) as Task;
  });

  async function generateTasks() {
    setLoading(true);
    try {
      const response = await fetch(
        `${import.meta.env.VITE_BACKEND_SERVER_URI}/steps/tasks/generate`,
        {
          method: "POST",
          credentials: "include",
          mode: "cors",
          body: JSON.stringify({ id }),
        }
      );
      console.log("Step Response is: ", response);

      if (response.ok) {
        const projectResponse = await fetch(
          `${import.meta.env.VITE_BACKEND_SERVER_URI}/projects/${
            step.projectId
          }`,
          { credentials: "include", mode: "cors" }
        );
        if (projectResponse.ok) {
          const data: Project = await projectResponse.json();
          setProject(data);
          setStep(data.stepList.find((step) => step.id === id) as Step);
          setLoading(false);
        } else {
          setLoading(false);
        }
      } else {
        setLoading(false);
      }
    } catch (error) {
      if (import.meta.env.DEV) {
        console.log("\x1b[93mDev console: \x1b[0m", error);
      }
    }
  }

  return (
    <Container
      className={`d-flex flex-column my-3 ${styles["dynamic-height"]}`}
    >
      <Row className="text-start">
        <NavLink
          to={`${import.meta.env.BASE_URL}/dashboard/project/${step.projectId}`}
          className="text-success text-decoration-none fs-3"
        >
          <i className="bi bi-chevron-left" /> Project
        </NavLink>
      </Row>
      <h1 className="text-light text-center mb-4">{step.title}</h1>
      <Card className="overflow-auto">
        <Card.Header as="h3">{step.description}</Card.Header>
        <Card.Body>
          <ListGroup variant="flush" className="gap-3">
            {step.taskList.length > 0 ? (
              taskListWithData.map((task: Task) => {
                let phaseColor = "";
                switch (task.phase) {
                  case "Not Started":
                    phaseColor = "text-info";
                    break;
                  case "In Progress":
                    phaseColor = "text-success";
                    break;
                  case "In Review":
                    phaseColor = "text-warning";
                    break;
                  default:
                    phaseColor = "text-primary";
                }
                return (
                  <ListGroup.Item key={task.id}>
                    <Container className="p-0 m-0">
                      <Row>
                        <Col>
                          <Row>
                            <Col className="fw-bold">
                              <Row>
                                <Col md={12} lg={9}>
                                  {task.title}
                                </Col>
                                <Col className={phaseColor}>{task.phase}</Col>
                              </Row>
                            </Col>
                            <Col xs={3}>
                              <NavLink
                                to={`${
                                  import.meta.env.BASE_URL
                                }/dashboard/task/${task.id}`}
                              >
                                <Button
                                  variant="success"
                                  className="text-light w-100"
                                >
                                  Start Task
                                </Button>
                              </NavLink>
                            </Col>
                          </Row>
                        </Col>
                      </Row>
                      <Row>
                        <Col className="d-flex align-items-center">
                          <div>{task.description}</div>
                        </Col>
                      </Row>
                    </Container>
                  </ListGroup.Item>
                );
              })
            ) : (
              <ListGroup.Item className="d-flex justify-content-center">
                <Button className="w-100" onClick={generateTasks}>
                  {loading && <Spinner className="me-3"></Spinner>}
                  {loading ? "Loading..." : "Generate Tasks"}
                </Button>
              </ListGroup.Item>
            )}
          </ListGroup>
        </Card.Body>
      </Card>
    </Container>
  );
}
