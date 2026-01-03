package edu.icet.Repository;

import edu.icet.Model.Entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    // Find all rentals for a specific user
    List<Rental> findByUserId(Long userId);

    // Find all rentals for a specific car
    List<Rental> findByCarId(Long carId);

    // Find rentals by status
    List<Rental> findByStatus(String status);

    // Find rentals by user and status
    List<Rental> findByUserIdAndStatus(Long userId, String status);

    // Find rentals by car and status
    List<Rental> findByCarIdAndStatus(Long carId, String status);

    // Check if car is available for specific dates
    @Query("SELECT COUNT(r) FROM Rental r WHERE r.carId = :carId " +
            "AND r.status IN ('pending', 'approved', 'active') " +
            "AND ((r.startDate <= :endDate AND r.endDate >= :startDate))")
    long countOverlappingRentals(@Param("carId") Long carId,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);

    // Find active rentals (currently rented)
    @Query("SELECT r FROM Rental r WHERE r.status = 'active' " +
            "AND r.startDate <= :currentDate AND r.endDate >= :currentDate")
    List<Rental> findActiveRentals(@Param("currentDate") LocalDate currentDate);

    // Find upcoming rentals
    @Query("SELECT r FROM Rental r WHERE r.status IN ('pending', 'approved') " +
            "AND r.startDate > :currentDate")
    List<Rental> findUpcomingRentals(@Param("currentDate") LocalDate currentDate);

    // Find completed rentals
    @Query("SELECT r FROM Rental r WHERE r.status = 'completed' " +
            "OR (r.status = 'active' AND r.endDate < :currentDate)")
    List<Rental> findCompletedRentals(@Param("currentDate") LocalDate currentDate);

    // Find rentals within date range
    @Query("SELECT r FROM Rental r WHERE r.startDate >= :startDate AND r.endDate <= :endDate")
    List<Rental> findRentalsBetweenDates(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    // Delete all rentals for a specific car (for cascade delete)
    @Modifying
    @Query("DELETE FROM Rental r WHERE r.carId = :carId")
    void deleteByCarId(@Param("carId") Long carId);
}