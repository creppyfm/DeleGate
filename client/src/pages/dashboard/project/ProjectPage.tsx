import { useRef, useState, useEffect } from "react";
import { Card, Col, Container, ListGroup, Row, Spinner } from "react-bootstrap";
import { useParams } from "react-router-dom";

type ProjectData = {
  id: string;
  userId: string;
  title: string;
  description: string;
  phase: string;
  created: string;
  updated: string;
  projectMembers: ProjectMemberList;
  taskList: TaskList;
  stepList: StepList;
};

type ProjectMember = {
  userId: string;
  role: string;
};

type ProjectMemberList = ProjectMember[];

type Task = {
  id: string;
  stepId: string;
  title: string;
  description: string;
  weight: number;
  status: string;
  created: string;
  updated: string;
  assignedUsers: AssignedUserList;
};

type TaskList = Task[];

type AssignedUser = {
  id: string;
  token: string;
  openAIKey: string;
  firstName: string;
  lastName: string;
  email: string;
  provider: string;
  projectIds: string[];
  strengths: string[];
  currentTasks: string[];
};

type AssignedUserList = AssignedUser[];

type Step = {
  id: string;
  projectId: string;
  title: string;
  description: string;
  taskList: TaskList;
};

type StepList = Step[];

export function ProjectPage() {
  const [loading, setLoading] = useState(true);
  const [steps, setSteps] = useState<StepList>([]);
  const [projectName, setProjectName] = useState("Loading Project...");
  const [projectDescription, setProjectDescription] =
    useState("Loading Project...");
  // const isFetching = useRef(false)
  const { id } = useParams();

  async function fetchProject() {
    console.log("Inside fetchProject");
    try {
      const response = await fetch(`/projects/${id}`);
      if (response.ok) {
        const data: ProjectData = await response.json();
        const { title, description, phase, created, updated, stepList } = data;
        setProjectName(title);
        setSteps(stepList);

        setLoading(false);
      }
    } catch (error) {
      console.log(error);
    }
  }

  useEffect(() => {
    fetchProject();
  }, []);

  return (
    <Container className="h-100 pt-5">
      <h1 className="text-center text-light mb-5">{projectName}</h1>
      <Row className="">
        <Col lg={12} xl={6}>
          <ListGroup className="rounded-4">
            {loading && (
              <ListGroup.Item>
                <div className="d-flex text-warning">
                  <Spinner animation="border" role="status" className="me-3" />
                  <h3>Loading... </h3>
                </div>
              </ListGroup.Item>
            )}
            {!loading &&
              steps.map((step) => {
                return <ListGroup.Item>{step.title}</ListGroup.Item>;
              })}
          </ListGroup>
        </Col>
        <Col lg={12} xl={6}>
          <Card bg="light" className="rounded-4">
            <Card.Header as="h3">This is the card header!</Card.Header>
            <Card.Body>
              <Card.Title>Special title treatment</Card.Title>
              <Card.Text>
                With supporting text below as a natural lead-in to additional
                content.
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}
