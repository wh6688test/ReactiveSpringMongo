package org.tutorials.wproject1.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tutorials.wproject1.model.Group;

import java.util.List;
import java.util.Optional;

@Repository
public interface  GroupRepository extends CrudRepository<Group, Long> {

        @Query(value="select g from Group g")
        public Iterable<Group> findAll();

        @Query(value="select g from Group g WHERE g.groupName = :groupName")
        public Optional<List<Group>> findGroupsByGroupName(String groupName);

        @Query(value="select * from Group g INNER JOIN Member m WHERE m.rating = :rating", nativeQuery = true)
        public Optional<List<Group>> findGroupsByMemberRating(@Param("rating") short rating);

        @Query(value="select * from Group g INNER JOIN Member m WHERE m.name = :name", nativeQuery = true)
        public Optional<List<Group>> findGroupsByMemberName(@Param("name") String name);

}
