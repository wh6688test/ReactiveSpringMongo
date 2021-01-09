//@MockBean : service layer unit tests
package org.tutorials.wproject1.testing.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.tutorials.wproject1.controller.GroupMapper;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.model.GroupDTO;
import org.tutorials.wproject1.model.MemberDTO;
import org.tutorials.wproject1.service.GroupService;
import org.tutorials.wproject1.testing.JsonUtil;

@DataJpaTest
public class Wproject1ControllerTest {

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

    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private GroupService groupService;

    @MockBean GroupMapper groupMapper;

    @BeforeEach 
    public void setUp(){

    }
    @Test
    public void testControllerFindAll() throws Exception {
        //Generating the Entity Object
         final Long gid1=1L, gid2=2L, member1Id = 1L, member2Id = 2L;

         GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).build();
        
         MemberDTO member1 = MemberDTO.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
         MemberDTO member2 = MemberDTO.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
 
         List<MemberDTO> members = new ArrayList<MemberDTO>();
         members.add(member1);
         members.add(member2);
         
         GroupDTO groupDTO2 = GroupDTO.builder().gid(gid2).groupName(group2Name).members(members).attr1(attr1).attr2(attr2).build();
        
         List<GroupDTO> groupDTOs = Arrays.asList(groupDTO1);
         groupDTOs.add(groupDTO2);

        //Mock service level preconditions 
        //doReturn(new ArrayList<>()).when(groupService).findAll();
        //doReturn(new Group()).when(groupMapper).toGroupDTOs(any());
        doReturn(groupMapper.toGroupsList(groupDTOs)).when(groupService).findAll();

        //controller verifications against service mock
        this.mvc.perform(get("/groups/")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is(groupDTO1.getGroupName())))
        .andExpect(jsonPath("$[1].name", is(groupDTO2.getGroupName())))
        .andExpect(jsonPath("$[1].attributes", is(notNullValue())))
        .andExpect(jsonPath("$[1].members", is(notNullValue())));
    }

    @Test
    public void testGroupServiceFindById() throws Exception {
         Long gid=1L;
         //Generating the Entity Object
         GroupDTO groupDTO = GroupDTO.builder().gid(gid).groupName(group2Name).build();
        
         //Service Mock preconditions : doReturn is more flexible than when ... thenReturn...
         //doReturn(expectedGroup).when(groupRepository).findById(expectedGroup.getGid());
         //when(groupService.findGroupById(gid)).thenReturn(any(Group.class));
         //when(groupMapper.toGroupDTO(any(Group.class))).thenReturn(groupDTO);
         when(groupService.findGroupById(gid)).thenReturn(groupMapper.toGroup(groupDTO));

         //Controller verification against service mock
         this.mvc.perform(get("/group/")
         .param("gid", String.valueOf(gid))
         .contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.groupName", is(groupDTO.getGroupName())))
         .andExpect(jsonPath("$.gid", is(notNullValue())));
    }

    @Test
    public void testGroupServiceFindByGroupName() throws Exception {
        //construct the objects
        Long gid1=1L, gid2=2L;
        
        GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).build();
        GroupDTO groupDTO2 = GroupDTO.builder().gid(gid2).groupName(group1Name).build();
        List<GroupDTO>groupDTOs = Arrays.asList(groupDTO1);
        groupDTOs.add(groupDTO2);

        //Service Mock precondition
        //doReturn(new ArrayList<>()).when(groupService).findGroupByName(anyString());
        //doReturn(groupDTOs).when(groupMapper).toGroupDTOs(any());
        doReturn(groupMapper.toGroupsList(groupDTOs)).when(groupService).findGroupByName(anyString());

         //controller verifications against service mock
         this.mvc.perform(get("/group/"+group1Name+"/")
         .contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(2)))
         .andExpect(jsonPath("$[0].name", is(groupDTO1.getGroupName())))
         .andExpect(jsonPath("$[1].name", is(groupDTO2.getGroupName())));
     }
       

    @Test
    public void testGroupServiceFindByGroupMemberName() throws Exception {
        //construct the objects
        Long gid2=2L;

        MemberDTO memberDTO1 = MemberDTO.builder().name(group2Member1Name).rating(group2Member1Rating).build();
        MemberDTO memberDTO2 = MemberDTO.builder().name(group2Member2Name).rating(group2Member2Rating).build();

        List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
        memberDTOs.add(memberDTO1);
        memberDTOs.add(memberDTO2);
        GroupDTO groupDTO2 = GroupDTO.builder().gid(gid2).groupName(group2Name).members(memberDTOs).build();

        List<GroupDTO> groupDTOs = Arrays.asList(groupDTO2);

         //Mock precondition
         //doReturn(new ArrayList<>()).when(groupService).findGroupsByMemberName(anyString());
         //doReturn(groupDTOs).when(groupMapper).toGroupDTOs(any());
         doReturn(groupMapper.toGroupsList(groupDTOs)).when(groupService).findGroupsByMemberName(anyString());

          //controller verifications against service mock
          this.mvc.perform(get("/group/member"+group2Member1Name+"/")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].members[0].name", is(group2Member1Name)));
      }
    
    @Test
    public void testGroupServiceFindByMemberRating() throws Exception {
        
     //construct the objects
     Long gid2=2L;

     MemberDTO memberDTO1 = MemberDTO.builder().name(group2Member1Name).rating(group2Member1Rating).build();
     MemberDTO memberDTO2 = MemberDTO.builder().name(group2Member2Name).rating(group2Member1Rating).build();

     List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
     memberDTOs.add(memberDTO1);
     memberDTOs.add(memberDTO2);
     GroupDTO groupDTO2 = GroupDTO.builder().gid(gid2).groupName(group2Name).members(memberDTOs).build();

     List<GroupDTO> groupDTOs = Arrays.asList(groupDTO2);

      //Service level Mock precondition
      //doReturn(new ArrayList<>()).when(groupService).findGroupsByMemberRating(anyShort());
      //doReturn(groupDTOs).when(groupMapper).toGroupDTOs(any());
      doReturn(groupMapper.toGroupsList(groupDTOs)).when(groupService).findGroupsByMemberRating(anyShort());

       //controller verifications against service mock
       this.mvc.perform(get("/group/member"+group2Member1Rating+"/")
       .contentType(MediaType.APPLICATION_JSON))
       .andExpect(status().isOk())
       .andExpect(jsonPath("$", hasSize(2)))
       .andExpect(jsonPath("$[0].members[0].rating", is(group2Member1Rating)))
       .andExpect(jsonPath("$[0].members[1].rating", is(group2Member1Rating)));
    }

    @Test
    public void testGroupServiceCreate() throws Exception {
        
         //construct the objects
         Long gid2=2L, member1Id=1L, member2Id=2L;

         MemberDTO memberDTO1 = MemberDTO.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
         MemberDTO memberDTO2 = MemberDTO.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
 
         List<MemberDTO> membersDTOs = new ArrayList<MemberDTO>();
         membersDTOs.add(memberDTO1);
         membersDTOs.add(memberDTO2);
         
         GroupDTO groupDTO = GroupDTO.builder().gid(gid2).groupName(group2Name).attr1(attr1).attr2(attr2).members(membersDTOs).attr1(attr1).build();
        
        //Service Level Mock preconditions : doReturn is more flexible than when ... thenReturn...
        //doReturn(new Group()).when(groupService).createGroup(any(Group.class));
        //doReturn(groupDTO).when(groupMapper).toGroupDTO(any(Group.class));
        doReturn(groupMapper.toGroup(groupDTO)).when(groupService).createGroup(any(Group.class));

         //Controller verification against service mock
         this.mvc.perform(post("/group/")
         .param("gid", String.valueOf(gid2))
         .content(JsonUtil.asJsonString(groupDTO))
         .contentType(MediaType.APPLICATION_JSON)
         .accept(MediaType.APPLICATION_JSON))
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.gid").exists())
         .andExpect(jsonPath("$.attr1", is(attr1)))
         .andExpect(jsonPath("$.members").exists())
         .andExpect(jsonPath("$.members[0].memberId").exists())
         .andExpect(jsonPath("$.members[0].memberName", is(group2Member1Name)));
    }


    @Test
    public void testGroupServiceUpdate() throws Exception {
        
         //construct the objects
         Long gid2=2L, member1Id=1L, member2Id=2L;

         MemberDTO memberDTO1 = MemberDTO.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
         MemberDTO memberDTO2 = MemberDTO.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
 
         List<MemberDTO> membersDTOs = new ArrayList<MemberDTO>();
         membersDTOs.add(memberDTO1);
         membersDTOs.add(memberDTO2);
         
         
         GroupDTO groupDTO = GroupDTO.builder().gid(gid2).groupName(group2Name).members(membersDTOs).attr1(attr1).attr2(attr2).build();
         doReturn(groupMapper.toGroup(groupDTO)).when(groupService).findGroupById(anyLong());
         MemberDTO updatedMemberDTO = MemberDTO.builder().memberId(memberDTO1.getMemberId()).name(group2Member1Name).rating(updatedRating).build();
         membersDTOs.remove(memberDTO1);
         membersDTOs.add(updatedMemberDTO);
         GroupDTO updatedGroupDTO = GroupDTO.builder().gid(gid2).groupName(updatedGroup2Name).members(membersDTOs).attr1(updatedAttr1).attr2(attr2).build();
        
         
        //service level mock precondition
        //doReturn(new Group()).when(groupService).updateGroup(anyLong(), any(Group.class));
        //doReturn(new GroupDTO()).when(groupMapper).toGroupDTO(any(Group.class));
        doReturn(groupMapper.toGroup(updatedGroupDTO)).when(groupService).updateGroup(anyLong(), any(Group.class));

         //Controller verification against service mock
         this.mvc.perform(put("/group/1")
         .param("gid", String.valueOf(gid2))
         .content(JsonUtil.asJsonString(updatedGroupDTO))
         .contentType(MediaType.APPLICATION_JSON)
         .accept(MediaType.APPLICATION_JSON))
         .andExpect(status().isAccepted())
         .andExpect(jsonPath("$.gid").exists())
         .andExpect(jsonPath("$.groupName", is(updatedGroup2Name)))
         .andExpect(jsonPath("$.attr1", is(updatedAttr1)))
         .andExpect(jsonPath("$.attr2", is(attr2)))
         .andExpect(jsonPath("$.members").exists())
         .andExpect(jsonPath("$.members[0].memberId").exists())
         .andExpect(jsonPath("$.members[0].rating").exists())
         .andExpect(jsonPath("$.members[0].memberName", is(group2Member1Name)));
    }


    @Test
    public void testGroupServiceDelete() throws Exception {
        
        //construct the objects
        
        Long gid2=2L, member1Id=1L, member2Id=2L;
        
        MemberDTO member1DTO = MemberDTO.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        MemberDTO member2DTO = MemberDTO.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();

        List<MemberDTO> membersDTOs = new ArrayList<MemberDTO>();
        membersDTOs.add(member1DTO);
        membersDTOs.add(member2DTO);
        
        GroupDTO group2DTO = GroupDTO.builder().gid(gid2).groupName(group2Name).members(membersDTOs).attr1(attr1).build();

        doReturn(groupMapper.toGroup(group2DTO)).when(groupService).findGroupById(group2DTO.getGid());

        doNothing().when(groupService).deleteGroup(groupMapper.toGroup(group2DTO));

         //Controller verification against service mock
         this.mvc.perform(delete("/group/1"))
         .andExpect(status().isAccepted());
    }

}
