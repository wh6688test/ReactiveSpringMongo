package org.tutorials.wproject1.controller;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.model.GroupDTO;
import org.tutorials.wproject1.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.tutorials.wproject1.exception.BusinessException;
import org.tutorials.wproject1.exception.InvalidInputException;
import org.tutorials.wproject1.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor

@RestController
@RequestMapping(value="/wrest")
public class Wproject1Controller {

    private static final String INVALID_INPUT_ERROR = "Exception.invalidInput";
    private static final String ERROR_GROUP = "group";
    private static final String ERROR_MEMBER = "member";
    private static final String ERROR_ATTRIBUTES = "attributes";

    private static final String CONTENT_TYPE="application/json";

    private static final String BUSINESS_ERROR = "Exception.server";
    private static final String NOT_FOUND_ERROR ="Exception.server";
    
    private final GroupService groupService;
    
    private final MessageSource messageSource;
    
    private final GroupMapper groupMapper;

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader(HttpHeaders.ACCEPT, CONTENT_TYPE);
    }

    //returns empty list if no group exists
    @ApiResponses(value= {
            @ApiResponse ( responseCode="200", description="Found all the groups",
                    content={@Content(mediaType="application.json")}
            ),
            @ApiResponse ( responseCode="404", description="Group not found", content=@Content),
   
    })
    @GetMapping("/groups")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GroupDTO>> getGroups() throws BusinessException {
        try {
            return ResponseEntity.ok()
               .body(groupMapper.toGroupDTOs(groupService.findAll()));
        } catch (Exception e) {
               throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, Locale.getDefault()));
       }
    } 

    //throws not found exception for non-existing gid
    @ApiResponses(value= {
            @ApiResponse ( responseCode="200", description="Found the group by id",
                    content= {@Content(mediaType = "application.json")
            }),

            @ApiResponse ( responseCode="400", description="invalid id supplied", content=@Content),

            @ApiResponse ( responseCode="404", description="Group not found", content=@Content),
    })


    @Operation(summary="Get the group by gid")
    @GetMapping("/group")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GroupDTO> getGroup(@RequestParam  Long gid) throws InvalidInputException, BusinessException  {
       
        if(null==gid || gid.equals(0L)) { 
                throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_GROUP}, Locale.getDefault()));
        }
        try {
           return ResponseEntity.ok().body(groupMapper.toGroupDTO(groupService.findGroupById(gid)));
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, Locale.getDefault()));
        }
    }

    @Operation(summary="Get the group by gid")
    @GetMapping("/group/{groupName}")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GroupDTO>> getGroupByName(@PathVariable String groupName) throws InvalidInputException, BusinessException  {

        if(null==groupName) { 
                throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_GROUP, groupName}, Locale.getDefault()));
        } 
        try {
           return ResponseEntity.ok().body(groupMapper.toGroupDTOs(groupService.findGroupByName(groupName)));
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, Locale.getDefault()));
        }
    }

    //empty list if no member exists
    @GetMapping(value="/group/member/{memberName}", produces="application/json")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GroupDTO>> getGroupWithMemberName(@PathVariable String memberName) throws InvalidInputException, BusinessException {

        if(null==memberName || memberName.length() == 0) { 
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_MEMBER, memberName}, Locale.getDefault()));
        }
        try {
            return ResponseEntity.ok().body(groupMapper.toGroupDTOs(groupService.findGroupsByMemberName(memberName)));
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, Locale.getDefault()));
        }

    }

    //empty list if no member exists
    @GetMapping(value="/group/member/{rating}", produces="application/json")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GroupDTO>> getGroupWithMemberRating(@PathVariable short rating) throws InvalidInputException, BusinessException {

        if(rating > 6 || rating < 1) { 
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_MEMBER, rating}, Locale.getDefault()));
        }
        try {
         return ResponseEntity.ok().body(groupMapper.toGroupDTOs(groupService.findGroupsByMemberRating(rating)));
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, Locale.getDefault()));
        }

    }

    @PostMapping(path="/group", consumes="application/json")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) throws BusinessException {
        groupService.createGroup(groupMapper.toGroup(groupDTO));
        try {
          return ResponseEntity.status(HttpStatus.CREATED).body(groupDTO);
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, Locale.getDefault()));
        }
    }

    //do nothing if the group does not exist
    @DeleteMapping(path="/group/{gid}")
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteGroup(@PathVariable Long gid) throws InvalidInputException, ResourceNotFoundException, BusinessException{
       
        if(null==gid || gid.equals(0L)) { 
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_GROUP, gid}, Locale.getDefault()));
        }
        try {
                Group retrievedGroup = groupService.findGroupById(gid);
                groupService.deleteGroup(retrievedGroup);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, new Object[]{ERROR_GROUP}, Locale.getDefault()));
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, Locale.getDefault()));
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    //do nothing if group does not exist
    @PutMapping(value="/group/{gid}")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable Long gid, @RequestBody GroupDTO groupDTO) throws InvalidInputException, ResourceNotFoundException, BusinessException {

        if(null==gid || gid.equals(0L)) { 
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_GROUP, gid}, Locale.getDefault()));
        }
        if(null==groupDTO) { 
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_ATTRIBUTES, groupDTO}, Locale.getDefault()));
        }
        try {
           return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(groupMapper.toGroupDTO(groupService.updateGroup(gid, groupMapper.toGroup(groupDTO))));
        }  catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, new Object[]{ERROR_GROUP}, Locale.getDefault()));
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, Locale.getDefault()));
        }
    }
}