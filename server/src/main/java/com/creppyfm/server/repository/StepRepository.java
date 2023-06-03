package com.creppyfm.server.repository;

import com.creppyfm.server.model.Step;
import com.creppyfm.server.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends MongoRepository<Step, String> {
    Step findStepById(String id);

    Step findByTaskListContaining(String taskId);

}
