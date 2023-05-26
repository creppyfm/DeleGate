// import styles from "./Home.module.css";
import { Container, Row } from "react-bootstrap";
import { ProjectCard } from "./ProjectCard";
import { v4 as uuidv4 } from "uuid";
import { User } from "../../../App";

type DashboardPageProps = {
  user: User;
};

export function DashboardPage({ user }: DashboardPageProps) {
  console.log("Dashboard Page user present: ", user.loggedIn);

  return (
    <Container className="mt-3">
      <Row xs={1} md={2} xl={3} className="g-4">
        {Array.from({ length: 4 }).map((_) => {
          const randomKey = uuidv4();
          const project = {
            projectId: randomKey.slice(randomKey.length / 1.5),
            title: "Project: " + randomKey.slice(randomKey.length / 1.5),
            subtitle: null,
            description:
              "This is a longer card with supporting text below as a natural lead-in to additional content. This content is a little bit longer. This is a longer card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.",
            phase: "Not Started",
            projectMembers: ["Bill", "Joanne"],
            updated: "November 5, 1605",
          };
          return <ProjectCard key={randomKey} project={project} />;
        })}
      </Row>
    </Container>
  );
}
