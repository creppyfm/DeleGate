package com.creppyfm.server.controller;

import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.Step;
import com.creppyfm.server.service.ProjectService;
import com.creppyfm.server.service.StepService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/steps")
@CrossOrigin
@Tag(name = "Step Controller")
public class StepController {

    @Autowired
    private StepService stepService;
    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Step>> getAllSteps(@RequestBody String projectId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(projectId);
        return new ResponseEntity<List<Step>>(stepService.getAllSteps(jsonNode.get("projectId").asText()), HttpStatus.OK);
    }

    @GetMapping("/{projectId}/{id}")
    public ResponseEntity<Step> getStepById(@PathVariable String id) {
        Step step = stepService.getStepById(id);
        if (step != null) {
            return new ResponseEntity<>(step, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<List<Step>> getStepsByProjectId(@PathVariable String projectId) {
        Project project = projectService.findProjectById(projectId);
        if (project != null) {
            return new ResponseEntity<>(project.getStepList(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<Step> createStep(@PathVariable String projectId, @RequestBody Step step) {
        Project project = projectService.findProjectById(projectId);
        if (project != null) {
            Step createdStep = stepService.createStep(projectId, step.getTitle(), step.getDescription());
            project.getStepList().add(createdStep);
            projectService.updateProject(projectId, project);
            return new ResponseEntity<>(createdStep, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Step> updateStep(@PathVariable("id") String id, @RequestBody Step updatedStep) {
        Step step = stepService.updateStep(id, updatedStep);
        if (step != null) {
            return new ResponseEntity<>(step, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStep(@PathVariable("id") String id) {
        boolean isDeleted = stepService.deleteStep(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete-many")
    public ResponseEntity<Void> deleteManySteps(@RequestBody List<String> rejectedSteps) throws JsonProcessingException {
        boolean isDeleted = stepService.deleteManySteps(rejectedSteps);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
     * @PostMapping("/{id}/tasks/delegate")
     * public ResponseEntity<Project> delegateTasksForProject(@PathVariable String
     * id) throws IOException, InterruptedException {
     * //projectService.assignTasksAutomatically(id);
     * return new ResponseEntity<>(HttpStatus.OK);
     * }
     */
    @PostMapping("/tasks/generate")
    public ResponseEntity<Step> generateTasksForProject(@RequestBody String id) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parsed = objectMapper.readTree(id);

        stepService.generateTasksForStep(parsed.get("id").asText());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
