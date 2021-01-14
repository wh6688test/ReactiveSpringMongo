package org.tutorials.wproject1.testing.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.tutorials.wproject1.Wproject1Application;
import org.tutorials.wproject1.model.Groups;
import org.tutorials.wproject1.model.Member;
import org.tutorials.wproject1.repository.GroupsRepository;
import org.tutorials.wproject1.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import  org.junit.jupiter.api.Test;


//@Import(TestConfig.class)
//@ActiveProfiles("it")
@SpringBootTest(classes = Wproject1Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
//@Transactional
@AutoConfigureTestDatabase
public class Wproject1IntegrationTest {

    
    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate testTemplate;
    
    @Autowired
    private GroupsRepository groupsRepository;
    @Autowired
    private MemberRepository memberRepository;
    
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
    private final String urlPrefix="/wrest";
   
    @BeforeEach 
    public void setUp(){
         groupsRepository.deleteAll();
         memberRepository.deleteAll();
    }
    @Test
    public void testFindGroupsAllOK() throws Exception {

        Groups group1 = Groups.builder().groupName(group1Name).build();
        Member member1 = Member.builder().name(group2Member1Name).rating(group2Member1Rating).build();
        List<Member> members = new ArrayList<Member>();
        members.add(member1);
         //attributes
        Groups group2 = Groups.builder().groupName(group2Name).members(members).attr1(attr1).build();
        List<Groups>groups = new ArrayList<>();
        //List<Groups> groups = Arrays.asList(group1);
        groups.add(group1);
        groups.add(group2);
        //memberRepository.save(member1);
        groupsRepository.save(group1);
        groupsRepository.save(group2);

        var response = testTemplate.getForEntity(urlPrefix+"/groups", String.class);
        //then verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(group1Name));
        assertTrue(response.getBody().contains(attr1));
        assertTrue(response.getBody().contains(group2Name));
        assertTrue(response.getBody().contains(group2Member1Name));
    }

    @Test
    public void testFindGroupsByIdOK() throws Exception {

        //given entry exists in the db
        final Long gid1=1L;
        final String baseUrl =urlPrefix+"/group?gid="+gid1;
        Groups group1 = Groups.builder().gid(gid1).groupName(group1Name).build();
        groupsRepository.save(group1);

        //when retrieving group by id
        ResponseEntity<Groups> response = testTemplate.getForEntity(baseUrl, Groups.class);
        //then verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(group1, response.getBody());

    }
    
    @Test
    public void testFindGroupsByGroupName() throws Exception {
        //give entries exist in db
        final String baseUrl = urlPrefix+"/group/"+group2Name;
        final Long gid1=1L;

        Groups group2 = Groups.builder().gid(gid1).groupName(group2Name).attr1(attr1).attr2(attr2).build();
        List<Groups> groups = new ArrayList<>();
        groups.add(group2);
        groupsRepository.save(group2);
        //when retrieving group by name
        ResponseEntity<String> response = testTemplate.getForEntity(baseUrl, String.class);
        //then verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
         assertTrue(response.getBody().contains(group2Name));
         assertTrue(response.getBody().contains(attr1));
         assertTrue(response.getBody().contains(attr2));
        //assertEquals(JsonUtil.asJsonString(groups), response.getBody());

    }

    @Test
    public void testFindGroupsByMemberName() throws Exception {

        //given data exists in db
        final String baseUrl = urlPrefix+"/group/member/name/"+group2Member1Name;
        final Long gid2=1L, member1Id = 1L, member2Id = 2L;
        
        Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
        
        Groups group2 = Groups.builder().gid(gid2).groupName(group2Name).attr1(attr1).attr2(attr2).members(members).build();
       
        List<Groups> groups = new ArrayList<>();
        
        groups.add(group2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        groupsRepository.save(group2);
        //when retrieving group by member name
        ResponseEntity<String> response = testTemplate.getForEntity(baseUrl, String.class);
        //then verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
         assertTrue(response.getBody().contains(group2Member1Name));
         assertTrue(response.getBody().contains(group2Member2Name));
         assertTrue(response.getBody().contains(group2Name));
         assertFalse(response.getBody().contains(group1Name));
    }

    @Test
    public void testFindGroupsByMemberRating() throws Exception {
        //given db entries exist
        final String baseUrl = urlPrefix+"/group/member/rating/"+group2Member1Rating;
        final Long gid1=1L, gid2=2L, member1Id = 1L, member2Id = 2L;
       
        Groups group1 = Groups.builder().gid(gid1).groupName(group1Name).build();
        Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
        Groups group2 = Groups.builder().gid(gid2).groupName(group2Name).members(members).build();
        List<Groups>groups = new ArrayList<>();
        //List<Groups> groups = Arrays.asList(group1);
        groups.add(group1);
        groups.add(group2);
        groupsRepository.save(group1);
        groupsRepository.save(group2);
        //when retrieve group by member rating
        ResponseEntity<String> response = testTemplate.getForEntity(baseUrl, String.class);
        //then verify
         assertEquals(HttpStatus.OK, response.getStatusCode());
         assertTrue(response.getBody().contains(group2Member1Name));
         assertTrue(response.getBody().contains(group2Member2Name));
         assertTrue(response.getBody().contains(group2Name));
         assertFalse(response.getBody().contains(group1Name));
    }

    @Test
    public void testPostGroupOK() throws Exception {
        //when group is posted
        final Long gid1=1L, member1Id = 2L;
        final String baseUrl = urlPrefix+"/group/";
       
        Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        Groups group1 = Groups.builder().gid(gid1).groupName(group1Name).members(members).attr1(attr1).attr2(attr2).build();
        HttpEntity<Groups> request = new HttpEntity<>(group1);
        ResponseEntity<String> response = this.testTemplate.postForEntity(baseUrl, request, String.class);
        //then verifications
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().contains(group2Member1Name));
        assertTrue(response.getBody().contains(String.valueOf(group2Member1Rating)));
        assertTrue(response.getBody().contains(group1Name));
        assertTrue(response.getBody().contains(attr1));
        assertTrue(response.getBody().contains(attr2));
    }

    @Test
    public void testUpdateGroupOK() throws Exception {
        //given group exists in db
        final Long gid1=2L, member1Id = 1L;
       
        Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        Groups group1 = Groups.builder().gid(gid1).groupName(group1Name).members(members).attr1(attr1).build();
       
        memberRepository.save(member1);
        groupsRepository.save(group1);

        //when updates with pu
        members.remove(member1);
        Member updatedMember = Member.builder().memberId(member1Id).name(group2Member1Name).rating(updatedRating).build();
        members.add(updatedMember);
        Groups modifiedGroups = Groups.builder().gid(gid1).groupName(updatedGroup2Name).attr1(updatedAttr1).attr2(attr2).members(members).build();
       
        final String baseUrl = BASE_URL_ROOT+serverPort+"/wrest/group/"+groupsRepository.findGroupsByGroupName(group1Name).get().get(0).getGid();
        
        HttpEntity<Groups> request = new HttpEntity<>(modifiedGroups);
        ResponseEntity<String> response = this.testTemplate.exchange(baseUrl, HttpMethod.PUT, request, String.class);
       
        //verify tehe updates
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
         assertTrue(response.getBody().contains(group2Member1Name));
         assertTrue(response.getBody().contains(String.valueOf(updatedRating)));
         assertTrue(response.getBody().contains(updatedGroup2Name));
         assertFalse(response.getBody().contains(group1Name));
         assertTrue(response.getBody().contains(String.valueOf(group2Member1Rating)));
        //assertEquals(JsonUtil.asJsonString(modifiedGroups), result.getBody());
    }

    @Test
    public void testDeleteGroupOK() throws Exception {
        //given group exists in db
        final Long gid1=1L, member1Id = 1L;

        Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        
        Groups group1 = Groups.builder().gid(gid1).groupName(group1Name).members(members).attr1(attr1).build();
        memberRepository.save(member1);
      
        groupsRepository.save(group1);
        final String baseUrl = urlPrefix + "/group/"+groupsRepository.findGroupsByGroupName(group1Name).get().get(0).getGid();
        //when delete the group
        RequestEntity<Void> request = RequestEntity.delete(new URI(baseUrl)).build();
        ResponseEntity<Void> response = testTemplate.exchange(request, Void.class);
        //verify the result
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        assertFalse(groupsRepository.findGroupsByGroupName(group1Name).isPresent());
    }
}