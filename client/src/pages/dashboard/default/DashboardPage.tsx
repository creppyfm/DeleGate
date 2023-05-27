import { Button, Card, Col, Container, Row } from "react-bootstrap";
import { Project, ProjectCard } from "./ProjectCard";
import { v4 as uuidv4 } from "uuid";
import { projectList } from "../../../utils/mockProjectList";
import { useEffect, useState } from "react";

type ProjectList = Project[];

export function DashboardPage() {
  const [list, setList] = useState<ProjectList>([]);

  async function getProjectList() {
    if (list.length === 0) {
      try {
        const response = await fetch("/projects");
        if (response.ok) {
          const data: ProjectList = await response.json();
          const fetchedList: ProjectList = [];
          data.forEach((project) => {
            if (
              project &&
              project.projectId &&
              project.title &&
              project.phase &&
              project.updated
            ) {
              fetchedList.push(project);
            }
          });
          console.log(projectList);
          // setList(projectList);
        }
      } catch (error) {
        console.log(error);
      }
    }
  }

  useEffect(() => {
    setList(projectList);
    getProjectList();
  }, []);

  return (
    <Container className="mt-3 rounded position-relative">
      <Row className="g-4">
        <Col lg={12} xl={8}>
          <h2 className="text-light text-center mt-3 mb-4">Quick Look</h2>
          <Card>
            <Card.Body>
              <Card.Title as="h3" className="text-center">
                Heads Up
              </Card.Title>
              <Card.Subtitle>Aggregate Data</Card.Subtitle>
              <Card.Text className="mt-3">
                Over time this section will expand.
              </Card.Text>
              <Card.Text>Charts, shorcuts, you name it.</Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col lg={12} xl={4}>
          <h2 className="text-light text-center mt-3 mb-4">Open Projects</h2>
          <ul className="w-100 ps-0">
            {list.map((project, index) => {
              return (
                <ProjectCard
                  key={uuidv4()}
                  project={project}
                  timeout={50 * index}
                />
              );
            })}
          </ul>
        </Col>
      </Row>
      <Button
        size="lg"
        variant="outline-success"
        className="position-absolute top-0"
      >
        <span>Start New Project</span>
      </Button>
    </Container>
  );
}
