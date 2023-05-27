package com.creppyfm.server.controller;

import com.creppyfm.server.data_transfer_object_model.ProjectResponse;
import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.ProjectMembers;
import com.creppyfm.server.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/projects")
@Tag(name = "Project Controller")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return new ResponseEntity<List<ProjectResponse>>(projectService.findAllProjects(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getSingleProject(@PathVariable String id) {
        return new ResponseEntity<Project>(projectService.findProjectById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/project-members")
    public ResponseEntity<List<ProjectMembers>> getProjectMembers(@PathVariable String id) {
        return new ResponseEntity<List<ProjectMembers>>(projectService.findProjectMembersById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        String userId = project.getUserId();
        String title = project.getTitle();
        String description = project.getDescription();
        String phase = project.getPhase();
        return new ResponseEntity<Project>(projectService.
                createProject(userId, title, description, phase), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Project> generateProjectWithSteps(@SessionAttribute("userId") String userId, @RequestBody String prompt) throws IOException {
        Project project = projectService.createsProjectAndGeneratesSteps(userId, prompt);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PostMapping("/{id}/project-members/add")
    public ResponseEntity<Project> addProjectMember(@PathVariable String id, @RequestBody ProjectMembers projectMember) {
        Project updatedProject = projectService.addProjectMember(id, projectMember);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") String id, @RequestBody Project updatedProject) {
        Project project = projectService.updateProject(id, updatedProject);
        if (project != null) {
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{projectId}/project-members/{userId}/role")
    public ResponseEntity<Void> updateProjectMemberRole(@PathVariable String projectId, @PathVariable String userId, String newRole) {
        boolean isUpdated = projectService.updateProjectMemberRoleByUserId(projectId, userId, newRole);
        if (isUpdated) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{projectId}/project-members/{userId}")
    public ResponseEntity<Void> removeProjectMember(@PathVariable String projectId, @PathVariable String userId) {
        boolean isRemoved = projectService.removeProjectMemberByUserId(projectId, userId);
        if (isRemoved) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") String id) {
        boolean isDeleted = projectService.deleteProject(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
