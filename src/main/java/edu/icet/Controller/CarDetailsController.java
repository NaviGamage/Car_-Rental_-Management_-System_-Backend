package edu.icet.Controller;

import edu.icet.Model.Dto.CarDetailsDto;
import edu.icet.Service.CarDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

public class CarDetailsController {

    private final CarDetailsService carDetailsService;

    @PostMapping
    public ResponseEntity<CarDetailsDto> addCar(@Valid @RequestBody CarDetailsDto carDetailsDto) {
        CarDetailsDto savedCar = carDetailsService.addCar(carDetailsDto);
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
    }

    @PostMapping("/{carId}")
    public ResponseEntity<CarDetailsDto> updateCar(@PathVariable Long carId,@Valid @RequestBody CarDetailsDto carDetailsDto) {
        CarDetailsDto updatedCar = carDetailsService.updateCar(carId, carDetailsDto);
        return ResponseEntity.ok(updatedCar);
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<String> deleteCar(@PathVariable Long carId) {carDetailsService.deleteCar(carId);
        return ResponseEntity.ok("Car deleted successfully");
    }

    @GetMapping("/{carId}")
    public ResponseEntity<CarDetailsDto> getCarById(@PathVariable Long carId) {
        CarDetailsDto car = carDetailsService.getCarById(carId);
        return ResponseEntity.ok(car);
    }

    @GetMapping("/plate/{plateNumber}")
    public ResponseEntity<CarDetailsDto> getCarByPlateNumber(@PathVariable String plateNumber) {
        CarDetailsDto car = carDetailsService.getCarByPlateNumber(plateNumber);
        return ResponseEntity.ok(car);
    }

    @GetMapping
    public ResponseEntity<List<CarDetailsDto>> getAllCars() {
        List<CarDetailsDto> cars = carDetailsService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CarDetailsDto>> getCarsByStatus(@PathVariable String status) {
        List<CarDetailsDto> cars = carDetailsService.getCarsByStatus(status);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<CarDetailsDto>> getCarsByBrand(@PathVariable String brand) {
        List<CarDetailsDto> cars = carDetailsService.getCarsByBrand(brand);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/fuel/{fuelType}")
    public ResponseEntity<List<CarDetailsDto>> getCarsByFuelType(@PathVariable String fuelType) {
        List<CarDetailsDto> cars = carDetailsService.getCarsByFuelType(fuelType);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<CarDetailsDto>> getCarsByPriceRange(@RequestParam Double minRate,@RequestParam Double maxRate) {
        List<CarDetailsDto> cars = carDetailsService.getCarsByPriceRange(minRate, maxRate);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/capacity/{seatingCapacity}")
    public ResponseEntity<List<CarDetailsDto>> getCarsBySeatingCapacity(@PathVariable Integer seatingCapacity) {
        List<CarDetailsDto> cars = carDetailsService.getCarsBySeatingCapacity(seatingCapacity);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CarDetailsDto>> searchCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) Double minRate,
            @RequestParam(required = false) Double maxRate,
            @RequestParam(required = false) Integer seatingCapacity) {
        List<CarDetailsDto> cars = carDetailsService.searchCars(brand, status, fuelType, minRate, maxRate, seatingCapacity);
        return ResponseEntity.ok(cars);
    }

    @PatchMapping("/{carId}/status")
    public ResponseEntity<String> updateCarStatus(@PathVariable Long carId,@RequestParam String status) {
        carDetailsService.updateCarStatus(carId, status);
        return ResponseEntity.ok("Car status updated successfully");
    }
}
