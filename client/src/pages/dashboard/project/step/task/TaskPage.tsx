import { useParams } from "react-router-dom";
import { Task, useProjectContext } from "../../../../../utils/GetProjectData";
import { Button, Container, Form } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import styles from "./TaskPage.module.css";
import { useRef, useState } from "react";
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
    { role: "assistant", content: "Ready to get stuff done? I can help!" },
    {
      role: "assistant",
      content: `I grabbed this from the task description: 
    ${task?.description}`,
    },
    { role: "assistant", content: "How can I assist?" },
  ]);
  const [prompt, setPrompt] = useState("");
  const [streaming, setStreaming] = useState(false);
  const [streamBucket, setStreamBucket] = useState("");
  const tempMessage = useRef("");

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    setPrompt(e.target.value);
  }

  async function handleSubmit() {
    if (prompt.length === 0) return;
    try {
      const response = await fetch(
        `${import.meta.env.VITE_BACKEND_SERVER_URI}/chat/${id}`,
        {
          method: "POST",
          credentials: "include",
          mode: "cors",
          body: JSON.stringify({ prompt }),
        }
      );
      if (response.ok) {
        const newConversation: ChatMessage[] = [
          ...conversation,
          { role: "user", content: prompt },
        ];
        setConversation(newConversation);
        handleStream(newConversation);
      }
    } catch (error) {
      if (import.meta.env.DEV) {
        console.log("\x1b[93mDev console: \x1b[0m", error);
      }
    }
  }

  function handleStream(currentRenderedConversation: ChatMessage[]) {
    setStreaming(true);
    setPrompt("");
    const chatBox = document.querySelector("[data-chat-box]") as HTMLDivElement;
    const stream = new EventSource(
      `${import.meta.env.VITE_BACKEND_SERVER_URI}/stream`,
      { withCredentials: true }
    );
    stream.onmessage = async (event) => {
      const data = await JSON.parse(event.data);
      if (data.finishReason === "stop") {
        setStreaming(false);
        setConversation([
          ...currentRenderedConversation,
          { role: "assistant", content: tempMessage.current },
        ]);
        tempMessage.current = "";
        return stream.close();
      }
      tempMessage.current = tempMessage.current + data.content;
      console.log(tempMessage.current);
      setStreamBucket(tempMessage.current);
      chatBox.scrollTop = chatBox.scrollHeight;
    };

    stream.onerror = (event) => {
      if (import.meta.env.DEV) {
        console.log("\x1b[93mDev console: \x1b[0m", event);
        console.log("\x1b[93mDev console: \x1b[0m", "Closing stream...");
      }
      stream.close();
    };
  }

  const fetchedTask = project?.taskList.find((task) => task.id === id) as Task;

  return (
    <Container className="my-4">
      <NavLink
        to={`${import.meta.env.BASE_URL}dashboard/step/${fetchedTask.stepId}`}
        className="text-success text-decoration-none mt-4 fs-3"
      >
        <i className="bi bi-chevron-left" /> Step
      </NavLink>
      <h1 className="text-light text-center my-4">{fetchedTask.title}</h1>
      <div className={styles.box}>
        <h2 className="border-bottom">Task Assistant</h2>
        <div className={styles.chat} data-chat-box>
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
            value={prompt}
          />
          <Button
            onClick={handleSubmit}
            variant="primary"
            disabled={streaming}
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
