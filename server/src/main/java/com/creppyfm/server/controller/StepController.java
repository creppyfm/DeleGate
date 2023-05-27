package com.creppyfm.server.controller;
import com.creppyfm.server.model.Project;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/step")
@CrossOrigin
public class StepController {



/*
    @PostMapping("/{id}/tasks/delegate")
    public ResponseEntity<Project> delegateTasksForProject(@PathVariable String id) throws IOException, InterruptedException {
        //projectService.assignTasksAutomatically(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/tasks/generate")
    public ResponseEntity<Project> generateTasksForProject(@PathVariable String id) throws IOException {
        //projectService.generateTasksForProject(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
*/

}
