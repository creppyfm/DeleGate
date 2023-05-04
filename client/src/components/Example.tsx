import Alert from "react-bootstrap/Alert";

export function Example() {
  return (
    <Alert dismissible variant="primary">
      <Alert.Heading>Oh snap! You got an alert!</Alert.Heading>
      <p>Now you get to read this cool message.</p>
    </Alert>
  );
}
