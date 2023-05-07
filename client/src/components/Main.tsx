import { Featurette } from "./Featurette";
import { FeaturetteDivider } from "./FeaturetteDivider";

export function Main() {
  return (
    <>
      <Featurette reversed={false} />
      <FeaturetteDivider />
      <Featurette reversed={true} />
      <FeaturetteDivider />
      <Featurette reversed={false} />
    </>
  );
}
