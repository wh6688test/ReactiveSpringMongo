package org.tutorials.wproject1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="Member")
public class Member {
    @NotNull
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long memberId;

    @NotBlank
    @Size(min = 1, max = 20)
    private String name;
    @Min(1)
    @Max(6)
    private short rating;

}