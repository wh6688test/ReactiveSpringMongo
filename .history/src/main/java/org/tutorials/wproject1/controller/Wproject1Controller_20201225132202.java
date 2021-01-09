package org.tutorials.wproject1.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;


import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.model.Member;
import org.tutorials.wproject1.service.IGroupService;
import org.tutorials.wproject1.exception.ResourceNotFoundException;

import java.util.*;

@RestController
@RequestMapping(value="/wrest")
public class Wproject1Controller {

    private static final String notFoundError="Exception.notFound";
    private static final String notFoundError1="Exception.notFound1";
    
    //@Autowired
    private IGroupService groupService;

    @Autowired
    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    //@Autowired
    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    //@Autowired
    //private MessageSource messageSource;

    //throw not found exception for root access
    @GetMapping(value="/")
    public void rootIndex() {
       //throw new ResourceNotFoundException(messageSource.getMessage(NotAllowedError, null, Locale.US));
    }

    //returns empty list if no group exists
    @ApiResponses(value= {
            @ApiResponse ( responseCode="200", description="Found all the groups",
                    content={@Content(mediaType="application.json")}
            )
    })
    @GetMapping(value="/groups")
    @ResponseStatus(HttpStatus.OK)
    public List<Group> getGroups() {
            return groupService.findAll();
    }

    //throws not found exception for non-existing gid
    @ApiResponses(value= {
            @ApiResponse ( responseCode="200", description="Found the group by id",
                    content= {@Content(mediaType = "application.json", schema = @Schema(implementation = Group.class))
            }),

            @ApiResponse ( responseCode="400", description="invalid id supplied", content=@Content),

            @ApiResponse ( responseCode="404", description="Group not found", content=@Content),

    })
    @Operation(summary="Get the group by gid")
    @GetMapping(value="/group")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Group> getGroup(@RequestParam(value="gid") Long gid) {

        Optional<Group>group1=groupService.findGroup(gid);
        group1.orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(notFoundError1, new Object[]{"group : " + gid.toString()}, Locale.US)));
        return group1;
    }

    //returns null instead of NotFoundException
    @GetMapping(value="/group/attr")
    @ResponseStatus(HttpStatus.OK)
    public Group getGroupAttr(@RequestParam Long gid) {
        return groupService.findGroupAttr(gid);

    }

    //empty list if no member exists
    @GetMapping(value="/group/member/{memberId}", produces="application/json")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Group>> getGroupWithMemberId(@PathVariable String memberId) {

        Optional<List<Group>>groups=groupService.findMemberById(memberId);
        groups.orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(notFoundError1, new Object[]{"member : " + memberId}, Locale.US)));
        return groups;

    }

    @PostMapping(path="/group", consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Group createGroup(@RequestBody Map<String, String> groupAttr) {

        return groupService.createGroup(groupAttr);
    }

    //do nothing if the group does not exist
    @DeleteMapping(path="/group/{gid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable Long gid) {
        //Optional<Group>existingGroup=getGroup(gid);
        //groupService.deleteGroup(existingGroup.get());
        Optional<Group>returnedGroup=groupService.findGroup(gid);
        returnedGroup.orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(notFoundError1, new Object[]{"group : " + gid.toString()}, Locale.US)));
        groupService.deleteGroup(returnedGroup.get());
    }


    //do nothing if member does not exist
    @DeleteMapping(path="/group/member/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public List<Group> deleteMember(@PathVariable String memberId) {
            //Optional<Set<Group>>existingGroups=this.getGroupWithMemberId(memberId);
           // existingGroups.get().forEach(g->groupService.deleteMember(g, memberId));
        return groupService.deleteMember(memberId);
    }


    //do nothing if group does not exist
    @PutMapping(value="/group/{gid}/attr")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Group> updateGroupAttributes(@PathVariable Long gid, @RequestBody Map<String, String> attributeIn) {
        Optional<Group>updatedGroup=groupService.updateGroupAttribute(gid, attributeIn);
        updatedGroup.orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(notFoundError1, new Object[]{"group : " + gid.toString()}, Locale.US)));
        return updatedGroup;
    }
    //do nothing if group does not exist
    @PutMapping(value="/group/{gid}/member/rating")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Group> updateMemberRating(@PathVariable Long gid, @RequestBody Member  memberIn) {
        Optional<Group>updatedGroup=groupService.updateGroupMember(gid, memberIn);
        updatedGroup.orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(notFoundError1, new Object[]{"group : " + gid.toString()}, Locale.US)));
        return updatedGroup;
    }

}