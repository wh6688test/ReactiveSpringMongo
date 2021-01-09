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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.model.Member;
import org.tutorials.wproject1.repository.GroupRepository;
import org.tutorials.wproject1.repository.MemberRepository;

@DataJpaTest
public class Wproject1DataRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private GroupRepository groupRepository;
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
        Group group1 = Group.builder().groupName(group1Name).build();
        entityManager.persist(group1);
        //group with the same name : save to db using repository.save
        Group group1b = Group.builder().groupName(group1Name).build();
        groupRepository.save(group1b);

        //group 2 with both members and attributes : use repository save to persistent to db
        //members
        Member member1 = Member.builder().name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().name(group2Member2Name).rating(group2Member2Rating).build();

        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
      
       
        Group group2 = Group.builder().groupName(group2Name).attr1(attr1).attr2(attr2).members(members).build();
       
        memberRepository.save(member1);
        memberRepository.save(member2);
        groupRepository.save(group2);

    }
    @Test
    public void testGroupRepositoryFindAllNoMemberAttrs() {
        List<Group> returnedGroups = (List<Group>) groupRepository.findAll();
        long groupSize =  groupRepository.count();
        assertNotNull(returnedGroups);
        assertEquals(3, groupSize);
    }

    @Test
    public void testGroupRepositoryGetGroupByIdAndName() {
        
        Optional<List<Group>> returnedGroups = groupRepository.findGroupsByGroupName(group1Name);
        assertNotNull(returnedGroups);
        assertTrue(returnedGroups.isPresent());
        assertEquals(2, returnedGroups.get().size());
        assertNotEquals(returnedGroups.get().get(0).getGid(),  returnedGroups.get().get(1).getGid());
        assertEquals(group2Name, returnedGroups.get().get(0).getGroupName());
        assertEquals(group2Name, returnedGroups.get().get(1).getGroupName());

        Long firstGroupId=returnedGroups.get().get(0).getGid();

        Optional<Group>returnedGroupById = groupRepository.findById(firstGroupId);
        assertTrue(returnedGroupById.isPresent());
        assertEquals(returnedGroupById.get().getGroupName(), returnedGroups.get().get(0).getGroupName());
    }

    @Test
    public void testGroupRepositoryGetGroupsByGroupMemberName() {
        
        Optional<List<Group>> returnedGroups = groupRepository.findGroupsByMemberName(group2Member1Name);
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
        Optional<List<Group>> returnedGroups = groupRepository.findGroupsByMemberRating(group2Member1Rating);
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

        Optional<List<Group>>returnedGroups = groupRepository.findGroupsByGroupName(group2Name);
        assertTrue(returnedGroups.isPresent());
        assertEquals(1, returnedGroups.get().size());
        Group returnedGroup = returnedGroups.get().get(0);
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
        Optional<Group> returnedUpdatedGroup = groupRepository.findById(groupId);
        assertTrue(returnedUpdatedGroup.isPresent());
        Group updatedGroup = returnedUpdatedGroup.get();
        assertEquals(updatedGroup2Name, updatedGroup.getGroupName());
        assertEquals(updatedAttr1, updatedGroup.getAttr1());
        assertEquals(attr2, updatedGroup.getAttr1());
        List<Member> updatedMembers = updatedGroup.getMembers();
        assertNotNull(updatedMembers);
        assertEquals(2, updatedMembers.size());
        Optional<Member> updatedMember1= members.stream().findFirst();
        assertTrue(updatedMember1.isPresent());
        assertEquals(group2Member1Name, updatedMember1.get().getName());
        assertEquals(updatedRating,updatedMember1.get().getRating());
    }


    @Test
    public void testGroupRepositoryDelete() {

        List<Group> returnedGroups = (List<Group>) groupRepository.findAll();
        assertNull(returnedGroups);
        assertEquals(3, returnedGroups.size());
       
        Optional<List<Group>> group2 =  groupRepository.findGroupsByGroupName(group2Name);
        assertTrue(group2.isPresent());
        assertNotNull(group2.get().get(0));
        assertEquals(1, group2.get().size());
        groupRepository.delete(group2.get().get(0));

        returnedGroups = (List<Group>)groupRepository.findAll();
        assertNull(returnedGroups);
        assertEquals(2, returnedGroups.size());

        Optional<List<Group>> group1 =  groupRepository.findGroupsByGroupName(group1Name);
        assertTrue(group1.isPresent());
        assertNotNull(group1.get().get(0));
        assertEquals(2, group1.get().size());
        Group g1 = group1.get().get(0);
        Group g2 = group1.get().get(1);
        groupRepository.delete(g1);
        groupRepository.delete(g2);

        returnedGroups = (List<Group>)groupRepository.findAll();
        assertTrue((returnedGroups == null || returnedGroups.size() == 0));
    }

}
