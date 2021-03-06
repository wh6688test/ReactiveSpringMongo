package org.tutorials.wproject1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Data;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO  {
    private String gid;
    private String groupName;
    private String attr1;
    private String attr2;
    private String memberName;
    private short rating;
}
