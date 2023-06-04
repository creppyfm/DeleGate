import {
  Button,
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
      className={`text-center d-flex flex-column my-3 position-relative  ${styles["dynamic-height"]}`}
    >
      <NavLink
        to={`/dashboard/project/${step.projectId}`}
        className="position-absolute top-0 start-0 text-success text-decoration-none fs-3"
      >
        <i className="bi bi-chevron-left" /> Project
      </NavLink>
      <h1 className="text-light mb-4">{step.title}</h1>
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
                      <Col lg={12} xl={4}>
                        <div className="fw-bold">
                          {task.title}
                          <div className={phaseColor}>{task.phase}</div>
                        </div>
                        <Button variant="success" className="text-light w-100">
                          Start Task
                        </Button>
                      </Col>
                      <Col lg={12} xl={8}>
                        <div className="">{task.description}</div>
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
