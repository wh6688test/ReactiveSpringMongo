package org.tutorials.wproject1.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.tutorials.wproject1.model.Attributes;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.model.Member;
import org.tutorials.wproject1.repository.GroupRepository;
import org.tutorials.wproject1.repository.AttributesRepository;
import org.tutorials.wproject1.repository.MemberRepository;

@Service
public class GroupService implements IGroupService {

    @Autowired
    private GroupRepository groupRepository;
    

    @Override
    public Set<Group> findAll() {
        return (Set<Group>) groupRepository.findAll();
    }

    @Override
    public Optional<Group> findGroupById(Long gid) {
        return groupRepository.findById(gid);
    }

    @Override
    public Optional<Set<Attributes>> findGroupAttributes(Long gid) {

        Optional<Group>group=groupRepository.findById(gid);

        return group.map(g->g.getAttributes());
    }

    public Optional<Set<Group>> findGroupsByMemberId(String memberId) {

        return groupRepository.findGroupsByMemberId(memberId);
    }

    @Override
    public Optional<Set<Group>> findGroupsByMemberRating(Short rating) {
        
        return groupRepository.findGroupsByMemberRating(rating);
    }

    @Override
    public Group createGroup(Group group) {
   
        return groupRepository.save(group);
    }

    @Override
    public void deleteGroup(Group group) {
        groupRepository.delete(group);
    }

    @Override
    public Optional<Group> deleteGroupAttributes(Long gid)  {
        Optional<Group>retrievedGroup=groupRepository.findById(gid);
        if (retrievedGroup.isPresent()) {
           retrievedGroup.get().setAttributes(new HashSet<>());
           groupRepository.save(retrievedGroup.get());
        }

        return retrievedGroup;

    }

    @Override
    public Optional<Group> deleteGroupMembers(Long gid) {
       Optional<Group>retrievedGroup=groupRepository.findById(gid);
        if (retrievedGroup.isPresent()) {
           retrievedGroup.get().setMembers(new HashSet<>());
           groupRepository.save(retrievedGroup.get());
        }

        return retrievedGroup;
    }

    @Override
    public void deleteAll() {
        groupRepository.deleteAll();
    }

    

    @Override
    public Optional<Group> updateGroupAttributes(Long gid, Attributes attribute) {
       Optional<Group>retrievedGroup=groupRepository.findById(gid);
        if (retrievedGroup.isPresent()) {
            Set<Attributes> retrievedAttributes=retrievedGroup.get().getAttributes();
            Set<Attributes>updatedAttributes=retrievedAttributes.stream().map(k->{
               if (k.getKey().equals(attribute.getKey())) {
                   return attribute;
               } else {
                   return k;
               }
            }).collect(Collectors.toSet());
            retrievedGroup.get().setAttributes(updatedAttributes);
            groupRepository.save(retrievedGroup.get());
        } 
        return retrievedGroup;
    }

    @Override
    public Optional<Group> updateGroupMember(Long gid, Member memberIn) {
      Optional<Group>retrievedGroup=groupRepository.findById(gid);
        if (retrievedGroup.isPresent()) {
             Set<Member> retrievedMembers=retrievedGroup.get().getMembers();
            Set<Member>updatedMembers=retrievedMembers.stream().map(e->{
               if (e.getId()==memberIn.getId()) {
                   return memberIn;
               } else {
                   return e;
               }
            }).collect(Collectors.toSet());
            retrievedGroup.get().setMembers(updatedMembers);
            groupRepository.save(retrievedGroup.get());
        } 
        return retrievedGroup;
    }
}
