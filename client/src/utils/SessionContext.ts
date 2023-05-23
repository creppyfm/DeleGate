import { useContext, createContext } from "react";
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
