package com.creppyfm.server.controller;

import com.creppyfm.server.enumerated.Phase;
import com.creppyfm.server.model.Task;
import com.creppyfm.server.service.TaskService;
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

@CrossOrigin
@RestController
@RequestMapping("/tasks")
@Tag(name = "Task Controller")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping()
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        String stepId = task.getStepId();
        String title = task.getTitle();
        String description = task.getDescription();
        int generation = task.getGeneration();
        int weight = task.getWeight();
        Phase phase = task.getPhase();

        return new ResponseEntity<Task>(taskService.
                createTask(stepId, title, description, generation, weight, phase), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Task> decomposeTask(@PathVariable("id") String id) throws IOException {
        taskService.decomposesTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public List<Task> getAllTasks(@RequestBody String projectId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(projectId);
        return taskService.getAllTasks(jsonNode.get("projectId").asText());
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") String id, @RequestBody Task updatedTask) {
        Task task = taskService.updateTask(id, updatedTask);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") String id) {
        boolean isDeleted = taskService.deleteTask(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete-many")
    public ResponseEntity<Void> deleteManyTasks(@RequestBody List<String> rejectedTasks) throws JsonProcessingException {
        boolean isDeleted = taskService.deleteManyTasks(rejectedTasks);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
