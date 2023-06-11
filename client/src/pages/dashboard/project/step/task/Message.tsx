import { Card } from "react-bootstrap";
import { ChatMessage } from "./TaskPage";

export default function Message({ role, content }: ChatMessage) {
  return (
    <Card
      bg={role === "user" ? "success" : "light"}
      className={`w-50 shadow bg- bg-opacity-10 ${
        role === "user" ? "align-self-end me-3" : ""
      } `}
    >
      <Card.Body>{content}</Card.Body>
    </Card>
  );
}
