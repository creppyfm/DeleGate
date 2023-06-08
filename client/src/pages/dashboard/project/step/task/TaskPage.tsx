import { useParams } from "react-router-dom";
import { useProjectContext } from "../../../../../utils/GetProjectData";
import { Card, Container } from "react-bootstrap";

export function TaskPage() {
  const { id } = useParams();
  const { project } = useProjectContext();

  const fetchedTask = project?.taskList.find((task) => task.id === id);

  return (
    <Card>
      <Card.Header>Task Assistant</Card.Header>
      <Card.Body>
        <Card.Title>Card Title</Card.Title>
        <Card.Text>Card Text</Card.Text>
      </Card.Body>
    </Card>
  );
}
