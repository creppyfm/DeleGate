import { Button } from "react-bootstrap";
import styles from "./LoginPage.module.css";

export function LoginPage() {
  return (
    <main className="d-flex flex-column align-items-center gap-3">
      <h2 className="mb-3 fw-normal text-light">Please Sign In</h2>
      <Button
        className={styles["sso-button"]}
        as="a"
        href="/oauth2/authorization/github"
      >
        Github
      </Button>
      <Button
        className={styles["sso-button"]}
        as="a"
        href="/oauth2/authorization/google"
      >
        Google
      </Button>
    </main>
  );
}
