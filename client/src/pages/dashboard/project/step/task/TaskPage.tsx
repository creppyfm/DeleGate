import { useParams } from "react-router-dom";
import { Task, useProjectContext } from "../../../../../utils/GetProjectData";
import { Card, Container, Row } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import styles from "../../../default/Dashboard.module.css";

export function TaskPage() {
  const { id } = useParams();
  const { project } = useProjectContext();

  const fetchedTask = project?.taskList.find((task) => task.id === id) as Task;

  return (
    <>
      <Container
        className={`d-flex flex-column my-3 ${styles["dynamic-height"]}`}
      >
        <Row className="text-start">
          <NavLink
            to={`/dashboard/step/${fetchedTask.stepId}`}
            className="text-success text-decoration-none fs-3"
          >
            <i className="bi bi-chevron-left" /> Step
          </NavLink>
        </Row>
        <h1 className="text-light text-center my-4">{fetchedTask.title}</h1>
        <Row>
          <Card>
            <Card.Header>Task Assistant</Card.Header>
            <Card.Body>
              <Card.Title>Card Title</Card.Title>
              <Card.Text>Card Text</Card.Text>
            </Card.Body>
          </Card>
        </Row>
      </Container>
    </>
  );
}
