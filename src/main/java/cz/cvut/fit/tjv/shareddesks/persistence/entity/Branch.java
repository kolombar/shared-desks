package cz.cvut.fit.tjv.shareddesks.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Branch {

    public Branch (String city) {
        this.city = city;
    }

    @Id
    private String city;

    @OneToMany(mappedBy = "belongsTo")
    private Set<Employee> employees;

    @OneToMany(mappedBy = "isLocatedAt")
    private Set<SharedDesk> sharedDesks;
}
