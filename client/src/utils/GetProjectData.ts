import { useContext } from "react";
import { createContext } from "react";
import { redirect } from "react-router-dom";

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
  setProject: (project: Project | null) => void;
}

export const ProjectContext = createContext<ProjectContext>({
  project: null,
  setProject: () => null,
});

export function useProjectContext() {
  return useContext(ProjectContext);
}

export async function useUpdateProjectData() {
  const { project, setProject } = useContext(ProjectContext);
  if (project) {
    try {
      const response = await fetch(`/projects/${project.id}`);
      if (response.ok) {
        const data: Project = await response.json();
        setProject(data);
      } else {
        setProject(null);
        redirect("/dashboard");
      }
    } catch (error) {
      setProject(null);
      redirect("/dashboard");
      console.log(error);
    }
  } else {
    setProject(null);
    redirect("/dashboard");
  }
}
