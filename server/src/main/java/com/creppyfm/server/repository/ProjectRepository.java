package com.creppyfm.server.repository;

import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.Step;
import com.creppyfm.server.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    Project findProjectById(String id);

    List<Project> findAllByUserId(String userId);

    Project findByStepListContaining(Step step);

}
