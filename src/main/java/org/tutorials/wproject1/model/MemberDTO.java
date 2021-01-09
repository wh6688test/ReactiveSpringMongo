package org.tutorials.wproject1.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class MemberDTO {

    private Long memberId;

    private String name;
    private short rating;

    public MemberDTO(Long memberId, String name, short rating) {
        this.memberId = memberId;
        this.name = name;
        this.rating = rating;
    }

}
