import { Button } from "react-bootstrap";
import styles from "./LoginPage.module.css";
export function LoginPage() {
  return (
    <main className="d-flex flex-column align-items-center gap-3">
      <h2 className="mb-3 fw-normal text-light">Please Sign In</h2>
      <Button className={styles["sso-button"]}>Github</Button>
      <Button className={styles["sso-button"]}>Google</Button>
    </main>
  );
}
{
  /* <form action="/users/login" method="POST">
        <img
          class="mb-4 rounded-3"
          src="/CarLogoDark.png"
          alt="Carpooly Logo"
          width="150"
          height="auto"
        />
        <h1 class="h3 mb-3 fw-normal">Please sign in</h1>
        <div class="form-floating input-top">
          <input
            type="email"
            class="form-control"
            id="email"
            name="email"
            placeholder="name@example.com"
          />
          <label for="email">Email address</label>
        </div>
        <div class="form-floating input-bottom mb-4">
          <input
            type="password"
            class="form-control"
            id="password"
            name="password"
            placeholder="Password"
          />
          <label for="password">Password</label>
        </div>

        <button class="w-100 btn btn-lg btn-primary" type="submit">
          Sign in
        </button>
      </form> */
}
