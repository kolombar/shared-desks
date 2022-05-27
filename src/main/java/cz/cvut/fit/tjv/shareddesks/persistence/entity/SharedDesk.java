package cz.cvut.fit.tjv.shareddesks.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SharedDesk {

  public SharedDesk(Equipment equipment, Branch branch) {
    this.equipment = equipment;
    this.isLocatedAt = branch;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "desk_id")
  private Long id;

  @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL)
  private Set<Booking> bookings = new HashSet<>();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Equipment equipment;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Branch isLocatedAt;

  @Override
  public String toString() {
      String id = "";
      if (this.id != null) {
          id = this.id.toString();
      }

      String equipment = "";
      if (this.equipment != null) {
          equipment = this.equipment.toString();
      }

      String city = "";
      if (this.isLocatedAt != null) {
          city = this.isLocatedAt.getCity();
      }
    return id + ", " + equipment + ", " + city;
  }
}
