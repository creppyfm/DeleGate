import { useState, useEffect } from "react";
import { Card, Col, Container, ListGroup, Row, Spinner } from "react-bootstrap";
import { NavLink, useParams } from "react-router-dom";

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
  userID: string;
  role: string;
  currentTasks: string[];
  firstName: string;
  lastName: string;
};

type ProjectMemberList = ProjectMember[];

export type Task = {
  taskId: string;
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

type AssignedUserList = string[];

export type Step = {
  id: string;
  projectId: string;
  title: string;
  description: string;
  taskList: string[];
};

type StepList = Step[];

export function ProjectPage() {
  const [loading, setLoading] = useState(true);
  const [steps, setSteps] = useState<StepList>([]);
  const [projectName, setProjectName] = useState("Loading Project...");
  const [description, setDescription] = useState("Loading Project...");
  const [phase, setPhase] = useState("Not Started");
  const [updated, setUpdated] = useState("Unknown");
  const { id } = useParams();

  async function fetchProject() {
    try {
      const response = await fetch(`/projects/${id}`);
      if (response.ok) {
        const data: ProjectData = await response.json();
        const { title, description, phase, updated, stepList } = data;
        setProjectName(title);
        setSteps(stepList);
        setDescription(description);
        setPhase(phase);
        setUpdated(updated);

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
    <Container className="pt-5">
      <h1 className="text-center text-light mb-5">{projectName}</h1>
      <Row style={{ height: "60%" }}>
        <Col lg={12} xl={6}>
          <Card bg="light" className="rounded-4 mb-3">
            <Card.Header as="h3">Summary</Card.Header>
            <Card.Body>
              <Card.Title></Card.Title>
              <Card.Text>{description}</Card.Text>
            </Card.Body>
            <Card.Footer>
              <div className="d-flex justify-content-between">
                <span>Phase: {phase}</span>
                <span>Updated: {updated}</span>
              </div>
            </Card.Footer>
          </Card>
          <ListGroup className="rounded-4">
            {loading ? (
              <ListGroup.Item>
                <div className="d-flex text-warning">
                  <Spinner animation="border" role="status" className="me-3" />
                  <h3>Loading... </h3>
                </div>
              </ListGroup.Item>
            ) : (
              steps.map((step) => {
                const { id, title }: Step = step;
                return (
                  <ListGroup.Item key={id}>
                    <NavLink to={`/dashboard/step/${id}`}>{title}</NavLink>
                  </ListGroup.Item>
                );
              })
            )}
          </ListGroup>
        </Col>
        <Col lg={12} xl={6}></Col>
      </Row>
    </Container>
  );
}
