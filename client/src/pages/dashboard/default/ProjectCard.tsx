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
      {/* <ListGroupItem as="li" className="bg-light p-3 mb-3 rounded">
        <LinkContainer to="/dashboard/project">
          <NavLink className="d-flex justify-content-between align-items-center">
            <ListGroup variant="flush" className="w-100">
              <ListGroupItem>
                <div className="fw-bold fs-4">{title}</div>
              </ListGroupItem>
              <ListGroupItem>
                <div className={`${phaseColor}`}>{phase}</div>
              </ListGroupItem>
              <ListGroupItem>
                <div>
                  Updated <Badge bg="primary">{updated}</Badge>
                </div>
              </ListGroupItem>
            </ListGroup>
          </NavLink>
        </LinkContainer>
      </ListGroupItem> */}
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
