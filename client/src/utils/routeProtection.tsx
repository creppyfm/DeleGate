import { Navigate, useLocation } from "react-router-dom";
import { User } from "./GetUserData";

type ProtectedRouteProps = {
  user: User;
  children: JSX.Element;
};

export function ProtectedRoute({ user, children }: ProtectedRouteProps) {
  const location = useLocation();

  if (user.loggedIn) {
    return children;
  }
  return <Navigate to="/login" state={{ from: location }} replace />;
}
