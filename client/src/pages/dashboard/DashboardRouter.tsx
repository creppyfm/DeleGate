import { Route, Routes } from "react-router-dom";
import { DashboardPage } from "./default/DashboardPage";
import { ProjectPage } from "./project/ProjectPage";
import { TaskPage } from "./task/TaskPage";
import { StepPage } from "../step/StepPage";

export function DashboardRouter() {
  return (
    <Routes>
      <Route path="/" element={<DashboardPage />} />
      <Route path="/project/:id" element={<ProjectPage />} />
      <Route path="/step/:id" element={<StepPage />} />
      <Route path="/task/:id" element={<TaskPage />} />
    </Routes>
  );
}
