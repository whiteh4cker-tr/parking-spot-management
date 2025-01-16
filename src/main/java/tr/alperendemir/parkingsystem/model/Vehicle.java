package tr.alperendemir.parkingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String licensePlate;

    @Column(nullable = false)
    private String type; // e.g., CAR, MOTORCYCLE

    @JsonIgnore // Add this annotation to break the circular reference
    @OneToOne(mappedBy = "vehicle")
    private ParkingSpot parkingSpot;
}