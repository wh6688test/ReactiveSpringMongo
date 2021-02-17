package org.tutorials.wproject1.model;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Data;

@Document(collection="groups")
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Group  {
    @Id
    private String gid;
    @NotBlank
    @Size(max=150)
    private String groupName;
    
    private String attr1;
    private String attr2;

    private String memberName;
    private short rating;
}
