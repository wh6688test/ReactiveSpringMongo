package org.tutorials.wproject1.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.tutorials.wproject1.model.Attributes;
import org.tutorials.wproject1.model.Group;

import org.tutorials.wproject1.model.Member;
import org.tutorials.wproject1.repository.GroupRepository;


public interface IGroupService {

    Set<Group> findAll();

    Optional<Group>findGroupById(Long gid);

    Optional<Set<Attributes>> findGroupAttributes(Long gid);

    Optional<Set<Group>> findGroupsByMemberId(String memberId);
    Optional<Set<Group>> findGroupsByMemberRating(Short rating);

    Group createGroup(Group group);

    void deleteGroup(Group group);
    Optional<Group> deleteGroupAttributes(Long gid);
    Optional<Group> deleteGroupMembers(Long gid);
    void deleteAll();

    Optional<Group> updateGroupAttributes(Long gid, Attributes attr);

    Optional<Group> updateGroupMember(Long gid, Member memberIn);



}
