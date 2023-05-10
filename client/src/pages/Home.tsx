import { Hero } from "../components/Hero";
import { FeaturetteDivider } from "../components/FeaturetteDivider";
import { Main } from "../components/Main";

export function Home() {
  return (
    <>
      <Hero />
      <FeaturetteDivider />
      <Main />
      <FeaturetteDivider />
    </>
  );
}
