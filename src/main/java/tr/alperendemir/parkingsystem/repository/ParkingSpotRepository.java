package tr.alperendemir.parkingsystem.repository;

import tr.alperendemir.parkingsystem.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    Optional<ParkingSpot> findBySpotNumber(String spotNumber);
}