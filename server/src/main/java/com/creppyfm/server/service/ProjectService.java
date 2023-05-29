package com.creppyfm.server.service;

import com.creppyfm.server.data_transfer_object_model.ProjectDataTransferObject;
import com.creppyfm.server.data_transfer_object_model.ProjectResponse;
import com.creppyfm.server.data_transfer_object_model.StepDataTransferObject;
import com.creppyfm.server.model.*;
import com.creppyfm.server.openai_chat_handlers.OpenAIChatAPIManager;
import com.creppyfm.server.repository.ProjectRepository;
import com.creppyfm.server.repository.StepRepository;
import com.creppyfm.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StepService stepService;
    @Autowired
    private StepRepository stepRepository;

    public List<ProjectResponse> findAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponse> projectResponses = new ArrayList<>();

        for (Project project : projects) {
            ProjectResponse projectResponse = new ProjectResponse(
                    project.getId(),
                    project.getTitle(),
                    project.getDescription(),
                    project.getPhase(),
                    project.getUpdated().toString()
            );
            projectResponses.add(projectResponse);
        }
        return projectResponses;
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


    /*
    * FOR TESTING:
    * COMMENT userId PARAMETER, userId IN createProject CALL
    *   ADD HARD-CODED userId
    * UNCOMMENT, REMOVE HARD-CODED ID WHEN NOT TESTING
    * */
    public Project createsProjectAndGeneratesSteps(String userId, String prompt) throws IOException, URISyntaxException, InterruptedException {
        OpenAIChatAPIManager openAIChatAPIManager = new OpenAIChatAPIManager();
        ProjectDataTransferObject projectDTO = new ProjectDataTransferObject();
        projectDTO = openAIChatAPIManager.buildsProjectDataTransferObject(prompt);

        String title = projectDTO.getTitle();
        String description = projectDTO.getDescription();
        List<StepDataTransferObject> stepDTOList = projectDTO.getSteps();

        Project project = createProject(userId, title, description, "in-progress");

        List<Step> stepList = new ArrayList<>();
        for (StepDataTransferObject stepDTO : stepDTOList) {
            Step step = stepService.createStep(project.getId(), stepDTO.getTitle(), stepDTO.getDescription());
            stepList.add(step);
        }
        project.setStepList(stepList);

        return projectRepository.save(project);
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

            stepRepository.deleteAll(existingProject.getStepList());

            projectRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
