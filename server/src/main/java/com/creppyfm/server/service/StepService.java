package com.creppyfm.server.service;

import com.creppyfm.server.enumerated.Phase;
import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.Step;
import com.creppyfm.server.model.Task;
import com.creppyfm.server.openai_chat_handlers.OpenAIChatAPIManager;
import com.creppyfm.server.repository.ProjectRepository;
import com.creppyfm.server.repository.StepRepository;
import com.creppyfm.server.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
    @Autowired
    OpenAIChatAPIManager openAIChatAPIManager;

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

    public List<Step> getAllSteps(String projectId) {
        Project project = projectRepository.findProjectById(projectId);
        return project.getStepList();
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

            if (!existingStep.getTaskList().isEmpty()) {
                for (String taskId : existingStep.getTaskList()) {
                    Task task = taskRepository.findTaskById(taskId);
                    // remove task from project.taskList
                    if (project != null) {
                        project.getTaskList().remove(task);
                    }
                    // remove task from taskRepository
                    taskRepository.delete(task);
                }
            }
            stepRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteManySteps(List<String> rejectedSteps) throws JsonProcessingException {
        List<String> rejectedStepIds = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (String jsonString : rejectedSteps) {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            rejectedStepIds.add(jsonNode.get("id").asText());
        }

        List<Step> stepList = stepRepository.findAllById(rejectedStepIds);
        if (!stepList.isEmpty()) {
            for (Step step : stepList) {
                deleteStep(step.getId());
            }
            return true;
        } else {
            return false;
        }
    }

    public void generateTasksForStep(String id) throws IOException {
        System.out.println("Id inside of generateTasksForProject: " + id);

        Optional<Step> optionalStep = stepRepository.findById(id);

        if (optionalStep.isPresent()) {
            Step step = optionalStep.get();
            String stepInfo = String.format("Title: %s\nDescription: %s\n\nList of tasks to complete the step:",
                    step.getTitle(), step.getDescription());
            String prompt = "Read the title and description of the Step included below, and generate a list " +
                    "of no more than 10 tasks to complete the Step. The format of your response should be a JSON array, where each "
                    +
                    "element is an array containing two strings - the title of the task as the first element, and a 3 to 5 sentence description "
                    +
                    "of the task as the second element. Each task should be detailed and actionable. " +
                    "The format of each task should match the following example: \n" +
                    "[\"Database Setup\", \"Download and install MySQL database server from the official website. After installation, "
                    +
                    "secure the database server by running the security script that comes with the installation. Then, create a new database "
                    +
                    "for the application and an account that can access and modify it. Finally, test the connection to the database from your "
                    +
                    "application code to ensure everything is set up correctly.\"]\n" +
                    "An example of the complete response format is: \n" +
                    "[\n" +
                    "[\"Task one title\", \"Task one description\"],\n" +
                    "[\"Task two title\", \"Task two description\"],\n" +
                    "[\"Task three title\", \"Task three description\"],\n" +
                    "[\"Task four title\", \"Task four description\"],\n" +
                    "[\"Task five title\", \"Task five description\"],\n" +
                    "[\"Task six title\", \"Task six description\"],\n" +
                    "[\"Task seven title\", \"Task seven description\"],\n" +
                    "[\"Task eight title\", \"Task eight description\"],\n" +
                    "[\"Task nine title\", \"Task nine description\"],\n" +
                    "[\"Task ten title\", \"Task ten description\"]\n" +
                    "]\n" +
                    "NOTE: Do not include any extra words, phrases, or sentences unrelated to the tasks you are generating."
                    +
                    "Do not include phrases such as \"Sure, I can do that,\" or any phrases throughout " +
                    "or ending your response. Do not include numbering for the tasks. Do not include a comma after " +
                    "the final array (the final task.) ONLY return the list of generated tasks " +
                    "in the format requested above.\n" +
                    "Here is the Step information:\n"
                    + stepInfo;

            List<List<String>> tasks;
            try {
                tasks = openAIChatAPIManager.buildsTaskList(prompt);
            } catch (IOException | InterruptedException e) {
                throw new IOException(e);
            }

            Project project = projectRepository.findByStepListContaining(step);
            List<Task> taskList = new ArrayList<>();
            List<String> taskIds = new ArrayList<>();
            for (List<String> list : tasks) {
                if (list.size() == 2) {
                    String taskTitle = list.get(0);
                    String taskDescription = list.get(1);
                    Task task = taskService.createTask(id, taskTitle, taskDescription, 0, 0, Phase.NOT_STARTED);
                    taskList.add(task);
                    taskIds.add(task.getId());
                }
            }
            project.getTaskList().addAll(taskList);

            step.getTaskList().addAll(taskIds);

        }
    }

    /*
     * Below- moved to StepService from ProjectService
     * Have to modify to accommodate new Step layer
     */
    /*
     * public void assignTasksAutomatically(String projectId) throws IOException,
     * InterruptedException {
     * Project project = findProjectById(projectId);
     * List<ProjectMembers> projectMembers = project.getProjectMembers();
     * List<Task> tasks = project.getTaskList();
     * 
     * // Map project members' strengths and tasks to send to the AI
     * Map<String, List<String>> memberStrengths = new HashMap<>();
     * for (ProjectMembers projectMember : projectMembers) {
     * User user = userRepository.findUserById(projectMember.getUserId());
     * memberStrengths.put(projectMember.getUserId(), user.getStrengths());
     * }
     * 
     * OpenAIChatAPIManager openAIChatAPIManager = new OpenAIChatAPIManager();
     * Map<String, String> taskIdToUserId =
     * openAIChatAPIManager.delegateTasks(memberStrengths, tasks);
     * 
     * // Assign tasks to users based on AI recommendations
     * for (Map.Entry<String, String> entry : taskIdToUserId.entrySet()) {
     * String taskId = entry.getKey();
     * String userId = entry.getValue();
     * 
     * Task task = taskRepository.findTaskById(taskId);
     * User user = userRepository.findUserById(userId);
     * 
     * if (task != null && user != null) {
     * user.getCurrentTasks().add(task);
     * task.getAssignedUsers().add(user);
     * userRepository.save(user);
     * taskRepository.save(task);
     * }
     * }
     * }
     */
}
