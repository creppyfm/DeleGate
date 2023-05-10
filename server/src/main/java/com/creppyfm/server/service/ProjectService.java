package com.creppyfm.server.service;

import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.ProjectMembers;
import com.creppyfm.server.model.Task;
import com.creppyfm.server.model.User;
import com.creppyfm.server.openai_chat_handlers.OpenAIChatAPIManager;
import com.creppyfm.server.repository.ProjectRepository;
import com.creppyfm.server.repository.TaskRepository;
import com.creppyfm.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Project findProjectById(String id) {
        return projectRepository.findProjectById(id);
    }

    public List<ProjectMembers> findProjectMembersById(String id) {
        Project project = projectRepository.findProjectById(id);
        return project.getProjectMembers();
    }

    public Project createProject(String userId, String title, String description, String status) {
        Project project = projectRepository.insert(new Project(userId, title, description, status, LocalDateTime.now(), LocalDateTime.now()));
        mongoTemplate.update(User.class)
                .matching(Criteria.where("id").is(userId))
                .apply(new Update().push("projectIds").value(project.getId()))
                .first();
        return project;
    }

    public void generateTasksForProject(String id) throws IOException {
        Project project = findProjectById(id);

        String projectInfo = String.format("Title: %s\nDescription: %s\n\nList of tasks to complete the project:", project.getTitle(), project.getDescription());
        String prompt =
                "Read the project title and description included below, and generate a list " +
                        "of no more than 10 steps to complete the project. Each step must be " +
                        "on its own line. Each step should be presented in the form of a key:value pair " +
                        "containing the title of the step as the key, and a concise, 3 to 5 sentence description " +
                        "of the step as the value. The format of each step should match the following example:\n " +
                        "1. Setup Java & Spring Boot: Value: Install the Java runtime environment. Download and configure " +
                        "the Spring Boot application using the Spring Initializer, making sure to add the necessary dependencies. " +
                        "Confirm the file structure of the Spring Boot application matches the needs of your project.\n " +
                        "NOTE: Do not include any extra words, phrases, or sentences unrelated to the tasks you are generating." +
                        "Do not include phrases such as \"Sure, I can do that,\" or any phrases throughout " +
                        "or ending your response. ONLY return the list of generated tasks in the format requested above.\n" +
                        "Here is the project information:\n"
                        + projectInfo;

        // Call the OpenAIAPIManager to get the list of tasks
        OpenAIChatAPIManager openAIChatAPIManager = new OpenAIChatAPIManager();
        List<String> tasks;
        try {
            tasks = openAIChatAPIManager.buildsTaskList(prompt);
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }

        for (String response : tasks) {
            if (response.length() > 0) {
                System.out.println(response);
                String[] taskAndDescription = response.split(": Value: ");
                if (taskAndDescription.length > 1) {
                    String taskTitle = taskAndDescription[0];
                    String taskDescription = taskAndDescription[1];
                    Task task = taskService.createTask(id, taskTitle, taskDescription, 0, "in-progress");
                    project.getTaskList().add(task);
                }
            }
        }

        projectRepository.save(project);
    }

    public Project addProjectMember(String projectId, ProjectMembers projectMembers) {
        Project project = projectRepository.findProjectById(projectId);
        if (project != null) {
            project.getProjectMembers().add(projectMembers);
        }
        assert project != null;
        return projectRepository.save(project);
    }

    public boolean updateProjectMemberRoleByUserId(String projectId, String userId, String newRole) {
        Project project = projectRepository.findProjectById(projectId);
        boolean isUpdated = project.updateProjectMemberRoleByUserId(userId, newRole);
        if (isUpdated) {
            projectRepository.save(project);
        }
        return isUpdated;
    }

    public Project updateProject(String id, Project updatedProject) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project existingProject = optionalProject.get();
            existingProject.setTitle(updatedProject.getTitle());
            existingProject.setDescription(updatedProject.getDescription());
            existingProject.setPhase(updatedProject.getPhase());
            existingProject.setUpdated(LocalDateTime.now());

            // Update project in the user's projects list
            User user = userRepository.findByProjectIdsContaining(id);
            if (user != null) {
                int projectIndex = user.getProjectIds().indexOf(id);
                if (projectIndex >= 0) { // Check if the index is within bounds
                    user.getProjectIds().set(projectIndex, existingProject.getId());
                    userRepository.save(user);
                }
            }

            return projectRepository.save(existingProject);
        } else {
            return null;
        }
    }

    public boolean removeProjectMemberByUserId(String projectId, String userId) {
        Project project = projectRepository.findProjectById(projectId);
        return project.removeProjectMemberByUserId(userId) && projectRepository.save(project) != null;
    }

    public boolean deleteProject(String id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project existingProject = optionalProject.get();

            // Remove project from the user's projects list
            User user = userRepository.findByProjectIdsContaining(id);
            if (user != null) {
                user.getProjectIds().remove(id);
                userRepository.save(user);
            }

            // Remove associated tasks from the Task collection
            taskRepository.deleteAll(existingProject.getTaskList());

            projectRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
