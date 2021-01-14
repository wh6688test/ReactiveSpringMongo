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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.tutorials.wproject1.controller.GroupMapper;
import org.tutorials.wproject1.controller.Wproject1Controller;
import org.tutorials.wproject1.model.Groups;
import org.tutorials.wproject1.model.Member;
import org.tutorials.wproject1.service.GroupService;
import org.tutorials.wproject1.testing.JsonUtil;

//@ExtendWith(MockitoExtension.class)
@WebMvcTest(Wproject1Controller.class)
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

    private final String uriPrefix="/wrest";

    @Autowired
    private MockMvc mvc;
    
    @MockBean private GroupService groupService;

    @MockBean private GroupMapper groupMapper;
   
    @BeforeEach 
    public void setUp(){

    }
    @Test
    public void testControllerFindAll() throws Exception {
        //Generating the Entity Object
         final Long gid1=1L, gid2=2L, member1Id = 1L, member2Id = 2L;

         //GroupDTO groupDTO1 = GroupDTO.builder().gid(gid1).groupName(group1Name).build();
         Groups group1 = Groups.builder().gid(gid1).groupName(group1Name).build();
        
         //MemberDTO member1 = MemberDTO.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
         //MemberDTO member2 = MemberDTO.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
 
         Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
         Member member2 = Member.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
 
         //List<MemberDTO> members = new ArrayList<MemberDTO>();
         List<Member> members = new ArrayList<>();
         members.add(member1);
         members.add(member2);
         
        //GroupDTO groupDTO2 = GroupDTO.builder().gid(gid2).groupName(group2Name).members(members).attr1(attr1).attr2(attr2).build();
        Groups group2 = Groups.builder().gid(gid2).groupName(group2Name).members(members).attr1(attr1).attr2(attr2).build();
        List<Groups> groups = new ArrayList<>();
        groups.add(group2);
        //List<Groups> groups = Arrays.asList(group2);
         
        doReturn(groups).when(groupService).findAll();

        //controller verifications against service mock
        this.mvc.perform(get(uriPrefix+"/groups/")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].groupName", is(group2.getGroupName())))
        .andExpect(jsonPath("$[0].attr1", is(group2.getAttr1())))
        .andExpect(jsonPath("$[0].attr2", is(group2.getAttr2())))
        .andExpect(jsonPath("$[0].members", is(notNullValue())));
    }

    @Test
    public void testGroupControllerFindById() throws Exception {
         Long gid=1L;
         Groups groups = Groups.builder().gid(gid).groupName(group2Name).build();
        
         when(groupService.findGroupById(gid)).thenReturn(groups);
         this.mvc.perform(get(uriPrefix+"/group")
         .param("gid", String.valueOf(gid))
         .contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.groupName", is(groups.getGroupName())))
         .andExpect(jsonPath("$.gid", is(notNullValue())));
    }

    @Test
    public void testGroupControllerFindByGroupName() throws Exception {
        //construct the objects
        Long gid1=1L, gid2=2L;
    
        Groups group1 = Groups.builder().gid(gid1).groupName(group1Name).build();
        Groups group2 = Groups.builder().gid(gid2).groupName(group1Name).build();
        List<Groups>groups = spy(new ArrayList<Groups>());
        groups.add(group1);
        groups.add(group2);

        doReturn(groups).when(groupService).findGroupByName(anyString());

         //controller verifications against service mock
         this.mvc.perform(get(uriPrefix+"/group/"+group1Name+"/")
         .contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(2)))
         .andExpect(jsonPath("$[0].groupName", is(group1.getGroupName())))
         .andExpect(jsonPath("$[1].groupName", is(group2.getGroupName())));
     }
       

    @Test
    public void testGroupControllerFindByGroupMemberName() throws Exception {
        //construct the objects
        Long gid2=2L;
       
        Member member1 = Member.builder().name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().name(group2Member2Name).rating(group2Member2Rating).build();
        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
        Groups group2 = Groups.builder().gid(gid2).groupName(group2Name).members(members).build();
        List<Groups> groups = Arrays.asList(group2);
       
         //Mock precondition
         doReturn(groups).when(groupService).findGroupsByMemberName(anyString());

          //controller verifications against service mock
          this.mvc.perform(get(uriPrefix+"/group/member/name/"+group2Member1Name)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].members[0].name", is(group2Member1Name)));
      }
    
    @Test
    public void testGroupControllerFindByGroupMemberRating() throws Exception {
        
     //construct the objects
     Long gid2=2L;

    Member member1 = Member.builder().name(group2Member1Name).rating(group2Member1Rating).build();
    Member member2 = Member.builder().name(group2Member2Name).rating(group2Member1Rating).build();
    List<Member> members = new ArrayList<>();
    members.add(member1);
    members.add(member2);

    Groups group2 = Groups.builder().gid(gid2).groupName(group2Name).members(members).build();
    List<Groups>groups = new ArrayList<>();
    groups.add(group2);
   
    doReturn(groups).when(groupService).findGroupsByMemberRating(anyShort());

       //controller verifications against service mock
       this.mvc.perform(get(uriPrefix+"/group/member/rating/"+group2Member1Rating)
       .contentType(MediaType.APPLICATION_JSON))
       .andExpect(status().isOk())
       .andExpect(jsonPath("$", hasSize(1)))
       .andExpect(jsonPath("$[0].members[0].name", is(group2Member1Name)))
       .andExpect(jsonPath("$[0].members[0].rating").value(String.valueOf(group2Member1Rating)));
    }

    @Test
    public void testGroupControllerCreate() throws Exception {
        
         //construct the objects
         Long gid2=8L, member1Id=1L;
        
        Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        Groups groups = Groups.builder().gid(gid2).groupName(group2Name).attr1(attr1).attr2(attr2).members(members).attr1(attr1).build();
          spy(groups);
          spy(members);
          doReturn(groups).when(groupService).createGroup(groups);
          when(groupMapper.toGroups(any())).thenReturn(groups);
          when(groupMapper.toMember(any())).thenReturn(member1);

         //Controller verification against service mock
         this.mvc.perform(post(uriPrefix+"/group")
       .contentType(MediaType.APPLICATION_JSON)
       .accept(MediaType.APPLICATION_JSON)
       .content(JsonUtil.asJsonString(groups)))
       
       .andExpect(status().isCreated())
       .andExpect(jsonPath("$.gid").exists())
         .andExpect(jsonPath("$.groupName", is(group2Name)))
         .andExpect(jsonPath("$.attr1", is(attr1)))
         .andExpect(jsonPath("$.attr2", is(attr2)))
         .andExpect(jsonPath("$.members").exists())
         .andExpect(jsonPath("$.members[0].memberId").exists())
         .andExpect(jsonPath("$.members[0].name").value(group2Member1Name))
         .andExpect(jsonPath("$.members[0].rating").value(String.valueOf(group2Member1Rating)));
         
       //It will return isCreated even if the resource already exists;
        }


    @Test
    public void testGroupControllerUpdate() throws Exception {
        
         //construct the objects
         Long gid2=2L, member1Id=1L, member2Id=2L;
       
        Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
        List<Member> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);
        Groups groups = Groups.builder().gid(gid2).groupName(group2Name).members(members).attr1(attr1).attr2(attr2).build();
        doReturn(groups).when(groupService).findGroupById(anyLong());
        Member updatedMember = Member.builder().memberId(member1.getMemberId()).name(group2Member1Name).rating(updatedRating).build();
        members.remove(member1);
        members.add(updatedMember);
        Groups updatedGroup = Groups.builder().gid(gid2).groupName(updatedGroup2Name).members(members).attr1(updatedAttr1).attr2(attr2).build();
 
        spy(updatedGroup);
        spy(members);
        doReturn(updatedGroup).when(groupService).updateGroup(anyLong(), any());
        doReturn(updatedGroup).when(groupMapper).toGroups(any());
        doReturn(updatedMember).when(groupMapper).toMember(any());

         //Controller verification against service mock
         this.mvc.perform(put(uriPrefix+"/group/"+gid2)
         .contentType(MediaType.APPLICATION_JSON)
         .accept(MediaType.APPLICATION_JSON)
         .content(JsonUtil.asJsonString(updatedGroup)))
         .andExpect(status().isAccepted())
         .andExpect(jsonPath("$.gid").exists())
         .andExpect(jsonPath("$.groupName", is(updatedGroup2Name)))
         .andExpect(jsonPath("$.attr1", is(updatedAttr1)))
         .andExpect(jsonPath("$.attr2", is(attr2)))
         .andExpect(jsonPath("$.members").exists())
         .andExpect(jsonPath("$.members[0].memberId").exists())
         .andExpect(jsonPath("$.members[0].rating").exists())
         .andExpect(jsonPath("$.members[1].rating").value(String.valueOf(updatedRating)));
         
    }


    @Test
    public void testGroupControllerDelete() throws Exception {
        
        //construct the objects
        Long gid2=2L, member1Id=1L, member2Id=2L;
       
        Member member1 = Member.builder().memberId(member1Id).name(group2Member1Name).rating(group2Member1Rating).build();
        Member member2 = Member.builder().memberId(member2Id).name(group2Member2Name).rating(group2Member2Rating).build();
        List<Member> members = new ArrayList<Member>();
        members.add(member1);
        members.add(member2);
        Groups group2 = Groups.builder().gid(gid2).groupName(group2Name).members(members).attr1(attr1).build();

        doReturn(group2).when(groupService).findGroupById(group2.getGid());

        doNothing().when(groupService).deleteGroup(group2);
      
         //Controller verification against service mock
         this.mvc.perform(delete(uriPrefix+"/group/1"))
         .andExpect(status().isNoContent());
    }

}
