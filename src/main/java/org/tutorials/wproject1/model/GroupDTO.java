package org.tutorials.wproject1.model;
import java.util.*;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
public class GroupDTO  {

    private Long gid;

    private String groupName;

    private String attr1;
    private String attr2;
    
    private List<MemberDTO> members;

    public GroupDTO(Long gid, String groupName, String attr1, String attr2, List<MemberDTO> members) {
        this.gid = gid;
        this.groupName = groupName;
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.members.addAll(members);
    }
    
}
