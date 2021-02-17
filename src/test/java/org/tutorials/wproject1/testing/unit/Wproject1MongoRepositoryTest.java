package org.tutorials.wproject1.testing.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.repository.GroupMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.boot.test.context.SpringBootTest;
import org.tutorials.wproject1.Spring5ReactiveApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Spring5ReactiveApplication.class)
public class Wproject1MongoRepositoryTest {
    
    @Autowired
    private GroupMongoRepository groupRepository;

    private final String group1Name = "simpleGroup1";
    private final String group2Name = "testGroup2";
    private final String group2Member1Name = "member1";
    private final short group2Member1Rating = 2;
   
    private final String attr1 = "Programming";
    private final String attr2 = "Austin tx";

    private final String updatedGroup2Name = "testGroupU1";  
    private final short updatedRating = 4;
    private final String updatedAttr1 = "updatedAttr1";


    
    @Test
    public void testGroupRepositoryGetGroupByName() {
        Group group1 = Group.builder().gid("1").groupName(group1Name).build();
        groupRepository.save(group1).block();
      
        Mono<Group>groupsMono = groupRepository.findGroupByName(group1Name);

        StepVerifier.create(groupsMono).assertNext( retrievedGroup1 -> {
          assertEquals(group1Name, retrievedGroup1.getGroupName());
          assertNotNull(group1.getGid());
        })
      .expectComplete().verify();
    }

    @Test
    public void testGroupRepositoryFindAll() {

      Group group1 = Group.builder().gid("1").groupName(group1Name).build();
      Group group2 = Group.builder().gid("2").groupName(group2Name)
      .attr1(attr1).attr2(attr2).memberName(group2Member1Name).rating(group2Member1Rating).build();
 
        groupRepository.save(group1).block();
        groupRepository.save(group2).block();

        Flux<Group>groupsFlux = groupRepository.findAll();

        StepVerifier.create(groupsFlux).expectNextCount(2).assertNext( group2a -> {
          assertEquals(group2Name, group2a.getGroupName());
          assertEquals(group2Name, group2a.getAttr1());
          assertEquals(group2Name, group2a.getAttr2());
          assertEquals(group2Member1Name, group2a.getMemberName());
          assertEquals(group2Member1Rating, group2a.getRating());
          assertNotNull(group1.getGid());

      }).expectComplete().verify();
    }

    @Test
    public void testGroupRepositoryPost() {
        Group group2 = Group.builder().gid("1").groupName(group2Name)
      .attr1(attr1).attr2(attr2).memberName(group2Member1Name).rating(group2Member1Rating).build();
 
        Mono<Group>postedGroupMono = groupRepository.save(group2);

        StepVerifier.create(postedGroupMono).assertNext( group1 -> {
          assertEquals(group2Name, group1.getGroupName());
          assertEquals(attr1, group1.getAttr1());
          assertEquals(attr2, group1.getAttr2());
          assertEquals(group2Member1Name, group1.getMemberName());
          assertEquals(group2Member1Rating, group1.getRating());
          assertNotNull(group1.getGid());
       }).expectComplete().verify();

    }

    @Test
    public void testGroupRepositoryPut() {
        Group group2 = Group.builder().gid("1").groupName(group2Name)
      .attr1(attr1).attr2(attr2).memberName(group2Member1Name).rating(group2Member1Rating).build();
       
       groupRepository.save(group2);

       Group updatedGroup = Group.builder().gid("1").groupName(updatedGroup2Name)
      .attr1(updatedAttr1).attr2(attr2).memberName(group2Member1Name).rating(group2Member1Rating).build();
 
       groupRepository.findGroupByName(group2Name)
       .map(gg->gg.getGid()).subscribe(gid1->updatedGroup.setGid(gid1));
      
       Mono<Group>updatedGroupMono = groupRepository.save(updatedGroup);

        StepVerifier.create(updatedGroupMono).assertNext( group1 -> {
          assertEquals(updatedGroup2Name, group1.getGroupName());
          assertEquals(updatedAttr1, group1.getAttr1());
          assertEquals(attr2, group1.getAttr2());
          assertEquals(group2Member1Name, group1.getMemberName());
          assertEquals(updatedRating, group1.getRating());
          assertNotNull(group1.getGid());
       }).expectComplete().verify();

    }

        @Test
        public void testGroupRepositoryDeleteById() {

            Group group2 = Group.builder().gid("1").groupName(group2Name)
              .attr1(attr1).attr2(attr2).memberName(group2Member1Name).rating(group2Member1Rating).build();
     
            groupRepository.save(group2).block();
    
            groupRepository.deleteById(group2.getGid());
            Mono<Group> groupFindMono=groupRepository.findById(group2.getGid());
    
            StepVerifier.create(groupFindMono).expectNextCount(0).verifyComplete();
        }

        @Test
        public void testGroupRepositoryDeleteAll() {

            Group group2 = Group.builder().gid("1").groupName(group2Name)
              .attr1(attr1).attr2(attr2).memberName(group2Member1Name).rating(group2Member1Rating).build();
     
            groupRepository.save(group2).block();
    
            groupRepository.deleteAll();
            Flux<Group> groupFindFlux=groupRepository.findAll();
    
            StepVerifier.create(groupFindFlux).expectNextCount(0).verifyComplete();
        }


}
