import { Button, Card, Col, Container, Row } from "react-bootstrap";
import { Project, ProjectCard } from "./ProjectCard";
import { v4 as uuidv4 } from "uuid";
import { NewProjectModal } from "./NewProjectModal";
import { useEffect, useState, useMemo, useRef } from "react";
import styles from "./Dashboard.module.css";

export type ProjectList = Project[];

export function DashboardPage() {
  const [list, setList] = useState<ProjectList>([]);
  const [showPrompt, setShowPrompt] = useState(false);
  const fetchingStatus = useRef(false);

  const processedList = useMemo(
    () => (
      <div className={`pe-3 overflow-auto ${styles.h80}`}>
        {list.map((project, index) => {
          return (
            <ProjectCard
              key={uuidv4()}
              project={project}
              timeout={50 * index}
            />
          );
        })}
      </div>
    ),
    [list]
  );

  async function getProjectList() {
    try {
      const response = await fetch("/projects");
      if (response.ok) {
        const data: ProjectList = await response.json();
        if (data.length > 0) {
          const fetchedList: ProjectList = [];
          data.forEach((project) => {
            fetchedList.push(project);
          });
          setList(fetchedList);
        } else {
          setList([
            {
              projectId: "None",
              title: "No Projects Found",
              phase: "Not Started",
              updated: "Not Started",
              description: "Not Started",
            },
          ]);
        }
      }
      fetchingStatus.current = false;
    } catch (error) {
      fetchingStatus.current = false;
      console.log(error);
    }
  }

  useEffect(() => {
    if (list.length === 0 && !fetchingStatus.current) {
      fetchingStatus.current = true;
      getProjectList();
    }
  }, [list]);

  return (
    <Container fluid="lg" className="mt-3 rounded position-relative h-100">
      <Row className="g-4 h-100">
        <Col lg={12} xl={7}>
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
        <Col lg={12} xl={5} className="h-100">
          <h2 className="text-light text-center my-3">Open Projects</h2>
          {processedList}
        </Col>
      </Row>
      <Button
        size="lg"
        variant="outline-success"
        onClick={() => setShowPrompt(true)}
        className="position-absolute top-0"
      >
        <span>Start New Project</span>
      </Button>
      <NewProjectModal
        showPrompt={showPrompt}
        setShowPrompt={setShowPrompt}
        setList={setList}
      />
    </Container>
  );
}
