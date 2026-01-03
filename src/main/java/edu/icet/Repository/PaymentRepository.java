package edu.icet.Repository;

import edu.icet.Model.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    // Find payment by booking ID
    Optional<Payment> findByBookingId(Long bookingId);

    // Check if payment exists for a booking
    boolean existsByBookingId(Long bookingId);

    // Find payments by date
    List<Payment> findByPaymentDate(LocalDate paymentDate);

    // Find payments within date range
    @Query("SELECT p FROM Payment p WHERE p.paymentDate >= :startDate AND p.paymentDate <= :endDate")
    List<Payment> findPaymentsBetweenDates(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    // Calculate total revenue
    @Query("SELECT SUM(p.amount) FROM Payment p")
    Double calculateTotalRevenue();

    // Calculate revenue for a specific period
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate >= :startDate AND p.paymentDate <= :endDate")
    Double calculateRevenueForPeriod(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

    // Count payments
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentDate >= :startDate AND p.paymentDate <= :endDate")
    long countPaymentsForPeriod(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);

    // Delete all payments for rentals associated with a specific car (native SQL)
    @Modifying
    @Query(value = "DELETE FROM payments WHERE booking_id IN (SELECT booking_id FROM rentals WHERE car_id = :carId)", nativeQuery = true)
    void deleteByCarId(@Param("carId") Long carId);
}