package org.tutorials.wproject1.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.*;

//lombok annotation for convenience and builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="GROUPS")

@NamedEntityGraph(
  name = "Groups.members",
  attributeNodes = {
    @NamedAttributeNode("members"),
  }
)
public class Groups   {
    @NotNull
    @Id
    @Column(name="GROUP_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gid;

    @NotNull
    private String groupName;

    private String attr1;
    private String attr2;

    //@JsonIgnore
    //@OneToMany(mappedBy = "groups", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Member> members = new ArrayList<>();

}
