import { Card, Col, Container, ListGroup } from "react-bootstrap";
import { useParams } from "react-router-dom";
import { Step, Task, useProjectContext } from "../../utils/GetProjectData";

// type Step = {
//   id: string;
//   projectId: string;
//   title: string;
//   description: string;
//   taskList: string[];
// };

export function StepPage() {
  const { id } = useParams();
  const { project } = useProjectContext();
  const { projectId, title, description, taskList } = project?.stepList.find(
    (step) => {
      return step.id === id;
    }
  ) as Step;

  const taskListWithData = taskList.map((taskId) => {
    return project?.taskList.find((task) => task.id === taskId) as Task;
  });

  return (
    <Container>
      <Col lg={12} xl={6}>
        <Card>
          <Card.Title>{title}</Card.Title>
          <Card.Body>
            <Card.Text>{description}</Card.Text>
          </Card.Body>
        </Card>
      </Col>
      <Col lg={12} xl={6}>
        <ListGroup>
          {taskList.length > 0 ? (
            taskListWithData.map((task: Task) => {
              return <ListGroup.Item>{task.title}</ListGroup.Item>;
            })
          ) : (
            <ListGroup.Item>No Tasks Generated</ListGroup.Item>
          )}
        </ListGroup>
      </Col>
    </Container>
  );
}
