import { useContext } from "react";
import { createContext } from "react";

export type User = {
  firstName: string;
  lastName: string;
  email: string;
  loggedIn: boolean;
};

export type SetUser = React.Dispatch<
  React.SetStateAction<{
    firstName: string;
    lastName: string;
    email: string;
    loggedIn: boolean;
  }>
>;

interface AppContext {
  user: User;
  setUser: (user: User) => void;
}

export const AppContext = createContext<AppContext>({
  user: {
    firstName: "",
    lastName: "",
    email: "",
    loggedIn: false,
  },
  setUser: () => {},
});

export function useAppContext() {
  return useContext(AppContext);
}

export async function useGetUserDataIfExists() {
  const { user, setUser } = useContext(AppContext);
  if (!user.loggedIn) {
    try {
      const response = await fetch(
        `${import.meta.env.VITE_BACKEND_SERVER_URI}/users/user`,
        { credentials: "include", mode: "cors" }
      ); // attempt to find user data
      if (response.ok) {
        // if user found (becuase session in httpOnly cookie) then process JSON
        const data = await response.json();
        setUser({ ...data, loggedIn: true }); // finally, log in user
      }
    } catch (error) {
      if (import.meta.env.DEV) {
        console.log("Dev console: ", error);
      }
    }
  }
}
