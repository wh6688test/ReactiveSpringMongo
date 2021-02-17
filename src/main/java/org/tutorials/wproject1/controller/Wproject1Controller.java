package org.tutorials.wproject1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerResponse.BodyBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.tutorials.wproject1.exception.BusinessException;
import org.tutorials.wproject1.exception.InvalidInputException;
import org.tutorials.wproject1.exception.ResourceAlreadyExistException;
import org.tutorials.wproject1.exception.ResourceNotFoundException;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.model.GroupDTO;

import java.util.ArrayList;

import javax.validation.Valid;
import javax.validation.constraints.Min;


@RestController
@Validated
@RequestMapping(value="/wrest")
public class Wproject1Controller {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private org.tutorials.wproject1.service.GroupService groupService;

    @ApiResponses(value= {
        @ApiResponse ( responseCode="200", description="find all groups",
                content={@Content(mediaType="application/json")}
        ),
        @ApiResponse ( responseCode="500", description="Business exception"),
})
    @Operation(summary="get all groups")
    @GetMapping(value="/groups", produces = {"application/json"})
    public Flux<Group> getGroups()  {
            return groupService.findAll()
                   .flatMap(Flux::just)
                   .onErrorResume(e -> {
                        if (e instanceof InvalidInputException) {
                           return Flux.error(new InvalidInputException());
                        }
                        return Flux.error(new BusinessException());
                    });
    } 

    //throws not found exception for non-existing gid
    @ApiResponses(value= {
            @ApiResponse ( responseCode="200", description="Found the group by id",
                    content= {@Content(mediaType = "application/json")
            }),
            @ApiResponse ( responseCode="400", description="invalid id supplied"),
            @ApiResponse ( responseCode="404", description="Group not found"),
            @ApiResponse ( responseCode="500", description="Business exception"),
    })


    @Operation(summary="Get the group by gid")
    @GetMapping(value="/group", produces = {"application/json"})
    public Mono<Group> getGroup(@RequestParam  @Min(1) String gid){

            return groupService.findGroupById(gid)
            .flatMap(Mono::just)
            .switchIfEmpty(Mono.empty())
            .onErrorResume(e -> {
                if (e instanceof InvalidInputException) {
                   return Mono.error(new InvalidInputException());
                }
                if (e instanceof ResourceNotFoundException) {
                    return Mono.error(new ResourceNotFoundException());
                }
                return Mono.error(new BusinessException());
            });
    }

    @ApiResponses(value= {
        @ApiResponse ( responseCode="201", description="Create the groups",
        content= {@Content(mediaType = "application/json")}
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request input"),
        @ApiResponse ( responseCode="500", description="Business exception"),     
})
    @PostMapping(path="/group", consumes={"application/json"}, produces={"application/json"})
    @ResponseStatus(value=HttpStatus.CREATED)
    @Operation(summary="create a group")
    public Mono<Group> createGroup(@Valid @RequestBody GroupDTO groupDTO) {
    
   Mono<Group>groupMono=groupService.findGroupById(groupDTO.getGid());
   //the exception is not returnning -- not sure how to properly chain this condition
   groupMono.flatMap( g-> Mono.error(new ResourceAlreadyExistException()));

   return  groupService.createGroup(groupMapper.toGroup(groupDTO))
                  .flatMap(Mono::just)
                  .onErrorResume(e -> {
                     if (e instanceof InvalidInputException) {
                         return Mono.error(new InvalidInputException());
                     }
                     return Mono.error(new BusinessException());
                  });
    }

    @ApiResponses(value= {
        @ApiResponse ( responseCode="204", description="delete group by gid"),
        @ApiResponse ( responseCode="400", description="invalid id supplied"),
        @ApiResponse ( responseCode="404", description="Group not found"),
        @ApiResponse ( responseCode="500", description="Business exception"),
    })
    @DeleteMapping(path="/group/{gid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary="Delete a group by its id")
    public Mono<Void> deleteGroup(@PathVariable String gid) {

            return groupService.findGroupById(gid)
                .flatMap(g -> groupService.deleteById(gid))
                .switchIfEmpty(Mono.empty())
                .onErrorResume(e -> {
                    if (e instanceof InvalidInputException) {
                       return Mono.error(new InvalidInputException());
                    }
                    if (e instanceof ResourceNotFoundException) {
                        return Mono.error(new ResourceNotFoundException());
                    }
                    return Mono.error(new BusinessException());
             });
    }

    @ApiResponses(value= {
        @ApiResponse ( responseCode="202", description="update the group",
                content={@Content(mediaType="application/json")}
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request input",
content = @Content),
        @ApiResponse ( responseCode="500", description="Business exception", content=@Content),     
})
    @PutMapping(value="/group/{gid}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary="Update a group by its id with a new group")
    public Mono<Group> updateGroup(@Valid @PathVariable String gid, @RequestBody GroupDTO groupDTO) throws InvalidInputException, ResourceNotFoundException, BusinessException {
       
            if (!gid.equals(groupDTO.getGid()))return Mono.error(new InvalidInputException("input gid is not matching"));
            return groupService.findGroupById(gid)
            .flatMap(g -> {
                Group updatedGroup = groupMapper.toGroup(groupDTO);
                updatedGroup.setGid(gid);
                return groupService.updateGroup(updatedGroup)
                       .flatMap(Mono::just);
            })
            .switchIfEmpty(Mono.empty())
            .onErrorResume(e -> {
            if (e instanceof InvalidInputException) {
               return Mono.error(new InvalidInputException());
            }
            if (e instanceof ResourceNotFoundException) {
                return Mono.error(new ResourceNotFoundException());
            }
            return Mono.error(new BusinessException());
     });
    }
}