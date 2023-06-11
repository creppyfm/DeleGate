import { useParams } from "react-router-dom";
import { Task, useProjectContext } from "../../../../../utils/GetProjectData";
import { Button, Container, Form } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import styles from "./TaskPage.module.css";
import { useState } from "react";
import Message from "./Message";
import { v4 as uuidv4 } from "uuid";

export type ChatMessage = {
  role: "user" | "assistant";
  content: string;
};

export function TaskPage() {
  const { id } = useParams();
  const { project } = useProjectContext();
  const task = project?.taskList.find((taskListItem) => taskListItem.id === id);
  const [conversation, setConversation] = useState<ChatMessage[]>([
    {
      role: "assistant",
      content: `It looks like we need to get this done: 
    ${task?.description}`,
    },
    { role: "assistant", content: "How can I assist?" },
  ]);
  const [prompt, setPrompt] = useState("");
  const [streaming, setStreaming] = useState(false);
  const [streamBucket, setStreamBucket] = useState("");

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    setPrompt(e.target.value);
  }

  async function handleSubmit() {
    if (prompt.length === 0) return;
    const promptSubmission = {
      taskId: id,
      chatMessage: {
        role: "user",
        content: prompt,
      },
    };
    const body = JSON.stringify(promptSubmission);

    try {
      const response = await fetch(
        `${import.meta.env.VITE_BACKEND_SERVER_URI}/chat`,
        {
          method: "POST",
          body: body,
          mode: "cors",
          credentials: "include",
          headers: { "Content-Type": "application/json" },
          redirect: "manual",
        }
      );
      console.log("Response is: ", response);
      if (response.ok) {
        handleStream();
      }
    } catch (error) {
      console.log(error);
    }
  }

  async function handleStream() {
    setStreaming(true);
    const stream = new EventSource(
      `${import.meta.env.VITE_BACKEND_SERVER_URI}/stream`
    );
    stream.onmessage = async (event) => {
      const data = await JSON.parse(event.data);
      if (data.finishReason === "stop") {
        setStreaming(false);
        setConversation([
          ...conversation,
          { role: "assistant", content: streamBucket },
        ]);
        return stream.close();
      }
      setStreamBucket(streamBucket + data.content);
    };
  }

  // create function to scroll

  const fetchedTask = project?.taskList.find((task) => task.id === id) as Task;

  return (
    <Container className="my-4">
      <NavLink
        to={`${import.meta.env.BASE_URL}/dashboard/step/${fetchedTask.stepId}`}
        className="text-success text-decoration-none mt-4 fs-3"
      >
        <i className="bi bi-chevron-left" /> Step
      </NavLink>
      <h1 className="text-light text-center my-4">{fetchedTask.title}</h1>
      <div className={styles.box}>
        <h2 className="border-bottom">Task Assistant</h2>
        <div className={styles.chat}>
          {conversation.map((chat) => {
            return (
              <Message key={uuidv4()} role={chat.role} content={chat.content} />
            );
          })}
          {streaming && (
            <Message key={uuidv4()} role="assistant" content={streamBucket} />
          )}
        </div>
        <div className="mt-2 d-flex flex-column">
          <Form.Control
            className={styles.input}
            as={"textarea"}
            rows={5}
            onChange={handleChange}
          />
          <Button
            onClick={handleSubmit}
            variant="primary"
            className={`align-self-end mt-2 ${styles.send}`}
          >
            <span className="fs-3 text-light">Send</span>{" "}
            <i className="bi bi-send-fill fs-3 text-light" />
          </Button>
        </div>
      </div>
    </Container>
  );
}
