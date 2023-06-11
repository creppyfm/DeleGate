import styles from "./Hero.module.css";

export function Hero() {
  return (
    <div className={styles.hero}>
      <h1 className="text-light my-3">The Future of Project Management</h1>
      <p className="lead text-light">
        DeleGate is an AI-powered platform designed for professional project
        managers. It offers contextual team management tools, revolutionizing
        the way projects are handled. By leveraging AI capabilities, DeleGate
        provides actionable insights, predictive analysis, and personalized
        recommendations. Its user-friendly interface streamlines project
        management workflows, allowing real-time collaboration and monitoring of
        progress. With comprehensive reporting and analytics, DeleGate empowers
        project managers to optimize team performance and drive success in
        today's dynamic business environment.
      </p>
      <p className="lead">
        <a
          href="#"
          className={`${styles["remove-text-shadow"]} btn btn-lg fw-bold bg-light border-light btn-light my-3`}
        >
          Get Started
        </a>
      </p>
    </div>
  );
}
