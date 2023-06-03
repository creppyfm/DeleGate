import {
  Button,
  Card,
  Col,
  Container,
  ListGroup,
  Row,
  Spinner,
} from "react-bootstrap";
import { useParams } from "react-router-dom";
import {
  Project,
  Step,
  Task,
  useProjectContext,
} from "../../utils/GetProjectData";
import { useState } from "react";

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
    <Container className="text-center">
      <h1 className="text-light my-4">{step.title}</h1>

      <ListGroup>
        <ListGroup.Item variant="dark">{step.description}</ListGroup.Item>
        {step.taskList.length > 0 ? (
          taskListWithData.map((task: Task) => {
            return <ListGroup.Item action>{task.title}</ListGroup.Item>;
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
    </Container>
  );
}
