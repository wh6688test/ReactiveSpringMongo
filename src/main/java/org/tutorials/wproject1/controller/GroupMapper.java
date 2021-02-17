package org.tutorials.wproject1.controller;

import org.mapstruct.Mapper;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.model.GroupDTO;


import java.util.List;
import java.util.SortedSet;

@Mapper
public interface GroupMapper {

    GroupDTO toGroupDTO(Group group1);
    List<GroupDTO> toGroupDTOs(List<Group> groups1);
    Group toGroup(GroupDTO groupDTO);
    List<Group> toGroupList(List<GroupDTO> groupDTOs);
}
