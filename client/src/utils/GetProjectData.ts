import { useContext, useState } from "react";
import { createContext } from "react";

export type Project = {
  id: string;
  userId: string;
  title: string;
  description: string;
  phase: string;
  created: string;
  updated: string;
  projectMembers: ProjectMember[];
  taskList: Task[];
  stepList: Step[];
};

export type ProjectMember = {
  id: string;
  role: string;
  currentTasks: string[];
  firstName: string;
  lastName: string;
};

export type Task = {
  id: string;
  stepId: string;
  title: string;
  description: string;
  weight: number;
  phase: string;
  created: string;
  updated: string;
  assignedUsers: string[];
};

export type Step = {
  id: string;
  projectId: string;
  title: string;
  description: string;
  taskList: string[];
};

interface ProjectContext {
  project: Project | null;
  setProject: (project: Project) => void;
}

export const ProjectContext = createContext<ProjectContext>({
  project: null,
  setProject: () => null,
});

export function useProjectContext() {
  return useContext(ProjectContext);
}
