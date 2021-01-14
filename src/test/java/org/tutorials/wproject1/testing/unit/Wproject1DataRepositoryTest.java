//@DataJpatest : repository layer unit test
package org.tutorials.wproject1.testing.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.tutorials.wproject1.model.Groups;
import org.tutorials.wproject1.model.Member;
import org.tutorials.wproject1.repository.GroupsRepository;
import org.tutorials.wproject1.repository.MemberRepository;

@DataJpaTest
public class Wproject1DataRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private GroupsRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;

    private final String group1Name = "simpleGroup1";
    private final String group2Name = "testGroup2";
    private final String group2Member1Name = "member1";
    private final short group2Member1Rating = 2;
    private final String group2Member2Name = "member2";
    private final short group2Member2Rating = 3;

    private final String attr1 = "Programming";
    private final String attr2 = "Austin tx";

    private final String updatedGroup2Name = "testGroupU";  
    private final short updatedRating = 5;
    private final String updatedAttr1 = "updatedValue1";

    @BeforeEach 
    public void setUp(){
        //simple group1 without members and attributes : using entityManager to save to db
        Groups group1 = Groups.builder().groupName(group1Name).build();
        //groupRepository.save(group1);
        entityManager.persist(group1);
        //group with the same name : save to db using repository.save
        Groups group1b = Groups.builder().groupName(group2Name).build();
        groupRepository.save(group1b);

        //group 2 with both members and attributes : use repository save to persistent to db
        //members
        Member member1 = Member.builder().name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().name(group2Member2Name).rating(group2Member2Rating).build();

        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
      
       
        Groups group2 = Groups.builder().groupName(group2Name).attr1(attr1).attr2(attr2).members(members).build();
       
        memberRepository.save(member1);
        memberRepository.save(member2);
        groupRepository.save(group2);

    }
    @Test
    public void testGroupRepositoryFindAllNoMemberAttrs() {
        List<Groups> returnedGroups = (List<Groups>) groupRepository.findAll();
        long groupSize =  groupRepository.count();
        assertNotNull(returnedGroups);
        assertEquals(3, groupSize);
    }

    @Test
    public void testGroupRepositoryGetGroupByIdAndName() {
        
        Optional<List<Groups>> returnedGroups = groupRepository.findGroupsByGroupName(group2Name);
        assertNotNull(returnedGroups);
        assertTrue(returnedGroups.isPresent());
        assertEquals(2, returnedGroups.get().size());
        assertNotEquals(returnedGroups.get().get(0).getGid(),  returnedGroups.get().get(1).getGid());
        assertEquals(group2Name, returnedGroups.get().get(0).getGroupName());
        assertEquals(group2Name, returnedGroups.get().get(1).getGroupName());

        Long firstGroupId=returnedGroups.get().get(0).getGid();

        Optional<Groups>returnedGroupById = groupRepository.findById(firstGroupId);
        assertTrue(returnedGroupById.isPresent());
        assertEquals(returnedGroupById.get().getGroupName(), returnedGroups.get().get(0).getGroupName());
    }

    @Test
    public void testGroupRepositoryGetGroupsByGroupMemberName() {
        
        Optional<List<Groups>> returnedGroups = groupRepository.findGroupsByMemberName(group2Member1Name);
        assertTrue(returnedGroups.isPresent());
        assertEquals(1, returnedGroups.get().size());
        assertEquals(group2Name, returnedGroups.get().get(0).getGroupName());
        assertNotNull(returnedGroups.get().get(0).getGid());
        List<Member>members = returnedGroups.get().get(0).getMembers();
        assertNotNull(members);
        assertEquals(2, members.size());
        Optional<Member> member1= members.stream().findFirst();
        assertTrue(member1.isPresent());
        assertEquals(group2Member1Name, member1.get().getName());
        assertEquals(group2Member1Rating, member1.get().getRating());
       
    }
    
    @Test
    public void testGroupRepositorySaveAndGetGroupsByRating() {
        Optional<List<Groups>> returnedGroups = groupRepository.findGroupsByMemberRating(group2Member1Rating);
        assertTrue(returnedGroups.isPresent());
        assertEquals(1, returnedGroups.get().size());
        assertEquals(group2Name, returnedGroups.get().get(0).getGroupName());

        List<Member>members = returnedGroups.get().get(0).getMembers();
        assertNotNull(members);
        assertEquals(2, members.size());
        Optional<Member> member1= members.stream().findFirst();
        assertTrue(member1.isPresent());
        assertEquals(group2Member1Name, member1.get().getName());
        assertEquals(group2Member1Rating, member1.get().getRating());
    }

    @Test
    public void testGroupRepositoryUpdateGroup() {

        Optional<List<Groups>>returnedGroups = groupRepository.findGroupsByMemberName(group2Member1Name);
        assertTrue(returnedGroups.isPresent());
        assertEquals(1, returnedGroups.get().size());
        Groups returnedGroup = returnedGroups.get().get(0);
        assertNotNull(returnedGroup);

        List<Member>members = returnedGroups.get().get(0).getMembers();
        assertNotNull(members);
        Optional<Member> member1= members.stream().findFirst();
        assertTrue(member1.isPresent());

        Long groupId = returnedGroup.getGid();
        Long memberId = member1.get().getMemberId();

        //updates
        returnedGroup.setGroupName(updatedGroup2Name);
        returnedGroup.setAttr1(updatedAttr1);
        members.remove(member1.get());
        members.add(Member.builder().memberId(member1.get().getMemberId()).name(group2Member1Name).rating(updatedRating).build());
        returnedGroup.setMembers(members);
        groupRepository.save(returnedGroup);

        assertNotNull(groupId);
        assertNotNull(memberId);
        //retrieve the updated group
        Optional<Groups> returnedUpdatedGroup = groupRepository.findById(groupId);
        assertTrue(returnedUpdatedGroup.isPresent());
        Groups updatedGroup = returnedUpdatedGroup.get();
        assertEquals(updatedGroup2Name, updatedGroup.getGroupName());
        assertEquals(updatedAttr1, updatedGroup.getAttr1());
        assertEquals(attr2, updatedGroup.getAttr2());
        List<Member> updatedMembers = updatedGroup.getMembers();
        assertNotNull(updatedMembers);
        assertEquals(2, updatedMembers.size());
        //Optional<Member> updatedMember1= 
        
        List<Member> updatedMember = updatedMembers.stream().filter(m -> 
                m.getName() == group2Member1Name
        ).collect(Collectors.toList());
        assertEquals(1, updatedMember.size());
        assertEquals(updatedRating,updatedMember.get(0).getRating());
    }


    @Test
    public void testGroupRepositoryDelete() {

        List<Groups> returnedGroups = (List<Groups>) groupRepository.findAll();
        assertNotNull(returnedGroups);
        assertEquals(3, returnedGroups.size());
       
        Optional<List<Groups>>group2 = groupRepository.findGroupsByMemberName(group2Member1Name);
        assertTrue(group2.isPresent());
        assertNotNull(group2.get().get(0));
        assertEquals(1, group2.get().size());
        groupRepository.delete(group2.get().get(0));

        returnedGroups = (List<Groups>)groupRepository.findAll();
        assertNotNull(returnedGroups);
        assertEquals(2, returnedGroups.size());

        Optional<List<Groups>> group1 =  groupRepository.findGroupsByGroupName(group1Name);
        assertTrue(group1.isPresent());
        assertNotNull(group1.get().get(0));
        assertEquals(1, group1.get().size());
        Optional<List<Groups>> updatedGroups =  groupRepository.findGroupsByGroupName(group2Name);
        assertTrue(updatedGroups.isPresent());
        assertNotNull(updatedGroups.get().get(0));
        assertEquals(1, updatedGroups.get().size());
        Groups g1 = group1.get().get(0);
        Groups g2 = updatedGroups.get().get(0);
        groupRepository.delete(g1);
        groupRepository.delete(g2);

        returnedGroups = (List<Groups>)groupRepository.findAll();
        assertTrue((returnedGroups == null || returnedGroups.size() == 0));
    }

}
