import { Modal } from "react-bootstrap";
import { NewProjectForm } from "./NewProjectForm";
import { useState } from "react";

import { ProjectPreview } from "./DashboardPage";

type NewProjectModalProps = {
  showPrompt: boolean;
  setShowPrompt: React.Dispatch<React.SetStateAction<boolean>>;
  setList: React.Dispatch<React.SetStateAction<ProjectPreview[]>>;
};

export function NewProjectModal({
  showPrompt,
  setShowPrompt,
  setList,
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
        <NewProjectForm
          loading={loading}
          setLoading={setLoading}
          setShowPrompt={setShowPrompt}
          setList={setList}
        />
      </Modal.Body>
    </Modal>
  );
}
