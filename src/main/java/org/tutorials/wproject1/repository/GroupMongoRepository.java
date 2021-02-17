package org.tutorials.wproject1.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.tutorials.wproject1.model.Group;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public interface  GroupMongoRepository extends ReactiveMongoRepository<Group, String> {
     
    @Query("{'groupName': ?0}")
    Mono<Group> findGroupByName (String groupName);
}
