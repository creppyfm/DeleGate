package com.creppyfm.server.repository;

import com.creppyfm.server.enumerated.Provider;
import com.creppyfm.server.model.Project;
import com.creppyfm.server.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findUserById(String id);

    User findByProjectIdsContaining(String projectId);

    User findByProviderAndProviderId(Provider provider, String providerId);
}
