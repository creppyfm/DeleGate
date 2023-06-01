import { useContext } from "react";
import { createContext } from "react";
import { AppContextProps } from "../App";

export const AppContext = createContext<AppContextProps>({
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
      const response = await fetch("/users/user"); // attempt to find user data
      if (response.ok) {
        // if user found (becuase session in httpOnly cookie) then process JSON
        const data = await response.json();
        setUser({ ...data, loggedIn: true }); // finally, log in user
      }
    } catch (error) {
      console.log(error);
    }
  }
}
