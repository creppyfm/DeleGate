import { Route, Routes } from "react-router-dom";
import { DashboardPage } from "./default/DashboardPage";
import { ProjectPage } from "./project/ProjectPage";

export function DashboardRouter() {
  return (
    <Routes>
      <Route path="/" element={<DashboardPage />} />
      <Route path="/project/:id" element={<ProjectPage />} />
    </Routes>
  );
}
