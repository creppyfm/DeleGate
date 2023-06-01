package com.creppyfm.server.service;

import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.Step;
import com.creppyfm.server.model.Task;
import com.creppyfm.server.repository.ProjectRepository;
import com.creppyfm.server.repository.StepRepository;
import com.creppyfm.server.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private StepRepository stepRepository;

    public Task createTask(String stepId, String title, String description, int weight, String status) {
        Task task = taskRepository.insert(new Task(stepId, title, description, weight, status, LocalDateTime.now(), LocalDateTime.now())); //insert into 'Task' collection
        mongoTemplate.update(Project.class) //insert into 'Step->taskList' array
                .matching(Criteria.where("id").is(stepId))
                .apply(new Update().push("taskList").value(task))
                .first();

        return task;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(String id) {
        Task task = taskRepository.findTaskById(id);
        if (task != null) {
            return task;
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }

    public Task updateTask(String id, Task updatedTask) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStatus(updatedTask.getStatus());
            existingTask.setUpdated(LocalDateTime.now());

            // Update task in the step's taskList
            Step step = stepRepository.findByTaskListContaining(existingTask);
            if (step != null) {
                int taskIndex = step.getTaskList().indexOf(existingTask);
                if (taskIndex >= 0) { // Check if the index is within bounds
                    step.getTaskList().set(taskIndex, existingTask);
                    stepRepository.save(step);
                }
            }

            return taskRepository.save(existingTask);
        } else {
            return null;
        }
    }

    public boolean deleteTask(String id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();

            // Remove task from the step's taskList
            Step step = stepRepository.findByTaskListContaining(existingTask);
            if (step != null) {
                step.getTaskList().remove(existingTask);
                stepRepository.save(step);
            }

            taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteManyTasks(List<String> rejectedTasks) {
        List<Task> taskList = taskRepository.findAllById(rejectedTasks);
        if (!taskList.isEmpty()){
            for (Task task : taskList) {
                deleteTask(task.getId());
            }
            return true;
        } else {
            return false;
        }
    }
}
