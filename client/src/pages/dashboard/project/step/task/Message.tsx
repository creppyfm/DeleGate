import { Card } from "react-bootstrap";
import { ChatMessage } from "./TaskPage";

type MessageProps = {
  role: string;
  setConversation: React.Dispatch<React.SetStateAction<ChatMessage[]>>;
};

export default function Message({ role, setConversation }: MessageProps) {
  return (
    <Card
      border={role === "user" ? "success" : "primary"}
      className={`w-75 ${role === "user" ? "align-self-end me-3" : ""} `}
    >
      <Card.Body>Message</Card.Body>
    </Card>
  );
}
