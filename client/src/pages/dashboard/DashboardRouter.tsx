import { Route, Routes } from "react-router-dom";
import { DashboardPage } from "./default/DashboardPage";
import { ProjectPage } from "./project/ProjectPage";
import { User } from "../../App";

type DashboardRouterProps = {
  user: User;
};

export function DashboardRouter({ user }: DashboardRouterProps) {
  return (
    <Routes>
      <Route path="/" element={<DashboardPage user={user} />} />
      <Route path="/project" element={<ProjectPage user={user} />} />
    </Routes>
  );
}
