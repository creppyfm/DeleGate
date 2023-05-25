package com.creppyfm.server.service;

import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.Task;
import com.creppyfm.server.model.User;
import com.creppyfm.server.repository.ProjectRepository;
import com.creppyfm.server.repository.TaskRepository;
import com.creppyfm.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(String id) {
        return userRepository.findUserById(id);
    }

    public List<Task> findCurrentTasksById(String id) {
        User user = userRepository.findUserById(id);
        return user.getCurrentTasks();
    }

    public List<String> findStrengthsById(String id) {
        User user = userRepository.findUserById(id);
        return user.getStrengths();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setToken(updatedUser.getToken());
            existingUser.setOpenAIKey(updatedUser.getOpenAIKey());
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setProvider(updatedUser.getProvider());
            existingUser.setProjectIds(updatedUser.getProjectIds());
            existingUser.setStrengths(updatedUser.getStrengths());
            existingUser.setCurrentTasks(updatedUser.getCurrentTasks());

            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    public boolean deleteUser(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            // Remove all associated projects and their tasks
            for (String projectId : existingUser.getProjectIds()) {
                // Remove associated tasks from the Task collection
                Project project = projectRepository.findProjectById(projectId);
                taskRepository.deleteAll(project.getTaskList());

                projectRepository.deleteById(project.getId());
            }

            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
