import { useContext, createContext } from "react";
import { AppContextProps } from "../App";

export const AppContext = createContext<AppContextProps | null>(null);

export function useAppContext() {
  return useContext(AppContext);
}
