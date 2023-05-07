import "./App.css";
import { Header } from "./components/Header";
import { Hero } from "./components/Hero";
import { Main } from "./components/Main";
import { FeaturetteDivider } from "./components/FeaturetteDivider";

function App() {
  return (
    <>
      <Header />
      <Hero />
      <FeaturetteDivider />
      <Main />
      <FeaturetteDivider />
      <footer className="mt-auto">
        <h1>Footer Here</h1>
      </footer>
    </>
  );
}

export default App;
