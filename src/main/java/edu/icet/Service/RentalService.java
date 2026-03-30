package edu.icet.Service;

import edu.icet.Model.Dto.RentalDto;
import edu.icet.Model.Dto.RentalRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface RentalService {

    // Create new rental (booking)
    RentalDto createRental(RentalRequestDto rentalRequestDto);

    // Get rental by ID
    RentalDto getRentalById(Long bookingId);

    // Get all rentals
    List<RentalDto> getAllRentals();

    // Get rentals by user ID
    List<RentalDto> getRentalsByUserId(Long userId);

    // Get rentals by car ID
    List<RentalDto> getRentalsByCarId(Long carId);

    // Get rentals by status
    List<RentalDto> getRentalsByStatus(String status);

    // Update rental status
    RentalDto updateRentalStatus(Long bookingId, String status);

    // Update rental
    RentalDto updateRental(Long bookingId, RentalDto rentalDto);

    // Cancel rental
    void cancelRental(Long bookingId);

    // Delete rental
    void deleteRental(Long bookingId);

    // Check car availability
    boolean isCarAvailable(Long carId, LocalDate startDate, LocalDate endDate);

    // Get active rentals
    List<RentalDto> getActiveRentals();

    // Get upcoming rentals
    List<RentalDto> getUpcomingRentals();

    // Get completed rentals
    List<RentalDto> getCompletedRentals();

    // Get rentals within date range
    List<RentalDto> getRentalsBetweenDates(LocalDate startDate, LocalDate endDate);
}
