import { Featurette } from "./Featurette";
import { FeaturetteDivider } from "./FeaturetteDivider";

export function Main() {
  return (
    <>
      <Featurette
        heading="Some Content"
        subHeading="Some more content."
        content="Imagine some incredible prose here."
        reversed={false}
      />
      <FeaturetteDivider />
      <Featurette
        heading="Some Content"
        subHeading="Some more content."
        content="Imagine some incredible prose here."
        reversed={true}
      />
      <FeaturetteDivider />
      <Featurette
        heading="Some Content"
        subHeading="Some more content."
        content="Imagine some incredible prose here."
        reversed={false}
      />
    </>
  );
}
