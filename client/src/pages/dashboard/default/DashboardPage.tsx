import { Container, Row } from "react-bootstrap";
import { Project, ProjectCard } from "./ProjectCard";
import { v4 as uuidv4 } from "uuid";
import { projectList } from "../../../utils/mockProjectList";
import { useEffect, useState } from "react";

type ProjectList = Project[];

export function DashboardPage() {
  const [list, setList] = useState<ProjectList>([]);

  // async function getProjectList() {
  //   const response = await fetch("/projects");
  //   if (response.ok) {
  //     const data: ProjectList = await response.json();
  //     const fetchedList: ProjectList = [];
  //     data.forEach((project) => {
  //       if (
  //         project &&
  //         project.projectId &&
  //         project.title &&
  //         project.phase &&
  //         project.updated
  //       ) {
  //         fetchedList.push(project);
  //       }
  //     });
  //     setList(projectList);
  //   }
  // }

  useEffect(() => {
    setList(projectList);
  }, []);

  return (
    <Container className="mt-3">
      <Row xs={1} md={2} xl={3} className="g-4">
        {list.map((project) => {
          return <ProjectCard key={uuidv4()} project={project} />;
        })}
      </Row>
    </Container>
  );
}
