import { Container, Row } from "react-bootstrap";
import { Featurette } from "../components/Featurette";
import { FeaturetteDivider } from "../components/FeaturetteDivider";

export function AboutUsPage() {
  return (
    <Container as="main" className="text-light pb-5 mt-3 overflow-auto">
      <Row className="text-center">
        <h1>Meet the Team</h1>
      </Row>
      <Featurette
        content="Meet Kenson, an accomplished Full Stack Developer hailing from the
          heartland of America - Arkansas. His unique skill set spans a broad
          spectrum of technologies, with a special focus on crafting robust
          APIs, stunning React interfaces, and top-tier iOS applications. His
          tool belt brims with a myriad of languages, frameworks, and APIs
          including JavaScript, TypeScript, React, NextJS, ExpressJS, MySQL,
          MongoDB, Firebase, Swift, SwiftUI, UIKit, SwiftNIO, Vapor, OpenAI API,
          and LLMs. Kenson's proficiency not only lies in creating exceptional
          tech but also in his dedication to empowering upcoming developers. A
          steadfast volunteer at Underdog Devs, he contributes significantly by
          leading weekly data structures and algorithms classes, hosting project
          standups, and mentoring fellow developers, thus fostering a thriving
          community of tech enthusiasts."
        imageURL={`${import.meta.env.BASE_URL}kenson.jpg`}
      />
      <FeaturetteDivider />
      <Featurette
        content="Our other cornerstone is Foli, a Backend Developer who possesses the
          knack of developing secure and scalable APIs using Java and Spring
          Boot. Having experience with technologies such as Java, Spring Boot,
          MongoDB Atlas, OpenAI's various API models, AWS EC2, AWS S3, MySQL,
          OAuth2, JavaFX, Scenebuilder, Heroku, and Vercel, Foli has proven his
          mettle in the tech arena. However, his unique journey from being a
          professional musician and a digital marketing agency owner to a
          software engineer has honed his creative and strategic thinking.
          Currently a Computer Science student on the verge of graduation,
          Foli's diverse background serves as an exceptional fusion of
          creativity, business acumen, and technical prowess, making him an
          integral part of our dynamic team."
        reversed
        imageURL={`${import.meta.env.BASE_URL}foli.jpg`}
      />
      <FeaturetteDivider />
      <Row as="section">
        <p className="lead p-3">
          Kenson and Foli together make an indomitable team, applying their
          unique backgrounds and wide-ranging technical skills to build products
          that are not only functionally excellent but also cater to real-world
          user experiences. Their dedication towards nurturing future talents
          sets them apart, making their venture a beacon in the realm of tech
          development.
        </p>
      </Row>
    </Container>
  );
}
