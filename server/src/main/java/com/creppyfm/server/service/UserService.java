package com.creppyfm.server.service;

import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.User;
import com.creppyfm.server.repository.ProjectRepository;
import com.creppyfm.server.repository.TaskRepository;
import com.creppyfm.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(String id) {
        return userRepository.findUserById(id);
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
            existingUser.setSource(updatedUser.getSource());

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
