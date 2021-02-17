package org.tutorials.wproject1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tutorials.wproject1.exception.BusinessException;
import org.tutorials.wproject1.exception.InvalidInputException;
import org.tutorials.wproject1.exception.ResourceNotFoundException;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.repository.GroupMongoRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@AllArgsConstructor
//@RequiredArgsConstructor
@NoArgsConstructor
//@Slf4j
@Service
public class GroupService  {

    @Autowired
    private GroupMongoRepository groupRepository;

    public Flux<Group> findAll() {
        return groupRepository.findAll();
    }
    private static final String INVALID_INPUT="resource not found";
    private static final String RESOURCE_NOT_FOUND="resource not found";

    public Mono<Group> findGroupById(String id) throws InvalidInputException, BusinessException { 
        if (id == null) throw new InvalidInputException(INVALID_INPUT);
        
        Mono<Group>retrievedGroup=groupRepository.findById(id).onErrorMap(e->new BusinessException(e.getMessage()));
       
        return retrievedGroup.switchIfEmpty(Mono.error(
            new ResourceNotFoundException(RESOURCE_NOT_FOUND)));

    }


    public Mono<Group> createGroup(Group group1) throws InvalidInputException, BusinessException{
        if (group1 == null) throw new InvalidInputException(INVALID_INPUT);
       
        return groupRepository.save(group1).onErrorMap(e -> new BusinessException(e.getMessage()));
    }

    public Mono<Group> updateGroup(Group group1) throws InvalidInputException, ResourceNotFoundException, BusinessException {

        if (group1 == null) throw new InvalidInputException(INVALID_INPUT);
        if ((group1.getGid()).isEmpty()) throw new InvalidInputException(INVALID_INPUT);
        
        groupRepository.findById(group1.getGid()).switchIfEmpty(Mono.error(
            new ResourceNotFoundException(RESOURCE_NOT_FOUND)));

        return groupRepository.save(group1).onErrorMap(e -> new BusinessException(e.getMessage()));
    }

    public Mono<Group> findGroupByName(String groupName) throws InvalidInputException, ResourceNotFoundException, BusinessException {
        
        if (groupName.isEmpty()) throw new InvalidInputException(INVALID_INPUT);
        Group emptyGroup = new Group();
        return groupRepository.findById(groupName)
            .defaultIfEmpty(emptyGroup).onErrorMap(e->new BusinessException(e.getMessage()));
    }

    public Mono<Void> deleteById(String id) throws InvalidInputException, ResourceNotFoundException, BusinessException {
        if (id.isEmpty()) throw new InvalidInputException(INVALID_INPUT);
        groupRepository.findById(id).switchIfEmpty(Mono.error(
            new ResourceNotFoundException(RESOURCE_NOT_FOUND)));

        return groupRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return groupRepository.deleteAll().onErrorMap(e->new BusinessException(e.getMessage()));
    }
}
