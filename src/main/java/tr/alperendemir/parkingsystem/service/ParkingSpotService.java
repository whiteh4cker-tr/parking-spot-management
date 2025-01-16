package tr.alperendemir.parkingsystem.service;

import tr.alperendemir.parkingsystem.model.ParkingSpot;
import tr.alperendemir.parkingsystem.model.ParkingSpotStatus;
import tr.alperendemir.parkingsystem.model.Vehicle;
import tr.alperendemir.parkingsystem.repository.ParkingSpotRepository;
import tr.alperendemir.parkingsystem.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingSpotService {

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public ParkingSpot parkVehicle(String spotNumber, Vehicle vehicle) {
        ParkingSpot parkingSpot = parkingSpotRepository.findBySpotNumber(spotNumber)
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));

        if (parkingSpot.getStatus() == ParkingSpotStatus.OCCUPIED) {
            throw new RuntimeException("Parking spot is already occupied");
        }

        parkingSpot.setVehicle(vehicle);
        parkingSpot.setStatus(ParkingSpotStatus.OCCUPIED);
        vehicle.setParkingSpot(parkingSpot);

        vehicleRepository.save(vehicle);
        return parkingSpotRepository.save(parkingSpot);
    }

    public ParkingSpot unparkVehicle(String spotNumber) {
        ParkingSpot parkingSpot = parkingSpotRepository.findBySpotNumber(spotNumber)
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));

        if (parkingSpot.getStatus() == ParkingSpotStatus.AVAILABLE) {
            throw new RuntimeException("Parking spot is already available");
        }

        Vehicle vehicle = parkingSpot.getVehicle();
        vehicle.setParkingSpot(null);
        parkingSpot.setVehicle(null);
        parkingSpot.setStatus(ParkingSpotStatus.AVAILABLE);

        vehicleRepository.save(vehicle);
        return parkingSpotRepository.save(parkingSpot);
    }
}