import styles from "./Hero.module.css";

export function Hero() {
  return (
    <div className={styles.hero}>
      <h1 className="text-light">Some Catchy Headline</h1>
      <p className="lead text-light">
        DeleGate is the future of project management. Powered by AI and
        delivering contextual team management tools for simplifying your work
        environment.
      </p>
      <p className="lead">
        <a
          href="#"
          className={`${styles["remove-text-shadow"]} btn btn-lg fw-bold bg-light border-light btn-light`}
        >
          Get Started
        </a>
      </p>
    </div>
  );
}
