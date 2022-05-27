package cz.cvut.fit.tjv.shareddesks.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Employee {

  public Employee(String firstName, String lastName, int employmentDurationInYears, Branch belongsTo, boolean isManager) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.employmentDurationInYears = employmentDurationInYears;
    this.belongsTo = belongsTo;
    this.isManager = isManager;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "employee_id")
  private Long id;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private Set<Booking> bookings = new HashSet<>();

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;
  private Integer employmentDurationInYears;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Branch belongsTo;

  @Column(nullable = false)
  private Boolean isManager;
}
