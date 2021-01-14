package org.tutorials.wproject1.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import org.tutorials.wproject1.exception.BusinessException;
import org.tutorials.wproject1.exception.InvalidInputException;
import org.tutorials.wproject1.model.Groups;
import org.tutorials.wproject1.model.Member;
import org.tutorials.wproject1.repository.GroupsRepository;
import org.tutorials.wproject1.repository.MemberRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


//@RequiredArgsConstructor
@AllArgsConstructor
@Service
public class GroupService  {
    private static final String GROUP_ERROR="group";
    private static final String MEMBER_ERROR="member";
    private static final String NOT_FOUND_ERROR ="Exception.notFound";
    private static final String BUSINESS_ERROR ="Exception.server";
    private static final String INVALID_INPUT_ERROR ="Exception.invalidInput";

    @Autowired
    MessageSource messageSource;

    //@Autowired
    private final GroupsRepository groupsRepository;
    private final MemberRepository memberRepository;
    
    public List<Groups> findAll() throws BusinessException {
        List<Groups> groups;
        try {
           groups= groupsRepository.findAll();
        }catch (Exception e) {
           throw (new BusinessException(messageSource.getMessage(BUSINESS_ERROR, null, LocaleContextHolder.getLocale())));
        }
        if (groups.isEmpty()) {
            return new ArrayList<>();
        }
        return groups;
    }

    public Groups findGroupById(Long gid) throws InvalidInputException, ResourceNotFoundException, BusinessException {
        if (gid == null || gid <= 0) {
            throw new InvalidInputException(messageSource.getMessage(INVALID_INPUT_ERROR, new Object[]{GROUP_ERROR, gid}, LocaleContextHolder.getLocale()));
        }
        return groupsRepository.findGroupsByID(gid)
        .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, new Object[]{GROUP_ERROR, gid}, LocaleContextHolder.getLocale())));
    }

    public List<Groups> findGroupByName(String groupName) throws ResourceNotFoundException, BusinessException  {
        return groupsRepository.findGroupsByGroupName(groupName)
        .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, new Object[]{GROUP_ERROR, groupName}, LocaleContextHolder.getLocale())));
    }

	public List<Groups> findGroupsByMemberName(String memberName) throws ResourceNotFoundException, BusinessException {
        return groupsRepository.findGroupsByMemberName(memberName)
          .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, new Object[]{MEMBER_ERROR, memberName}, LocaleContextHolder.getLocale())));
    }

    public List<Groups> findGroupsByMemberRating(short rating) throws ResourceNotFoundException, BusinessException {
        return groupsRepository.findGroupsByMemberRating(rating)
           .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, new Object[]{GROUP_ERROR, "rating:"+rating}, LocaleContextHolder.getLocale())));
    }

    public Groups createGroup(Groups group) throws BusinessException{
        
        return groupsRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Groups group) throws BusinessException {
        groupsRepository.delete(group);
    }
    @Transactional
    public Groups deleteGroupMembers(long gid, long memberId) throws ResourceNotFoundException, BusinessException {
        Groups retrievedGroup=groupsRepository.findById(gid)
           .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, new Object[]{GROUP_ERROR, gid + " and "+memberId}, LocaleContextHolder.getLocale())));
    
        List<Member>retrievedMembers = retrievedGroup.getMembers();
        retrievedMembers.removeIf(a -> a.getMemberId() == memberId);
        retrievedGroup.setMembers(retrievedMembers);
        groupsRepository.save(retrievedGroup);
    
        return retrievedGroup;
    }

    public void deleteAll() {
        groupsRepository.deleteAll();
    }

    @Transactional
    public Groups updateGroup(long gid, Groups updates) throws ResourceNotFoundException, BusinessException {
        Groups retrievedGroup=groupsRepository.findById(gid)
           .orElseThrow(()->new ResourceNotFoundException(messageSource.getMessage(NOT_FOUND_ERROR, new Object[]{GROUP_ERROR, gid}, LocaleContextHolder.getLocale())));
        if (updates.getMembers() == null || !updates.getMembers().isEmpty()) {
             retrievedGroup.setMembers(updates.getMembers());
        }
        retrievedGroup.setGroupName(updates.getGroupName());
        retrievedGroup.setAttr1(updates.getAttr1());
        retrievedGroup.setAttr2(updates.getAttr2());
        groupsRepository.save(retrievedGroup);
    
        return retrievedGroup;
    }
    
}
