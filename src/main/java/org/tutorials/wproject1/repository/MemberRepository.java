package org.tutorials.wproject1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutorials.wproject1.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {


}
