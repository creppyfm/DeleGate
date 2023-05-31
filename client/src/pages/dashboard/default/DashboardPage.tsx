import {
  Button,
  ButtonGroup,
  Card,
  Col,
  Container,
  Row,
  ToggleButton,
} from "react-bootstrap";
import { Project, ProjectCard } from "./ProjectCard";
import { v4 as uuidv4 } from "uuid";
import { projectList } from "../../../utils/mockProjectList";
import { NewProjectModal } from "./NewProjectModal";
import { useEffect, useState, useMemo } from "react";
import { OffCanvasPrompt } from "./OffCanvasPrompt";

type ProjectList = Project[];

export function DashboardPage() {
  const [list, setList] = useState<ProjectList>([]);
  const [showPrompt, setShowPropmt] = useState(false);
  const [radioValue, setRadioVaule] = useState("1");

  const processedList = useMemo(
    () =>
      list.map((project, index) => {
        return (
          <ProjectCard key={uuidv4()} project={project} timeout={50 * index} />
        );
      }),
    [list]
  );

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
    if (list.length === 0) {
      setList(projectList);
      getProjectList();
    }
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
          <ul className="w-100 ps-0">{processedList}</ul>
        </Col>
      </Row>
      <Button
        size="lg"
        variant="outline-success"
        onClick={() => setShowPropmt(true)}
        className="position-absolute top-0"
      >
        <span>Start New Project</span>
      </Button>
      {/* <NewProjectModal show={showPrompt} onHide={() => setShowPropmt(false)} /> */}
      <OffCanvasPrompt
        show={showPrompt}
        handleClose={() => setShowPropmt(false)}
      />
      <ButtonGroup className="position-absolute top-0 end-0">
        <ToggleButton
          id="radio-1"
          type="radio"
          variant="success"
          name="radio"
          value={1}
          checked={"1" === radioValue}
          onChange={() => {
            setRadioVaule("1");
          }}
        >
          Modal
        </ToggleButton>
        <ToggleButton
          id="radio-2"
          type="radio"
          variant="info"
          name="radio"
          value={2}
          checked={"2" === radioValue}
          onChange={() => {
            setRadioVaule("2");
          }}
        >
          Popover
        </ToggleButton>
      </ButtonGroup>
    </Container>
  );
}
