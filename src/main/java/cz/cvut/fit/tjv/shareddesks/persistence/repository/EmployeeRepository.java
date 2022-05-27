package cz.cvut.fit.tjv.shareddesks.persistence.repository;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Branch;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e.belongsTo FROM Employee e WHERE e.id = ?1")
    Optional<Branch> getEmployeeBranch(Long id);
}
