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

// type Step = {
//   id: string;
//   projectId: string;
//   title: string;
//   description: string;
//   taskList: string[];
// };

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
      const response = await fetch("/steps/tasks/generate", {
        method: "POST",
        credentials: "include",
        mode: "cors",
        body: JSON.stringify({ id }),
      });
      console.log("Step Response is: ", response);

      if (response.ok) {
        const projectResponse = await fetch(`/projects/${step.projectId}`);
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
      console.log(error);
    }
  }

  return (
    <Container
      className={`d-flex flex-column my-3 ${styles["dynamic-height"]}`}
    >
      <Row className="text-start">
        <NavLink
          to={`/dashboard/project/${step.projectId}`}
          className="text-success text-decoration-none fs-3"
        >
          <i className="bi bi-chevron-left" /> Project
        </NavLink>
      </Row>
      <h1 className="text-light text-center mb-4">{step.title}</h1>
      <Card className="overflow-auto">
        <Card.Header as="h3">{step.description}</Card.Header>
        <ListGroup>
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
                <ListGroup.Item action key={task.id}>
                  <Container className="p-0 m-0">
                    <Row className="mb-4">
                      <Col md={12} lg={3}>
                        <Row md={2} lg={1}>
                          <Col className="fw-bold">
                            {task.title}
                            <div className={phaseColor}>{task.phase}</div>
                          </Col>
                          <Col>
                            <Button
                              variant="success"
                              className="text-light w-100"
                            >
                              Start Task
                            </Button>
                          </Col>
                        </Row>
                      </Col>
                      <Col md={12} lg={9} className="d-flex align-items-center">
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
      </Card>
    </Container>
  );
}
