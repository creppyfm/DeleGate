import styles from "./ProjectCard.module.css";
import { useEffect, useState } from "react";
import { Badge, Fade, Card } from "react-bootstrap";
import { ProjectPreview } from "./DashboardPage";
import { NavLink } from "react-router-dom";

type ProjectCardProps = {
  project: ProjectPreview;
  timeout: number;
};

export function ProjectCard({ project, timeout }: ProjectCardProps) {
  const { title, phase, updated, projectId } = project;
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
      <NavLink
        to={`/dashboard/project/${projectId}`}
        style={{ textDecoration: "none" }}
      >
        <Card className="pt-3 my-2 pe-0 text-decoration-none">
          <Card.Title className="ps-3">{title}</Card.Title>
          <Card.Footer className="d-flex justify-content-around p-0 pt-2">
            <Card.Text>
              <span className={phaseColor}>{phase}</span>
            </Card.Text>
            <Card.Text>
              {" "}
              Updated <Badge bg="primary">{updated}</Badge>
            </Card.Text>
          </Card.Footer>
        </Card>
      </NavLink>
    </Fade>
  );
}
