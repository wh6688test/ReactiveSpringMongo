package org.tutorials.wproject1.controller;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tutorials.wproject1.model.Groups;
import org.tutorials.wproject1.model.GroupDTO;
import org.tutorials.wproject1.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.tutorials.wproject1.exception.BusinessException;
import org.tutorials.wproject1.exception.InvalidInputException;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RequiredArgsConstructor

@RestController
@Validated
@RequestMapping(value="/wrest")
public class Wproject1Controller {

    private static final String ERROR_GROUP = "group";
    private static final String ERROR_MEMBER = "member";
    private static final String ERROR_ATTRIBUTES = "attributes";

    private static final String CONTENT_TYPE="application/json";

    private static final String BUSINESS_ERROR = "Exception.server";
    private static final String NOT_FOUND_ERROR ="Exception.notFound";
    private static final String INVALID_INPUT_ERROR ="Exception.invalidInput";

    private final GroupService groupService;
    private final GroupMapper groupMapper;

    //@Autowired
    private final MessageSource messageSource;

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader(HttpHeaders.ACCEPT, CONTENT_TYPE);
    }
    
    //returns empty list if no group exists
    @ApiResponses(value= {
            @ApiResponse ( responseCode="200", description="Found all the groups",
                    content={@Content(mediaType="application.json")}
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request input", 
    content = @Content), 
            @ApiResponse ( responseCode="404", description="Group not found", content=@Content),
   
    })

    @Operation(summary="Get all groups")
    @GetMapping("/groups")
    public List<Groups> getGroups() throws BusinessException {
         
            return groupService.findAll(); 
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
    public Groups getGroup(@RequestParam  @Min(1) Long gid) throws InvalidInputException, BusinessException  {
       
        if(null==gid || gid <=0 ) { 
                throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_GROUP}, LocaleContextHolder.getLocale()));
        }
        try {
            return groupService.findGroupById(gid);
           
        } catch (ResourceNotFoundException e) {
                return null;
        }
         catch (Exception e) {
             //only business exception is thrown to end user
             throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, LocaleContextHolder.getLocale()));
         }
    }

    @Operation(summary="Get the group by group name")
    @GetMapping("/group/{groupName}")
    //@ResponseStatus(HttpStatus.OK)
    public List<Groups> getGroupByName(@PathVariable String groupName) throws InvalidInputException, BusinessException  {
        List<Groups> retrievedGroups=new ArrayList<>();
        if(null==groupName) { 
                throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_GROUP, groupName}, LocaleContextHolder.getLocale()));
        } 
        try {
           return groupService.findGroupByName(groupName);
        } catch (ResourceNotFoundException e) {
            return retrievedGroups;
        }
        catch (Exception e) {
            //only business exception is thrown to end user
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, LocaleContextHolder.getLocale()));
        }
    }

    //empty list if no member exists
    @GetMapping(value="/group/member/name/{memberName}", produces="application/json")
    //@ResponseStatus(HttpStatus.OK)
    @Operation(summary="Get group by member name")
    public List<Groups> getGroupWithMemberName(@PathVariable String memberName) throws InvalidInputException, BusinessException {
        List<Groups>result = new ArrayList<>();
        if(null==memberName || memberName.length() == 0) { 
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_MEMBER, memberName}, LocaleContextHolder.getLocale()));
        }
        try {
            return groupService.findGroupsByMemberName(memberName);
        } catch (ResourceNotFoundException e) {
           return result;
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, LocaleContextHolder.getLocale()));
        }
    }

    //empty list if no member exists
    @GetMapping(value="/group/member/rating/{rating}", produces="application/json")
    //@ResponseStatus(HttpStatus.OK)
    @Operation(summary="Get group by member rating")
    public List<Groups> getGroupWithMemberRating(@PathVariable short rating) throws InvalidInputException, BusinessException {
        List<Groups>result = new ArrayList<>();
        if(rating > 6 || rating < 1) { 
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_MEMBER, rating}, LocaleContextHolder.getLocale()));
        }
        try {
         return groupService.findGroupsByMemberRating(rating);
        } catch (ResourceNotFoundException e) {
            return result;
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, LocaleContextHolder.getLocale()));
        }

    }

    @PostMapping(path="/group", consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary="create a group")
    public Groups createGroup(@Valid @RequestBody GroupDTO groupDTO) throws BusinessException,InvalidInputException {
        /** 
        if (groupService.findGroupById(groupDTO.getGid()).isPresent()) {

        }
        **/
        //It will return HttpStatus.CREATED even if the resource already exists
        return groupService.createGroup(groupMapper.toGroups(groupDTO));
        
        /** 
        if (groupDTO.getMembers() == null) {
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_GROUP}, LocaleContextHolder.getLocale()));
        }**/
        /** 
        try {
          return ResponseEntity.status(HttpStatus.CREATED).body(groupDTO);
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, LocaleContextHolder.getLocale()));
        }**/
    }

    //do nothing if the group does not exist
    @DeleteMapping(path="/group/{gid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary="Delete a group by its id")
    public void deleteGroup(@PathVariable Long gid) throws InvalidInputException, ResourceNotFoundException, BusinessException{
       
        if(null==gid || gid.equals(0L)) { 
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_GROUP, gid}, LocaleContextHolder.getLocale()));
        }
        try {
                Groups retrievedGroup = groupService.findGroupById(gid);
                groupService.deleteGroup(retrievedGroup);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, new Object[]{ERROR_GROUP}, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, LocaleContextHolder.getLocale()));
        }
    }

    //do nothing if group does not exist
    @PutMapping(value="/group/{gid}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary="Update a group by its id with a new group")
    public Groups updateGroup(@PathVariable Long gid, @Valid @RequestBody GroupDTO groupDTO) throws InvalidInputException, ResourceNotFoundException, BusinessException {

        if(null==gid || gid.equals(0L)) { 
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_GROUP, gid}, LocaleContextHolder.getLocale()));
        }
        if(null==groupDTO) { 
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{ERROR_ATTRIBUTES, groupDTO}, LocaleContextHolder.getLocale()));
        }
        try {
           return groupService.updateGroup(gid, groupMapper.toGroups(groupDTO));
        }  catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, new Object[]{ERROR_GROUP}, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            throw new BusinessException(messageSource.getMessage(BUSINESS_ERROR, new Object[]{ERROR_GROUP}, LocaleContextHolder.getLocale()));
        }
    }
}