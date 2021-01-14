//@MockBean : service layer unit tests
package org.tutorials.wproject1.testing.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tutorials.wproject1.model.Groups;
import org.tutorials.wproject1.model.Member;
import org.tutorials.wproject1.repository.GroupsRepository;
import org.tutorials.wproject1.repository.MemberRepository;
import org.tutorials.wproject1.service.GroupService;

//tested the least : the least value added
@ExtendWith(MockitoExtension.class)
public class Wproject1ServiceTest {

    @Mock
    GroupsRepository groupRepository;
    @Mock
    MemberRepository memberRepository;
    
    @InjectMocks
    private GroupService groupService;
    
    @Spy
    Groups mockGroup;
    
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
         
        Groups group2 = Groups.builder().gid(gid1).groupName(group2Name).members(members).attr1(attr1).attr2(attr2).build();
        
        List<Groups> expectedGroups = Arrays.asList(group2);

        //Mock preconditions 
        doReturn(expectedGroups).when(groupRepository).findAll();

        //Service verification against Repository Mock
        List<Groups> actualGroups = groupService.findAll();
        assertNotNull(actualGroups); 
        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGroupServiceFindByGroupName() throws Exception {
        //construct the objects
        Long gid1=1L, gid2=2L;
        
        Groups group1 = Groups.builder().gid(gid1).groupName(group1Name).build();
        Groups group2 = Groups.builder().gid(gid2).groupName(group2Name).build();
        List<Groups>expectedGroups = Arrays.asList(group1);
        //expectedGroups.add(group2);

        //Mock precondition
        when(groupRepository.findGroupsByGroupName(group1Name)).thenReturn(Optional.of(expectedGroups));
        
        //Service verification against Repository Mock
        List<Groups> actualGroups = groupService.findGroupByName(group1Name);
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
        Groups group2 = Groups.builder().gid(gid2).groupName(group2Name).members(members).build();

        List<Groups> expectedGroups = Arrays.asList(group2);
        //mocked preconditions based on the constructed object
        doReturn(Optional.of(expectedGroups)).when(groupRepository).findGroupsByMemberName(group2Member1Name);
        //Service verification against Repository Mock
        List<Groups> actualGroups = groupService.findGroupsByMemberName(group2Member1Name);
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
        Groups group2 = Groups.builder().gid(gid2).groupName(group2Name).members(members).build();

        List<Groups> expectedGroups = Arrays.asList(group2);
        //mocked preconditions based on the constructed object
        doReturn(Optional.of(expectedGroups)).when(groupRepository).findGroupsByMemberRating(group2Member1Rating);
        //Service verification against Repository Mock
        List<Groups> actualGroups = groupService.findGroupsByMemberRating(group2Member1Rating);
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
         
         Groups group = Groups.builder().gid(gid2).groupName(group2Name).attr1(attr1).attr2(attr2).members(members).build();
         Groups expectedGroup = group;
         //mocked preconditions based on the constructed object
         doReturn(expectedGroup).when(groupRepository).save(group);

         //Service verification against Repository Mock
         Groups actualGroup = groupService.createGroup(group);
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
         
         Groups group2 = Groups.builder().gid(gid2).groupName(group2Name).attr1(attr1).attr2(attr2).members(members).build();
        
         //mocked preconditions based on the constructed object
         doReturn(Optional.of(group2)).when(groupRepository).findById(gid2);

         //updates
         List<Member> updatedMembers = new ArrayList<Member>();
         
         Member updatedMember = Member.builder().memberId(member1Id).name(updatedMemberName).rating(updatedRating).build();
         updatedMembers.add(updatedMember);
    
         Groups expectedUpdatedGroup = Groups.builder().gid(gid2).groupName(updatedGroup2Name).attr1(updatedAttr1).attr2(attr2).members(updatedMembers).build();
         
         //Service verification against Repository Mock
         Groups actualUpdatedGroup = groupService.updateGroup(gid2, expectedUpdatedGroup);

         assertNotNull(actualUpdatedGroup); 
         assertEquals(expectedUpdatedGroup, actualUpdatedGroup);
    }
}
