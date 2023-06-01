import { FormEvent, useState } from "react";
import { Button, Form, Spinner } from "react-bootstrap";

import { ProjectList } from "./DashboardPage";

type NewProjectFormProps = {
  loading: boolean;
  setLoading: React.Dispatch<React.SetStateAction<boolean>>;
  setShowPrompt: React.Dispatch<React.SetStateAction<boolean>>;
  setList: React.Dispatch<React.SetStateAction<ProjectList>>;
};

export function NewProjectForm({
  loading,
  setLoading,
  setShowPrompt,
  setList,
}: NewProjectFormProps) {
  const [prompt, setPrompt] = useState("");
  const [errorOnPost, setErrorOnPost] = useState(false);
  const examplesGiven = [
    "Build an operating system in Rust",
    "Organize a family reunion",
    "Start a food truck business",
  ];

  async function createNewProject() {
    try {
      const postBody = JSON.stringify({ prompt });
      console.log(postBody);
      const response = await fetch("http://localhost:8080/projects/new", {
        method: "POST",
        credentials: "include",
        mode: "cors",
        body: JSON.stringify({ prompt }),
      });
      if (response.ok) {
        setList([]);
        setLoading(false);
        setShowPrompt(false);
      } else {
        setLoading(false);
        setErrorOnPost(true);
      }
    } catch (error) {
      console.log(error);
      setLoading(false);
      setErrorOnPost(true);
    }
  }

  function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setLoading(true);
    createNewProject();
  }
  return (
    <Form onSubmit={handleSubmit}>
      <Form.Group>
        <Form.Label as="h3">What would you like to get done?</Form.Label>
        <Form.Control
          as="textarea"
          size="lg"
          value={prompt}
          onChange={(e) => {
            setPrompt(e.target.value);
          }}
          placeholder={`e.g. ${
            examplesGiven[
              Math.floor(Math.random() * (examplesGiven.length - 1))
            ]
          }.`}
          rows={3}
        />
      </Form.Group>
      <div className="d-flex justify-content-end mt-3">
        {errorOnPost && (
          <span className="text-danger me-5">Failed to create new project</span>
        )}
        <Button type="submit" size="lg" variant="primary" disabled={loading}>
          {loading && (
            <Spinner
              as="span"
              size="sm"
              role="status"
              aria-hidden="true"
              className="me-3"
            />
          )}
          {loading ? "Loading..." : "Submit"}
        </Button>
      </div>
    </Form>
  );
}
