import { Button } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";

export function Home() {
  return (
    <div>
      <h1 className="text-light">Dashboard Home</h1>
      <LinkContainer to="/dashboard/data">
        <Button>Data Page</Button>
      </LinkContainer>
    </div>
  );
}
