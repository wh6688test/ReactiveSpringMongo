package org.tutorials.wproject1.model;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDTO  {

    private Long gid;

    private String groupName;

    private String attr1;
    private String attr2;
    
    private List<MemberDTO> members;
    /** 
    public GroupDTO(Long gid) {
        this.gid = gid;
    }
    public GroupDTO(Long gid, String groupName) {
        this.gid = gid;
        this.groupName=groupName;
    }
   
    public GroupDTO(Long gid, String groupName, String attr1, String attr2) {
        this.gid = gid;
        this.groupName = groupName;
        this.attr1 = attr1;
        this.attr2 = attr2;
    }**/
}
