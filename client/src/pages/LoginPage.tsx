import { useEffect } from "react";
import { Button } from "react-bootstrap";
import { redirect } from "react-router-dom";
import styles from "./LoginPage.module.css";
import { useParams } from "react-router-dom";
import { SetUser } from "../App";

type LoginPageProps = {
  setUser: SetUser;
  provider?: string;
};

export function LoginPage({ setUser, provider }: LoginPageProps) {
  const { code } = useParams();

  async function getUser() {
    try {
      const body = new URLSearchParams({ code: code as string });

      const response = await fetch(`/auth/${provider}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: body,
      });

      // if (!response.ok) {
      //   throw new Error("HTTP status " + response.status);
      // }

      if (response.ok) {
        setUser({
          firstName: "John",
          lastName: "Smith",
          email: "test@test.com",
          sessionId: "21",
          loggedIn: true,
        });
        return true;
      }

      // const data = await response.json();

      // return data.user;
      return false;
    } catch (error) {
      console.log(error);
      return false;
    }
  }

  useEffect(() => {
    if (provider) {
      getUser().then((user) => {
        redirect("/");
      });
    }
  }, []);

  if (provider) {
    return (
      <main className="d-flex flex-column align-items-center gap-3">
        <h2 className="mb-3 fw-normal text-light">Loading Profile...</h2>
      </main>
    );
  }

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
