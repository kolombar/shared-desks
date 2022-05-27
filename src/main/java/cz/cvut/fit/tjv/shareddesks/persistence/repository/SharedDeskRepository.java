package cz.cvut.fit.tjv.shareddesks.persistence.repository;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.SharedDesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SharedDeskRepository extends JpaRepository<SharedDesk, Long> {

    @Query("SELECT d.isLocatedAt FROM SharedDesk d WHERE d.id = ?1")
    Optional<Branch> getDeskLocation(Long id);
}

