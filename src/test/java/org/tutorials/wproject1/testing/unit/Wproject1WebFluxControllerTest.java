package org.tutorials.wproject1.testing.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.model.GroupDTO;
import org.tutorials.wproject1.repository.GroupMongoRepository;
import org.tutorials.wproject1.service.GroupService;

import reactor.core.publisher.Mono;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.tutorials.wproject1.Spring5ReactiveApplication;
import org.tutorials.wproject1.controller.Wproject1Controller;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers=Wproject1Controller.class)
@Import(GroupService.class)
public class Wproject1WebFluxControllerTest {

    @MockBean
    GroupMongoRepository repository;

    @Autowired
    private WebTestClient webClient;

    private final String UriPrefix="/wrest";
    private final String group1Name = "simpleGroup1";
    private final String group2Name = "testGroup2";
    private final String group2Member1Name = "member1";
    private final short group2Member1Rating = 2;
    private final String group2Member2Name = "member2";
    private final short group2Member2Rating = 3;

    private final String attr1 = "key1";
    private final String attr2 = "value1";

    private final String updatedGroup2Name = "testGroupU";  
    private final short updatedRating = 5;
    private final String updatedAttr1 = "updatedValue1";

    private Group group1, group2;
    private GroupDTO groupDTO1, updatedGroupDTO2;

    @BeforeEach
    public void setup() {
        final String gid1="1", gid2="2", member1Id = "1", member2Id = "2";

         //GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).build();
         group1 = Group.builder().gid(gid1).groupName(group1Name).build();
        
        //GroupDTO groupDTO2 = GroupDTO.builder().gid(gid2).groupName(group2Name).members(members).attr1(attr1).attr2(attr2).build();
         group2 = Group.builder().gid(gid2).groupName(group2Name)
           .attr1(attr1).attr2(attr2).memberName(group2Member1Name).rating(group2Member1Rating).build();
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        groups.add(group1);

        groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).build();
        
        updatedGroupDTO2 = GroupDTO.builder().gid(gid2).groupName(updatedGroup2Name)
           .attr1(attr2).attr2(updatedAttr1).memberName(group2Member1Name).rating(updatedRating).build();
        
    }

    @Test
    void testCreateGroup() {
        
        Mockito.when(repository.save(group1))
          .thenReturn(Mono.just(group1));

        webClient.post().uri(UriPrefix+"/group")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(groupDTO1))
        .exchange()
        .expectStatus().isCreated();

        Mockito.verify(repository, times(1)).save(group1);

    }

    @Test
    void testRetrieveGroupById() {
        
        Mockito.when(repository.findById("1"))
        .thenReturn(Mono.just(group1));

        webClient.get().uri(uriBuilder -> uriBuilder.path(UriPrefix+"group")
                  .queryParam("gid", 1L).build())
        .exchange().expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBody().jsonPath("$.gid").isNotEmpty()
        .jsonPath("$.name").isEqualTo("any")
        .jsonPath("$.member").isEqualTo("any")
        .jsonPath("$.rating").isEqualTo(2);
        Mockito.verify(repository, times(1)).findById("1");
    }

    @Test
    void testRetrieveAllGroups() {
        webClient.get().uri("/groups")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
        .expectBodyList(Group.class)
        .hasSize(2)
        .consumeWith(group ->{
            List<Group> groups = group.getResponseBody();
            groups.forEach( g ->{
                assertNotNull(g.getGid());
            });
        });
    }

    @Test
    void testUpdateGroups() {
        webClient.put()
        .uri(UriPrefix+"/group/{gid}", 1)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(updatedGroupDTO2), GroupDTO.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.name").isEqualTo(updatedGroup2Name)
        .jsonPath("$.rating").isEqualTo(updatedRating);
    }

    @Test
    void testDeleteGroup() {
      Mono<Void> voidReturn = Mono.empty();

      Mockito.when(repository.deleteById("1"))
       .thenReturn(voidReturn);

       webClient.get().uri("/delete/{id}", 1)
       .exchange()
       .expectStatus().isEqualTo(HttpStatus.ACCEPTED);
    }

}
