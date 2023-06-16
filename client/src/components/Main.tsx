import { Featurette } from "./Featurette";
import { FeaturetteDivider } from "./FeaturetteDivider";

export function Main() {
  return (
    <>
      <Featurette
        heading="Intelligent Project Generation"
        subHeading="All driven by AI"
        // content="Imagine some incredible prose here."
        reversed={false}
        imageURL={import.meta.env.BASE_URL + "project_view.png"}
      />
      <FeaturetteDivider />
      <Featurette
        heading="Built in Task Assistant"
        subHeading="To maximize productivity"
        // content="Imagine some incredible prose here."
        reversed={true}
        imageURL={import.meta.env.BASE_URL + "task_view.png"}
      />
      {/* <FeaturetteDivider />
      <Featurette
        heading="Some Content"
        subHeading="Some more content."
        content="Imagine some incredible prose here."
        reversed={false}
      /> */}
    </>
  );
}
