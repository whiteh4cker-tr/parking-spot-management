package tr.alperendemir.parkingsystem.controller;

import org.springframework.http.ResponseEntity;
import tr.alperendemir.parkingsystem.model.ParkingSpot;
import tr.alperendemir.parkingsystem.model.ParkingSpotStatus;
import tr.alperendemir.parkingsystem.model.Vehicle;
import tr.alperendemir.parkingsystem.service.ParkingSpotService;
import tr.alperendemir.parkingsystem.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotService parkingSpotService;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @PostMapping("/park")
    public ParkingSpot parkVehicle(@RequestParam String spotNumber, @RequestBody Vehicle vehicle) {
        return parkingSpotService.parkVehicle(spotNumber, vehicle);
    }

    @PostMapping("/unpark")
    public ParkingSpot unparkVehicle(@RequestParam String spotNumber) {
        return parkingSpotService.unparkVehicle(spotNumber);
    }

    @GetMapping("/spots")
    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotRepository.findAll();
    }

    // Add a new parking spot
    @PostMapping("/spots/add")
    public ParkingSpot addParkingSpot(@RequestParam String spotNumber) {
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setSpotNumber(spotNumber);
        parkingSpot.setStatus(ParkingSpotStatus.AVAILABLE);
        return parkingSpotRepository.save(parkingSpot);
    }

    // Remove a parking spot
    @DeleteMapping("/spots/remove")
    public ResponseEntity<String> removeParkingSpot(@RequestParam String spotNumber) {
        ParkingSpot parkingSpot = parkingSpotRepository.findBySpotNumber(spotNumber)
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));

        if (parkingSpot.getStatus() == ParkingSpotStatus.OCCUPIED) {
            return ResponseEntity.badRequest().body("Cannot remove an occupied parking spot");
        }

        parkingSpotRepository.delete(parkingSpot);
        return ResponseEntity.ok("Parking spot removed successfully");
    }
}