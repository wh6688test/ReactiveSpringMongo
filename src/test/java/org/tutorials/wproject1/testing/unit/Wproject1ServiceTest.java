//@MockBean : service layer unit tests
package org.tutorials.wproject1.testing.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.tutorials.wproject1.exception.BusinessException;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.model.Member;
import org.tutorials.wproject1.repository.GroupRepository;
import org.tutorials.wproject1.repository.MemberRepository;
import org.tutorials.wproject1.service.GroupService;

@DataJpaTest
public class Wproject1ServiceTest {

    @Mock
    GroupRepository groupRepository;
    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    private GroupService groupService;
    
    private final String group1Name = "simpleGroup1";
    private final String group2Name = "testGroup2";
    private final String group2Member1Name = "member1";
    private final short group2Member1Rating = 2;
    private final String group2Member2Name = "member2";
    private final short group2Member2Rating = 3;

    private final String attr1 = "attr1";
    private final String attr2 = "attr2";

    private final String updatedGroup2Name = "testGroupU";  
    private final String updatedMemberName="updatedMember1";
    private final short updatedRating = 5;
    private final String updatedAttr1 = "updatedAttr1";

    @Test
    public void testGroupServiceFindAll() throws Exception {
        //Generating the Entity Object
        final Long gid1=1L, member1Id = 1L, member2Id = 2L;
        Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
 
        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
         
        Group group2 = Group.builder().gid(gid1).groupName(group2Name).members(members).attr1(attr1).attr2(attr2).build();
        
        List<Group> expectedGroups = Arrays.asList(group2);

        //Mock preconditions 
        doReturn(expectedGroups).when(groupRepository).findAll();

        //Service verification against Repository Mock
        List<Group> actualGroups = groupService.findAll();
        assertNotNull(actualGroups); 
        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGroupServiceFindById() throws Exception {
         Long gid=1L;
         //Generating the Entity Object
         Group expectedGroup = Group.builder().gid(gid).groupName(group2Name).build();
        
         //Mock preconditions : doReturn is more flexible than when ... thenReturn...
         //doReturn(expectedGroup).when(groupRepository).findById(expectedGroup.getGid());
         when(groupRepository.findById(gid)).thenReturn(Optional.of(expectedGroup));
        
         //Service verification against Repository Mock
         Group actualGroup = groupService.findGroupById(gid);
         assertNotNull(actualGroup); 
         assertEquals(expectedGroup, actualGroup);
    }

    @Test
    public void testGroupServiceFindByGroupName() throws Exception {
        //construct the objects
        Long gid1=1L, gid2=2L;
        
        Group group1 = Group.builder().gid(gid1).groupName(group1Name).build();
        Group group2 = Group.builder().gid(gid2).groupName(group1Name).build();
        List<Group>expectedGroups = Arrays.asList(group1);
        expectedGroups.add(group2);

        //Mock precondition
        when(groupRepository.findGroupsByGroupName(group1Name)).thenReturn(Optional.of(expectedGroups));
        
        //Service verification against Repository Mock
        List<Group> actualGroups = groupService.findGroupByName(group1Name);
        assertNotNull(actualGroups); 
        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGroupServiceFindByGroupMemberName() throws Exception {
        //construct the objects
        Long gid2=2L;

        Member member1 = Member.builder().name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().name(group2Member2Name).rating(group2Member2Rating).build();

        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
        Group group2 = Group.builder().gid(gid2).groupName(group2Name).members(members).build();

        List<Group> expectedGroups = Arrays.asList(group2);
        //mocked preconditions based on the constructed object
        doReturn(expectedGroups).when(groupRepository).findGroupsByMemberName(group2Member1Name);
        //Service verification against Repository Mock
        List<Group> actualGroups = groupService.findGroupsByMemberName(group2Member2Name);
        assertNotNull(actualGroups); 
        assertEquals(expectedGroups, actualGroups);

    }
    
    @Test
    public void testGroupServiceFindByMemberRating() throws Exception {
        
        //construct the objects
        Long gid2=1L;
        Member member1 = Member.builder().name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().name(group2Member2Name).rating(group2Member2Rating).build();

        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
        Group group2 = Group.builder().gid(gid2).groupName(group2Name).members(members).build();

        List<Group> expectedGroups = Arrays.asList(group2);
        //mocked preconditions based on the constructed object
        doReturn(expectedGroups).when(groupRepository).findGroupsByMemberRating(group2Member1Rating);
        //Service verification against Repository Mock
        List<Group> actualGroups = groupService.findGroupsByMemberRating(group2Member1Rating);
        assertNotNull(actualGroups); 
        assertEquals(expectedGroups, actualGroups);
    }


    @Test
    public void testGroupServiceCreate() throws Exception {
        
         //construct the objects
         Long gid2=2L, member1Id=1L, member2Id=2L;

         Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
         Member member2 = Member.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
 
         List<Member> members = new ArrayList<Member>();
         members.add(member1);
         members.add(member2);
         
         Group group = Group.builder().gid(gid2).groupName(group2Name).attr1(attr1).attr2(attr2).members(members).build();
         Group expectedGroup = group;
         //mocked preconditions based on the constructed object
         doReturn(expectedGroup).when(groupRepository).save(group);

         //Service verification against Repository Mock
         Group actualGroup = groupService.createGroup(group);
         assertNotNull(actualGroup); 
         assertEquals(expectedGroup, actualGroup);
    }

    @Test
    public void testGroupServiceUpdate() throws Exception {
        
         //construct the objects
         Long gid2=2L, member1Id=1L, member2Id=2L;

         Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
         Member member2 = Member.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
 
         List<Member> members = new ArrayList<Member>();
         members.add(member1);
         members.add(member2);
         
         Group group2 = Group.builder().gid(gid2).groupName(group2Name).attr1(attr1).attr2(attr2).members(members).build();
        
         //mocked preconditions based on the constructed object
         doReturn(group2).when(groupRepository).findById(gid2);

         //updates
         List<Member> updatedMembers = new ArrayList<Member>();
         
         Member updatedMember = Member.builder().memberId(member1Id).name(updatedMemberName).rating(updatedRating).build();
         updatedMembers.add(updatedMember);
    
         Group updatedGroup = Group.builder().gid(gid2).groupName(updatedGroup2Name).attr1(updatedAttr1).attr2(attr2).members(updatedMembers).build();
        
         List<Group> expectedUpdatedGroups = Arrays.asList(updatedGroup);
         
         //Service verification against Repository Mock
         Group actualUpdatedGroup = groupService.updateGroup(gid2, updatedGroup);

         assertNotNull(actualUpdatedGroup); 
         assertEquals(expectedUpdatedGroups, actualUpdatedGroup);
    }


    @Test
    public void testGroupServiceDelete() throws Exception {
        
        //construct the objects
        Long gid2=2L, member1Id=1L, member2Id=2L;

        Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();

        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
        
        
        Group group2 = Group.builder().gid(gid2).groupName(group2Name).members(members).attr1(attr1).build();
       
        doReturn(group2).when(groupRepository).findById(gid2);

        //mocked preconditions based on the constructed object
        doNothing().when(groupRepository).delete(group2);
        groupService.deleteGroup(group2);
        assertNull(groupService.findGroupById(gid2));

    }

    @Test
    public void testGroupServiceDeleteAll() throws BusinessException {
        
        //construct the objects
        Long gid1 = 1L, gid2=2L, member1Id=1L, member2Id=2L;

        Group group1 = Group.builder().gid(gid1).groupName(group1Name).build();
       
        Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();

        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
    
        Group group2 = Group.builder().gid(gid2).groupName(group2Name).members(members).attr1(attr1).build();
       
        List<Group> originalGroups = new ArrayList<>();
        originalGroups.add(group1);
        originalGroups.add(group2);

        doReturn(originalGroups).when(groupRepository).findAll();;

        //mocked preconditions based on the constructed object
        doNothing().when(groupRepository).deleteAll();
        groupService.deleteGroup(group2);
        List<Group> actualModifiedGroups = groupService.findAll();
        assertTrue(actualModifiedGroups == null || actualModifiedGroups.size() == 0);
    }
}
