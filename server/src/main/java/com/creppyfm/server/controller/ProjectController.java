package com.creppyfm.server.controller;

import com.creppyfm.server.model.Project;
import com.creppyfm.server.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return new ResponseEntity<List<Project>>(projectService.findAllProjects(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getSingleProject(@PathVariable String id) {
        return new ResponseEntity<Project>(projectService.findProjectById(id), HttpStatus.OK);
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

/* --for openai task gen
    @PostMapping("/{id}/tasks/generate")
    public ResponseEntity<Project> generateTasksForProject(@PathVariable String id) throws IOException {
        projectService.generateTasksForProject(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
*/

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") String id, @RequestBody Project updatedProject) {
        Project project = projectService.updateProject(id, updatedProject);
        if (project != null) {
            return new ResponseEntity<>(project, HttpStatus.OK);
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
