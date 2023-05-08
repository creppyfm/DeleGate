package com.creppyfm.server.service;

import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.ProjectMembers;
import com.creppyfm.server.model.User;
import com.creppyfm.server.repository.ProjectRepository;
import com.creppyfm.server.repository.TaskRepository;
import com.creppyfm.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

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
