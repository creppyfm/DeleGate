package com.creppyfm.server.service;

import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.Task;
import com.creppyfm.server.repository.ProjectRepository;
import com.creppyfm.server.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProjectRepository projectRepository;

    public Task createTask(String projectId, String title, String description, int weight, String status) {
        Task task = taskRepository.insert(new Task(projectId, title, description, weight, status, LocalDateTime.now(), LocalDateTime.now())); //insert into 'Task' collection
        mongoTemplate.update(Project.class) //insert into 'Project->taskList' array
                .matching(Criteria.where("id").is(projectId))
                .apply(new Update().push("taskList").value(task))
                .first();

        return task;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(String id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return task.get();
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

            // Update task in the project's taskList
            Project project = projectRepository.findByTaskListContaining(existingTask);
            if (project != null) {
                int taskIndex = project.getTaskList().indexOf(existingTask);
                if (taskIndex >= 0) { // Check if the index is within bounds
                    project.getTaskList().set(taskIndex, existingTask);
                    projectRepository.save(project);
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

            // Remove task from the project's taskList
            Project project = projectRepository.findByTaskListContaining(existingTask);
            if (project != null) {
                project.getTaskList().remove(existingTask);
                projectRepository.save(project);
            }

            taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
