package org.tutorials.wproject1.testing.integration;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.reactive.server.WebTestClient;
import org.tutorials.wproject1.controller.GroupMapper;
import org.tutorials.wproject1.model.GroupDTO;
import org.tutorials.wproject1.model.MemberDTO;
import org.tutorials.wproject1.repository.GroupRepository;
import org.tutorials.wproject1.repository.MemberRepository;
import org.tutorials.wproject1.testing.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.net.URI;
import java.util.ArrayList;
//import java.net.http.HttpHeaders;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import  org.junit.jupiter.api.Test;

//@Import(TestConfig.class)
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class Wproject1IntegrationTest {

    
    @LocalServerPort
    private int serverPort=8001;
    
    //@Autowired
    @MockBean
    private GroupRepository groupRepository;
    //@Autowired
    @MockBean
    private MemberRepository memberRepository;
    //@Autowired
    @MockBean GroupMapper groupMapper;

    private final String group1Name = "simpleGroup1";
    private final String group2Name = "testGroup2";
    private final String group2Member1Name = "member1";
    private final short group2Member1Rating = 2;
    private final String group2Member2Name = "member2";
    private final short group2Member2Rating = 3;

    private final String attr1 = "attr1";
    private final String attr2 = "attr1";

    private final String updatedGroup2Name = "testGroupU";  
    private final short updatedRating = 5;
    private final String updatedAttr1 = "updatedAttr1";

    private static final String BASE_URL_ROOT = "http://localhost:";
   
  
    TestRestTemplate testTemplate = new TestRestTemplate();
    
    @BeforeEach
    public void setup() {
        memberRepository.findAll().forEach(m -> {
            memberRepository.delete(m);
        });
        groupRepository.findAll().forEach(g -> {
              groupRepository.delete(g);
        });
    }

    @Test
    public void testFindGroupsAllOK() throws Exception {

        //Given : objects exist in db
        final String baseUrl = BASE_URL_ROOT+serverPort+"/groups/";
        final Long gid1=1L, gid2=2L, member1Id = 1L;
         GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).build();
         MemberDTO member1 = MemberDTO.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
         List<MemberDTO> members = new ArrayList<MemberDTO>();
         members.add(member1);
         //attributes
        GroupDTO groupDTO2 = GroupDTO.builder().gid(gid2).groupName(group2Name).members(members).attr1(attr1).build();
        List<GroupDTO> groupDTOs = Arrays.asList(groupDTO1);
        groupDTOs.add(groupDTO2);

        memberRepository.save(groupMapper.toMember(member1));
        groupRepository.save(groupMapper.toGroup(groupDTO1));

        //when retrieve groups by all
        ResponseEntity<String> response = testTemplate.getForEntity(baseUrl, String.class);
        //then verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(JsonUtil.asJsonString(groupDTOs), response.getBody());
      

    }

    @Test
    public void testFindGroupsByIdOK() throws Exception {

        //given entry exists in the db
        final Long gid1=1L;
        final String baseUrl = BASE_URL_ROOT+serverPort+"/group?gid="+gid1;
        GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).build();
        groupRepository.save(groupMapper.toGroup(groupDTO1));

        //when retrieving group by id
        ResponseEntity<GroupDTO> response = testTemplate.getForEntity(baseUrl, GroupDTO.class);
        //then verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groupDTO1, response.getBody());

    }
    
    @Test
    public void testFindGroupsByGroupName() throws Exception {
        //give entries exist in db
        final String baseUrl = BASE_URL_ROOT+serverPort+"/group/group2Name"+group2Name;
        final Long gid1=1L, gid2=2L;
        GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).build();
        
        GroupDTO groupDTO2 = GroupDTO.builder().gid(gid2).groupName(group2Name).attr1(attr1).build();
        List<GroupDTO> groupDTOs = Arrays.asList(groupDTO1);
        groupDTOs.add(groupDTO2);
        List<GroupDTO> expectedDTOs = Arrays.asList(groupDTO2);

        groupRepository.save(groupMapper.toGroup(groupDTO1));
        groupRepository.save(groupMapper.toGroup(groupDTO2));
        //when retrieving group by name
        ResponseEntity<String> response = testTemplate.getForEntity(baseUrl, String.class);
        //then verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(JsonUtil.asJsonString(expectedDTOs), response.getBody());

    }

    @Test
    public void testFindGroupsByMemberName() throws Exception {

        //given data exists in db
        final String baseUrl = BASE_URL_ROOT+serverPort+"/group/member/"+group2Member1Name;
        final Long gid1=1L, gid2=2L, member1Id = 1L, member2Id = 2L;
        GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).build();
        MemberDTO member1 = MemberDTO.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        MemberDTO member2 = MemberDTO.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
        List<MemberDTO> members = new ArrayList<MemberDTO>();
        members.add(member1);
        members.add(member2);
        GroupDTO groupDTO2 = GroupDTO.builder().gid(gid2).groupName(group2Name).members(members).build();
        List<GroupDTO> groupDTOs = Arrays.asList(groupDTO1);
        groupDTOs.add(groupDTO2);
        List<GroupDTO> expectedDTOs = Arrays.asList(groupDTO2);
        //entityManager.persist(group1);
        memberRepository.save(groupMapper.toMember(member1));
        groupRepository.save(groupMapper.toGroup(groupDTO1));
        //when retrieving group by member name
        ResponseEntity<String> response = testTemplate.getForEntity(baseUrl, String.class);
        //then verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(JsonUtil.asJsonString(expectedDTOs), response.getBody());
    }

    @Test
    public void testFindGroupsByMemberRating() throws Exception {
        //given db entries exist
        final String baseUrl = BASE_URL_ROOT+serverPort+"/group/member/"+group2Member1Rating;
        final Long gid1=1L, gid2=2L, member1Id = 1L, member2Id = 2L;
        GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).build();
        MemberDTO member1 = MemberDTO.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        MemberDTO member2 = MemberDTO.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
        List<MemberDTO> members = new ArrayList<MemberDTO>();
        members.add(member1);
        members.add(member2);
        GroupDTO groupDTO2 = GroupDTO.builder().gid(gid2).groupName(group2Name).members(members).build();
        List<GroupDTO> groupDTOs = Arrays.asList(groupDTO1);
        groupDTOs.add(groupDTO2);
        List<GroupDTO> expectedDTOs = Arrays.asList(groupDTO2);
        
        memberRepository.save(groupMapper.toMember(member1));
        groupRepository.save(groupMapper.toGroup(groupDTO1));
        groupRepository.save(groupMapper.toGroup(groupDTO2));
        //when retrieve group by member rating
        ResponseEntity<String> response = testTemplate.getForEntity(baseUrl, String.class);
        //then verify
         assertEquals(HttpStatus.OK, response.getStatusCode());
         assertEquals(JsonUtil.asJsonString(expectedDTOs), response.getBody());
    }

    @Test
    public void testPostGroupOK() throws Exception {
        //when group is posted
        final Long gid1=1L, member1Id = 1L;
        final String baseUrl = BASE_URL_ROOT+serverPort+"/group/";
        MemberDTO member1 = MemberDTO.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        List<MemberDTO> members = new ArrayList<MemberDTO>();
        members.add(member1);
        GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).members(members).attr1(attr1).attr2(attr2).build();
        HttpEntity<GroupDTO> request = new HttpEntity<>(groupDTO1);
        ResponseEntity<String> response = this.testTemplate.postForEntity(baseUrl, request, String.class);
        //then verifications
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(JsonUtil.asJsonString(groupDTO1), response.getBody());
    }

    @Test
    public void testUpdateGroupOK() throws Exception {
        //given group exists in db
        final Long gid1=1L, member1Id = 1L;
        final String baseUrl = BASE_URL_ROOT+serverPort+"/group/";
        MemberDTO member1 = MemberDTO.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        List<MemberDTO> members = new ArrayList<MemberDTO>();
        members.add(member1);

        GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).members(members).attr1(attr1).build();
       
        memberRepository.save(groupMapper.toMember(member1));
        groupRepository.save(groupMapper.toGroup(groupDTO1));

        //when updates with pu
        members.remove(member1);
        members.add(MemberDTO.builder().memberId(member1.getMemberId()).name(group2Member1Name).rating(updatedRating).build());
        
        GroupDTO modifiedGroupDTO = GroupDTO.builder().gid(gid1).groupName(updatedGroup2Name).attr1(updatedAttr1).members(members).build();
        HttpEntity<GroupDTO> request = new HttpEntity<>(modifiedGroupDTO);
        ResponseEntity<String> result = this.testTemplate.exchange(baseUrl, HttpMethod.PUT, request, String.class);
       
        //verify tehe updates
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(JsonUtil.asJsonString(modifiedGroupDTO), result.getBody());
    }

    @Test
    public void testDeleteGroupOK() throws Exception {
        //given group exists in db
        final Long gid1=1L, member1Id = 1L;
        final String baseUrl = BASE_URL_ROOT+serverPort+"/group/"+gid1;
        MemberDTO member1 = MemberDTO.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        List<MemberDTO> members = new ArrayList<MemberDTO>();
        members.add(member1);
        
        GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).members(members).attr1(attr1).build();
        memberRepository.save(groupMapper.toMember(member1));
     
        groupRepository.save(groupMapper.toGroup(groupDTO1));

        //when delete the group
        RequestEntity<Void> request = RequestEntity.delete(new URI(baseUrl)).build();
        ResponseEntity<Void> response = testTemplate.exchange(request, Void.class);
        //verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertFalse(groupRepository.findById(gid1).isPresent());
        assertFalse(memberRepository.findById(member1Id).isPresent());
    }
}