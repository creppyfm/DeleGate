import { Modal, Button } from "react-bootstrap";

type NewProjectModalProps = {
  show: boolean;
  onHide: () => void;
};

export function NewProjectModal(props: NewProjectModalProps) {
  return (
    <Modal
      {...props}
      size="lg"
      aria-labelledby="new-project-prompt-modal-vcenter"
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title id="new-project-prompt-modal-vcenter">
          Prompt Modal for New Project
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <h4>Centered Modal</h4>
        <p>
          Cras mattis consectetur purus sit amet fermentum. Cras justo odio,
          dapibus ac facilisis in, egestas eget quam. Morbi leo risus, porta ac
          consectetur ac, vestibulum at eros.
        </p>
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={props.onHide}>Close</Button>
      </Modal.Footer>
    </Modal>
  );
}
