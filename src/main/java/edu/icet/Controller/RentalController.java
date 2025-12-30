package edu.icet.Controller;

import edu.icet.Model.Dto.RentalDto;
import edu.icet.Model.Dto.RentalRequestDto;
import edu.icet.Service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RentalController {

    private final RentalService rentalService;

    // Create new rental (booking)
    @PostMapping
    public ResponseEntity<RentalDto> createRental(@Valid @RequestBody RentalRequestDto rentalRequestDto) {
        RentalDto createdRental = rentalService.createRental(rentalRequestDto);
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
    }

    // Get rental by ID
    @GetMapping("/{bookingId}")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Long bookingId) {
        RentalDto rental = rentalService.getRentalById(bookingId);
        return ResponseEntity.ok(rental);
    }

    // Get all rentals
    @GetMapping
    public ResponseEntity<List<RentalDto>> getAllRentals() {
        List<RentalDto> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }

    // Get rentals by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalDto>> getRentalsByUserId(@PathVariable Long userId) {
        List<RentalDto> rentals = rentalService.getRentalsByUserId(userId);
        return ResponseEntity.ok(rentals);
    }

    // Get rentals by car ID
    @GetMapping("/car/{carId}")
    public ResponseEntity<List<RentalDto>> getRentalsByCarId(@PathVariable Long carId) {
        List<RentalDto> rentals = rentalService.getRentalsByCarId(carId);
        return ResponseEntity.ok(rentals);
    }

    // Get rentals by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RentalDto>> getRentalsByStatus(@PathVariable String status) {
        List<RentalDto> rentals = rentalService.getRentalsByStatus(status);
        return ResponseEntity.ok(rentals);
    }

    // Get active rentals
    @GetMapping("/active")
    public ResponseEntity<List<RentalDto>> getActiveRentals() {
        List<RentalDto> rentals = rentalService.getActiveRentals();
        return ResponseEntity.ok(rentals);
    }

    // Get upcoming rentals
    @GetMapping("/upcoming")
    public ResponseEntity<List<RentalDto>> getUpcomingRentals() {
        List<RentalDto> rentals = rentalService.getUpcomingRentals();
        return ResponseEntity.ok(rentals);
    }

    // Get completed rentals
    @GetMapping("/completed")
    public ResponseEntity<List<RentalDto>> getCompletedRentals() {
        List<RentalDto> rentals = rentalService.getCompletedRentals();
        return ResponseEntity.ok(rentals);
    }

    // Get rentals between dates
    @GetMapping("/date-range")
    public ResponseEntity<List<RentalDto>> getRentalsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<RentalDto> rentals = rentalService.getRentalsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(rentals);
    }

    // Update rental
    @PutMapping("/{bookingId}")
    public ResponseEntity<RentalDto> updateRental(
            @PathVariable Long bookingId,
            @Valid @RequestBody RentalDto rentalDto) {
        RentalDto updatedRental = rentalService.updateRental(bookingId, rentalDto);
        return ResponseEntity.ok(updatedRental);
    }

    // Update rental status
    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<RentalDto> updateRentalStatus(
            @PathVariable Long bookingId,
            @RequestParam String status) {
        RentalDto updatedRental = rentalService.updateRentalStatus(bookingId, status);
        return ResponseEntity.ok(updatedRental);
    }

    // Cancel rental
    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<String> cancelRental(@PathVariable Long bookingId) {
        rentalService.cancelRental(bookingId);
        return ResponseEntity.ok("Rental cancelled successfully");
    }

    // Delete rental
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> deleteRental(@PathVariable Long bookingId) {
        rentalService.deleteRental(bookingId);
        return ResponseEntity.ok("Rental deleted successfully");
    }

    // Check car availability
    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkCarAvailability(
            @RequestParam Long carId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        boolean isAvailable = rentalService.isCarAvailable(carId, startDate, endDate);
        return ResponseEntity.ok(isAvailable);
    }

}
