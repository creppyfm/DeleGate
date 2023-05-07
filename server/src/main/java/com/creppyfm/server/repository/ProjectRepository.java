package com.creppyfm.server.repository;

import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    Project findProjectById(String id);

    Project findByTaskListContaining(Task task);
}
