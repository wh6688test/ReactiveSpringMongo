package org.tutorials.wproject1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="GROUPS")

public class Group implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long gid;

    //mapping to simple data type
    /**
    @ElementCollection
    @OneToMany
    @MapKeyColumn(name="key")
    @Column(name="value")
    @CollectionTable(name="group_attributes", joinColumns=@JoinColumn(name="group_id"))
    private Map<String, String> attributes=new HashMap<>();
    **/

    @ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinTable(
            name="Group_Attributes",
            joinColumns={
                    @JoinColumn(name="group_id")
            },
            inverseJoinColumns = {@JoinColumn(name="key")}
    )
    private Set<Attributes> attributes=new HashSet<>();

    @ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinTable(
            name="Group_Member",
            joinColumns={
                   @JoinColumn(name="group_id")
            },
            inverseJoinColumns = {@JoinColumn(name="member_id")}
    )
    private Set<Member> members=new HashSet<>();

}
