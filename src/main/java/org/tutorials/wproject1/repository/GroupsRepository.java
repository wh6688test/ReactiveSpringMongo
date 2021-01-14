package org.tutorials.wproject1.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tutorials.wproject1.model.Groups;

import java.util.List;
import java.util.Optional;

@Repository
public interface  GroupsRepository extends CrudRepository<Groups, Long> {
       // @EntityGraph(attributePaths = { "members" })
        //@EntityGraph(value = "Groups.members", type = EntityGraphType.LOAD)
        public List<Groups> findAll();

        @Query(value="select distinct g from Groups g WHERE g.gid= :gid")
        //@EntityGraph(value = "Groups.members", type = EntityGraphType.LOAD)
        public Optional<Groups> findGroupsByID(Long gid);

        @Query(value="select distinct g from Groups g WHERE g.groupName = :groupName")
        //@EntityGraph(value = "Groups.members", type = EntityGraphType.LOAD)
        public Optional<List<Groups>> findGroupsByGroupName(String groupName);

        @Query(value="select distinct g from Groups g JOIN g.members m WHERE m.rating = :rating")
        //@EntityGraph(value = "Groups.members", type = EntityGraphType.LOAD)
        public Optional<List<Groups>> findGroupsByMemberRating(@Param("rating") short rating);

        @Query(value="select distinct g from Groups g JOIN g.members m WHERE m.name = :name")
        //@EntityGraph(value = "Groups.members", type = EntityGraphType.LOAD)
        public Optional<List<Groups>> findGroupsByMemberName(@Param("name") String name);

}
