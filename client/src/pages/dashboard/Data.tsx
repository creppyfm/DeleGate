import { LinkContainer } from "react-router-bootstrap";
import { Button } from "react-bootstrap";

export function Data() {
  return (
    <div>
      <h1 className="text-light">Dashboard Home</h1>
      <LinkContainer to="/dashboard/">
        <Button>Home Page</Button>
      </LinkContainer>
    </div>
  );
}
