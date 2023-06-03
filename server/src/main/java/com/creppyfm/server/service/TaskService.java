package com.creppyfm.server.service;

import com.creppyfm.server.enumerated.Phase;
import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.Step;
import com.creppyfm.server.model.Task;
import com.creppyfm.server.repository.ProjectRepository;
import com.creppyfm.server.repository.StepRepository;
import com.creppyfm.server.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Autowired
    private ProjectRepository projectRepository;

    public Task createTask(String stepId, String title, String description, int weight, Phase phase) {
        Task task = taskRepository.insert(new Task(stepId, title, description, weight, phase, LocalDateTime.now(), LocalDateTime.now())); //insert into 'Task' collection
        Project project = projectRepository.findByStepListContaining(stepRepository.findStepById(stepId));
        mongoTemplate.update(Step.class) //insert into 'Step->taskList' array
                .matching(Criteria.where("id").is(stepId))
                .apply(new Update().push("taskList").value(task.getId()))
                .first();
        mongoTemplate.update(Project.class) //insert into 'Step->taskList' array
                .matching(Criteria.where("id").is(project.getId()))
                .apply(new Update().push("taskList").value(task))
                .first();

        return task;
    }

    public List<Task> getAllTasks(String projectId) {
        Project project = projectRepository.findProjectById(projectId);
        return project.getTaskList();
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
            existingTask.setPhase(updatedTask.getPhase());
            existingTask.setUpdated(LocalDateTime.now());

            // Update task in the step's taskList
            Step step = stepRepository.findByTaskListContaining(existingTask.getId());
            Project project = projectRepository.findByStepListContaining(step);
            if (step != null) {
                int stepTaskIndex = step.getTaskList()
                        .indexOf(existingTask.getId());
                int projectTaskIndex = project.getTaskList()
                        .indexOf(existingTask);
                if (stepTaskIndex >= 0) { // Check if the index is within bounds
                    step.getTaskList().set(stepTaskIndex, existingTask.getId());
                    stepRepository.save(step);
                }
                if (projectTaskIndex >= 0) {
                    project.getTaskList().set(projectTaskIndex, existingTask);
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

            // Remove task from the step's taskList
            Step step = stepRepository.findByTaskListContaining(existingTask.getId());
            Project project = projectRepository.findByStepListContaining(step);
            if (step != null) {
                step.getTaskList().remove(existingTask.getId());
                project.getTaskList().remove(existingTask);
                stepRepository.save(step);
                projectRepository.save(project);
            }

            taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteManyTasks(List<String> rejectedTasks) throws JsonProcessingException {
        List<String> rejectedTaskIds = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (String jsonString : rejectedTasks) {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            rejectedTaskIds.add(jsonNode.get("id").asText());
        }

        List<Task> taskList = taskRepository.findAllById(rejectedTaskIds);
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
