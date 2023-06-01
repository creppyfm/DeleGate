import { Modal } from "react-bootstrap";
import { NewProjectForm } from "./NewProjectForm";
import { useState } from "react";

type NewProjectModalProps = {
  show: boolean;
  onHide: () => void;
};

export function NewProjectModal(props: NewProjectModalProps) {
  const [loading, setLoading] = useState(false);
  return (
    <Modal
      {...props}
      size="lg"
      aria-labelledby="new-project-prompt-modal"
      backdrop={loading ? "static" : true}
      centered
    >
      <Modal.Header closeButton={!loading}>
        <h3 className="m-0">New Project</h3>
      </Modal.Header>
      <Modal.Body>
        <NewProjectForm loading={loading} setLoading={setLoading} />
      </Modal.Body>
    </Modal>
  );
}
