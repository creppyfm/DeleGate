import styles from "./ProjectCard.module.css";
import { useEffect, useState } from "react";
import {
  NavLink,
  ListGroupItem,
  Badge,
  Fade,
  ListGroup,
  Card,
} from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";

// export type Project = {
//   projectId: string;
//   title: string;
//   subtitle: string | null | undefined;
//   description: string;
//   phase: string;
//   projectMembers: string[];
//   updated: string;
// };

export type Project = {
  projectId: string;
  title: String;
  phase: string;
  updated: string;
};

export function ProjectCard({
  project,
  timeout,
}: {
  project: Project;
  timeout: number;
}) {
  const { title, phase, updated } = project;
  const [open, setOpen] = useState(false);
  let phaseColor = "";
  if (phase === "In Review") {
    phaseColor = "text-info";
  }
  useEffect(() => {
    setTimeout(() => {
      setOpen(true);
    }, timeout);
  }, []);
  return (
    <Fade in={open} className={styles.fade}>
      <LinkContainer className="my-2" to="dashboard/project">
        <Card className="pt-3">
          <Card.Title className="ps-3">{title}</Card.Title>
          <Card.Footer className="d-flex justify-content-around p-0">
            <Card.Text>
              <span className={phaseColor}>{phase}</span>
            </Card.Text>
            <Card.Text>
              {" "}
              Updated <Badge bg="primary">{updated}</Badge>
            </Card.Text>
          </Card.Footer>
        </Card>
      </LinkContainer>
    </Fade>
  );
}
