import { Offcanvas } from "react-bootstrap";

type OffCanvasPromptProps = {
  show: boolean;
  handleClose: () => void;
};

export function OffCanvasPrompt({ show, handleClose }: OffCanvasPromptProps) {
  return (
    <Offcanvas show={show} onHide={handleClose} placement="top">
      <Offcanvas.Header closeButton>
        <Offcanvas.Title>Offcanvas</Offcanvas.Title>
      </Offcanvas.Header>
      <Offcanvas.Body>
        Some text as placeholder. In real life you can have the elements you
        have chosen. Like, text, images, lists, etc.
      </Offcanvas.Body>
    </Offcanvas>
  );
}
