import "./App.css";
import { Header } from "./components/Header";
import { Main } from "./components/Main";
import { FeaturetteDivider } from "./components/FeaturetteDivider";

function App() {
  return (
    <>
      <Header />
      <Main />
      <FeaturetteDivider />
      <footer className="mt-auto">
        <h1>Footer Here</h1>
      </footer>
    </>
  );
}

export default App;
