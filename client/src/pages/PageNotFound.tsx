import { Button } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";

export function PageNotFound() {
  return (
    <div className="text-light m-auto">
      <h1>Page Not Found</h1>
      <p>Well, this is awkward. The page you're looking for isn't here.</p>
      <LinkContainer to="/">
        <Button>Go Home</Button>
      </LinkContainer>
    </div>
  );
}
