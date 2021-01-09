package org.tutorials.wproject1.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.*;

//lombok annotation for convenience and builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="GROUPS")
@NamedEntityGraph(
  name = "group-entity-graph",
  attributeNodes = {
    @NamedAttributeNode("groupName"),
    @NamedAttributeNode("members"),
  }
)
public class Group   {
    @NotNull
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gid;

    @NotNull
    private String groupName;

    private String attr1;
    private String attr2;

    @OneToMany(mappedBy = "group", fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Member> members;

    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

}
