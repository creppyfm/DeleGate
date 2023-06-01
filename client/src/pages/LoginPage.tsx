import { Button, Card, Image } from "react-bootstrap";
import styles from "./LoginPage.module.css";
import { useGetUserDataIfExists } from "../utils/GetUserData";

export function LoginPage() {
  useGetUserDataIfExists();
  return (
    <Card as="main" bg="dark" className="m-auto">
      <Card.Img variant="top" src="logo_with_brand_dark_trimmed.png" />
      <Card.Body>
        <Card.Title className="text-light text-center">
          Please Sign In
        </Card.Title>
        <div className="d-flex justify-content-around mt-4">
          <Button
            className={`fs-4 ${styles["sso-button"]}`}
            as="a"
            href="/oauth2/authorization/github"
          >
            <i className="bi bi-github" style={{ color: "#181717" }}></i> Github
          </Button>
          <Button
            className={`fs-4 ${styles["sso-button"]}`}
            as="a"
            href="/oauth2/authorization/google"
          >
            <i className="bi bi-google" style={{ color: "#4285F4" }}></i> Google
          </Button>
        </div>
        <div className="d-flex justify-content-around">
          <Button className="fs-4 my-4" style={{ width: "20rem" }} disabled>
            <i className="bi bi-envelope"></i> Log In With Email
          </Button>
        </div>
      </Card.Body>
    </Card>
  );
}
