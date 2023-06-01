import { Modal } from "react-bootstrap";
import { NewProjectForm } from "./NewProjectForm";
import { useState } from "react";

type NewProjectModalProps = {
  showPrompt: boolean;
  setShowPrompt: React.Dispatch<React.SetStateAction<boolean>>;
};

export function NewProjectModal({
  showPrompt,
  setShowPrompt,
}: NewProjectModalProps) {
  const [loading, setLoading] = useState(false);
  return (
    <Modal
      onHide={() => setShowPrompt(false)}
      show={showPrompt}
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
