import { Hero } from "../components/Hero";
import { FeaturetteDivider } from "../components/FeaturetteDivider";
import { Main } from "../components/Main";
import { useAppContext } from "../utils/SessionContext";
import { useEffect } from "react";

export function Home() {
  const { user, setUser } = useAppContext();

  async function getUserDataIfExists() {
    const response = await fetch("/user"); // attempt to find user data
    if (response.ok) {
      // if user found (becuase session in httpOnly cookie) then process JSON
      const data = await response.json();
      setUser({ ...data, loggedIn: true }); // finally, log in user
    }
  }

  useEffect(() => {
    if (!user.loggedIn) {
      getUserDataIfExists();
    }
  }, []);

  return (
    <>
      <Hero />
      <FeaturetteDivider />
      <Main />
      <FeaturetteDivider />
    </>
  );
}
