import "./App.css";
import { Header } from "./components/Header";
import { Hero } from "./components/Hero";
import { Main } from "./components/Main";
import { FeaturetteDivider } from "./components/FeaturetteDivider";
import { Footer } from "./components/Footer";

function App() {
  return (
    <>
      <Header />
      <Hero />
      <FeaturetteDivider />
      <Main />
      <FeaturetteDivider />
      <Footer />
    </>
  );
}

export default App;
