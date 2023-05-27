import { Hero } from "../components/Hero";
import { FeaturetteDivider } from "../components/FeaturetteDivider";
import { Main } from "../components/Main";
import { useGetUserDataIfExists } from "../utils/GetUserData";

export function Home() {
  useGetUserDataIfExists();

  return (
    <>
      <Hero />
      <FeaturetteDivider />
      <Main />
      <FeaturetteDivider />
    </>
  );
}
