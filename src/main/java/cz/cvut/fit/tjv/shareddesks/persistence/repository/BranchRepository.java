package cz.cvut.fit.tjv.shareddesks.persistence.repository;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, String> {
}
