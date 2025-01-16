package tr.alperendemir.parkingsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String spotNumber;

    @Enumerated(EnumType.STRING)
    private ParkingSpotStatus status;

    @OneToOne
    private Vehicle vehicle;
}