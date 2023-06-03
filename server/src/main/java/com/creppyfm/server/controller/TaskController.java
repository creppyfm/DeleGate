package com.creppyfm.server.controller;

import com.creppyfm.server.enumerated.Phase;
import com.creppyfm.server.model.Task;
import com.creppyfm.server.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        int weight = task.getWeight();
        Phase phase = task.getPhase();

        return new ResponseEntity<Task>(taskService.
                createTask(stepId, title, description, weight, phase), HttpStatus.OK);
    }

    @GetMapping
    public List<Task> getAllTasks(@RequestBody String projectId) {
        return taskService.getAllTasks(projectId);
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
    public ResponseEntity<Void> deleteManyTasks(@RequestBody List<String> rejectedTasks) {
        boolean isDeleted = taskService.deleteManyTasks(rejectedTasks);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
