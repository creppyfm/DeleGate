package com.creppyfm.server.controller;

import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.Step;
import com.creppyfm.server.model.Task;
import com.creppyfm.server.openai_chat_handlers.OpenAIChatAPIManager;
import com.creppyfm.server.repository.ProjectRepository;
import com.creppyfm.server.repository.StepRepository;
import com.creppyfm.server.repository.TaskRepository;
import com.creppyfm.server.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StepService {

    @Autowired
    private StepRepository stepRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ProjectRepository projectRepository;

    public Step createStep(String projectId, String title, String description) {
        Step step = new Step();
        step.setProjectId(projectId);
        step.setTitle(title);
        step.setDescription(description);
        stepRepository.insert(step);

        mongoTemplate.update(Project.class)
                .matching(Criteria.where("id").is(projectId))
                .apply(new Update().push("stepList").value(step))
                .first();

        return step;
    }

    public List<Step> getAllSteps() {
        return stepRepository.findAll();
    }

    public Step getStepById(String id) {
        return stepRepository.findStepById(id);
    }

    public Step updateStep(String id, Step updatedStep) {
        Optional<Step> optionalStep = stepRepository.findById(id);
        if (optionalStep.isPresent()) {
            Step existingStep = optionalStep.get();
            existingStep.setTitle(updatedStep.getTitle());
            existingStep.setDescription(updatedStep.getDescription());

            Project project = projectRepository.findByStepListContaining(existingStep);
            if (project != null) {
                int stepIndex = project.getStepList().indexOf(existingStep);
                if (stepIndex >= 0) {
                    project.getStepList().set(stepIndex, existingStep);
                    projectRepository.save(project);
                }
            }

            return stepRepository.save(existingStep);
        } else {
            return null;
        }
    }

    public boolean deleteStep(String id) {
        Optional<Step> optionalStep = stepRepository.findById(id);
        if (optionalStep.isPresent()) {
            Step existingStep = optionalStep.get();

            Project project = projectRepository.findByStepListContaining(existingStep);
            if (project != null) {
                project.getStepList().remove(existingStep);
                projectRepository.save(project);
            }

            taskRepository.deleteAll(existingStep.getTaskList());
            stepRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public void generateTasksForStep(String id) throws IOException {
        Step step = stepRepository.findStepById(id);

        String stepInfo = String.format("Title: %s\nDescription: %s\n\nList of tasks to complete the step:", step.getTitle(), step.getDescription());
        String prompt =
                "Read the Step title and description included below, and generate a list " +
                        "of no more than 10 tasks to complete the project. Each task must be " +
                        "on its own line. Each task should be presented in the form of a key:value pair " +
                        "containing the title of the task as the key, and a 3 to 5 sentence description " +
                        "of the task as the value. Each task should be detailed and actionable. " +
                        "The format of each task should match the following example:\n " +
                        "1. Setup Java & Spring Boot: Value: Install the Java runtime environment. Download and configure " +
                        "the Spring Boot application using the Spring Initializer, making sure to add the necessary dependencies. " +
                        "Confirm the file structure of the Spring Boot application matches the needs of your project.\n " +
                        "NOTE: Do not include any extra words, phrases, or sentences unrelated to the tasks you are generating." +
                        "Do not include phrases such as \"Sure, I can do that,\" or any phrases throughout " +
                        "or ending your response. ONLY return the list of generated tasks in the format requested above.\n" +
                        "Here is the Step information:\n"
                        + stepInfo;

        OpenAIChatAPIManager openAIChatAPIManager = new OpenAIChatAPIManager();
        List<String> tasks;
        try {
            tasks = openAIChatAPIManager.buildsTaskList(prompt);
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }

        for (String response : tasks) {
            if (response.length() > 0) {
                System.out.println(response);
                String[] taskAndDescription = response.split(": Value: ");
                if (taskAndDescription.length > 1) {
                    String taskTitle = taskAndDescription[0];
                    String taskDescription = taskAndDescription[1];
                    Task task = taskService.createTask(id, taskTitle, taskDescription, 0, "in-progress");
                    step.getTaskList().add(task);
                }
            }
        }

        stepRepository.save(step);
    }



    /*
     * Below- moved to StepService from ProjectService
     * Have to modify to accommodate new Step layer
     * */
/*    public void assignTasksAutomatically(String projectId) throws IOException, InterruptedException {
        Project project = findProjectById(projectId);
        List<ProjectMembers> projectMembers = project.getProjectMembers();
        List<Task> tasks = project.getTaskList();

        // Map project members' strengths and tasks to send to the AI
        Map<String, List<String>> memberStrengths = new HashMap<>();
        for (ProjectMembers projectMember : projectMembers) {
            User user = userRepository.findUserById(projectMember.getUserId());
            memberStrengths.put(projectMember.getUserId(), user.getStrengths());
        }

        OpenAIChatAPIManager openAIChatAPIManager = new OpenAIChatAPIManager();
        Map<String, String> taskIdToUserId = openAIChatAPIManager.delegateTasks(memberStrengths, tasks);

        // Assign tasks to users based on AI recommendations
        for (Map.Entry<String, String> entry : taskIdToUserId.entrySet()) {
            String taskId = entry.getKey();
            String userId = entry.getValue();

            Task task = taskRepository.findTaskById(taskId);
            User user = userRepository.findUserById(userId);

            if (task != null && user != null) {
                user.getCurrentTasks().add(task);
                task.getAssignedUsers().add(user);
                userRepository.save(user);
                taskRepository.save(task);
            }
        }
    }*/
}
