package org.tutorials.wproject1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Member")
public class Member implements Serializable{

    /**
	 *
	 */
	private static final long serialVersionUID = 3L;

	@NotNull
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private short rating;

    @ManyToMany(mappedBy="members")
    private Set<Group> groups;


}
