package org.tutorials.wproject1.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tutorials.wproject1.model.Group;
import org.tutorials.wproject1.model.Member;

import java.util.Optional;
import java.util.Set;

@Repository
public interface  GroupRepository extends CrudRepository<Group, Long> {

        @Query(value="select g from Group g join Member m where g.gid=m.group_id and m.rating = ?1")
        public Optional<Set<Group>> findGroupsByMemberRating(Short rating);

        @Query(value="select g from Group g join Member m where g.gid=m.group_id and m.id= ?1")
        public Optional<Set<Group>> findGroupsByMemberId(String memberId);

}
