import { useParams } from "react-router-dom";
import { Task, useProjectContext } from "../../../../../utils/GetProjectData";
import { Container } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import styles from "./TaskPage.module.css";
import { useState } from "react";
import Message from "./Message";

export type ChatMessage = {
  role: "user" | "assistant";
  message: string;
};

export function TaskPage() {
  const { id } = useParams();
  const { project } = useProjectContext();
  const [conversation, setConversation] = useState<ChatMessage[]>([
    { role: "assistant", message: "How can I assist?" },
  ]);

  const fetchedTask = project?.taskList.find((task) => task.id === id) as Task;

  const testMessages = [{}];
  return (
    <Container className="my-4">
      <NavLink
        to={`/dashboard/step/${fetchedTask.stepId}`}
        className="text-success text-decoration-none mt-4 fs-3"
      >
        <i className="bi bi-chevron-left" /> Step
      </NavLink>
      <h1 className="text-light text-center my-4">{fetchedTask.title}</h1>
      <div className={styles.box}>
        <h2 className="border-bottom">Task Assistant</h2>
        <div className={styles.chat}>
          <Message role="assistant" setConversation={setConversation} />
          <Message role="user" setConversation={setConversation} />
        </div>
        <div className="border-top">Input</div>
      </div>
    </Container>
  );
}
