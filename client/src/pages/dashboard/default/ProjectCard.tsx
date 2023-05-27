import { useEffect, useState } from "react";
import { NavLink, ListGroupItem, Badge, Fade } from "react-bootstrap";
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
    <Fade in={open} timeout={500}>
      <ListGroupItem as="li" className="bg-light rounded mb-3 p-3">
        <LinkContainer to="/dashboard/project">
          <NavLink className="d-flex justify-content-between align-items-center">
            <div className="fw-bold fs-4">{title}</div>
            <div className="d-flex align-items-center gap-2">
              <div className={`${phaseColor}`}>{phase}</div>
              <div>
                Updated <Badge bg="primary">{updated}</Badge>
              </div>
            </div>
          </NavLink>
        </LinkContainer>
      </ListGroupItem>
    </Fade>
  );
}
