package org.tutorials.wproject1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.tutorials.wproject1.exception.ResourceNotFoundException;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.model.Member;
import org.tutorials.wproject1.repository.GroupRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GroupService  {
    private static final String NOT_FOUND_ERROR ="Exception.notFound";
    //@Autowired
    private final MessageSource messageSource;

    //@Autowired
    private final GroupRepository groupRepository;
    
    public List<Group> findAll()  {
        List<Group> groups= (List<Group>) groupRepository.findAll();
        if (groups.isEmpty()) {
            return new ArrayList<>();
        }
        return groups;
    }

    public Group findGroupById(long gid) throws ResourceNotFoundException {
        return groupRepository.findById(gid)
          .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, null, Locale.getDefault())));
    }

    public List<Group> findGroupByName(String groupName) throws ResourceNotFoundException  {
        return groupRepository.findGroupsByGroupName(groupName)
          .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, null, Locale.getDefault())));
    }

	public List<Group> findGroupsByMemberName(String memberName) throws ResourceNotFoundException {
        return groupRepository.findGroupsByMemberName(memberName)
           .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, null, Locale.getDefault())));
    }

    public List<Group> findGroupsByMemberRating(short rating) throws ResourceNotFoundException {
        return groupRepository.findGroupsByMemberRating(rating)
           .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, null, Locale.getDefault())));
    }

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }
    public void deleteGroup(Group group) {
        groupRepository.delete(group);
    }

    public Group deleteGroupMembers(long gid, long memberId) throws ResourceNotFoundException {
        Group retrievedGroup=groupRepository.findById(gid)
           .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, null, Locale.getDefault())));
    
        List<Member>retrievedMembers = retrievedGroup.getMembers();
        retrievedMembers.removeIf(a -> a.getMemberId() == memberId);
        retrievedGroup.setMembers(retrievedMembers);
        groupRepository.save(retrievedGroup);
    
        return retrievedGroup;
    }

    public void deleteAll() {
        groupRepository.deleteAll();
    }

    public Group updateGroup(long gid, Group updates) throws ResourceNotFoundException {
        Group retrievedGroup=groupRepository.findById(gid)
           .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, null, Locale.getDefault())));
    
        retrievedGroup.setMembers(updates.getMembers());
        retrievedGroup.setGroupName(updates.getGroupName());
        groupRepository.save(updates);
    
        return retrievedGroup;
    }
    
}
