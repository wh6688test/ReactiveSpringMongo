package org.tutorials.wproject1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;

@Entity
    @Transactional
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name="Attributes")
   public class Attributes implements Serializable{

        /**
	 *
	 */
	private static final long serialVersionUID = 2L;

		@NotNull
        private String key;

        private String value;

        @ManyToMany(mappedBy="attributes")
        private Set<Group>groups;

    }
