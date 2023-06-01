package com.creppyfm.server.service;

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
                "Read the title and description of the Step included below, and generate a list " +
                        "of no more than 10 tasks to complete the Step. The format of your response should be a JSON array, where each " +
                        "element is an array containing two strings - the title of the task as the first element, and a 3 to 5 sentence description " +
                        "of the task as the second element. Each task should be detailed and actionable. " +
                        "The format of each task should match the following example: \n" +
                        "[\"Database Setup\", \"Download and install MySQL database server from the official website. After installation, " +
                        "secure the database server by running the security script that comes with the installation. Then, create a new database " +
                        "for the application and an account that can access and modify it. Finally, test the connection to the database from your " +
                        "application code to ensure everything is set up correctly.\"]\n" +
                        "An example of the complete response format is: \n" +
                        "[\n" +
                        "[\"Task one title\", \"Task one description\"],\n" +
                        "[\"Task two title\", \"Task two description\"],\n" +
                        "[\"Task three title\", \"Task three description\"],\n" +
                        "...\n" +
                        "].\n" +
                        "NOTE: Do not include any extra words, phrases, or sentences unrelated to the tasks you are generating." +
                        "Do not include phrases such as \"Sure, I can do that,\" or any phrases throughout " +
                        "or ending your response. Do not include numbering for the tasks. Do not include a comma after " +
                        "the final sub array (the final task.) ONLY return the list of generated tasks " +
                        "in the format requested above.\n" +
                        "Here is the Step information:\n"
                        + stepInfo;

        OpenAIChatAPIManager openAIChatAPIManager = new OpenAIChatAPIManager();
        List<List<String>> tasks;
        try {
            tasks = openAIChatAPIManager.buildsTaskList(prompt);
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }

        List<Task> taskList = new ArrayList<>();
        for (List<String> list : tasks) {
            if (list.size() == 2) {
                String taskTitle = list.get(0);
                String taskDescription = list.get(1);
                Task task = taskService.createTask(id, taskTitle, taskDescription, 0, "in-progress");
                taskList.add(task);
            }
        }
        step.setTaskList(taskList);
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
