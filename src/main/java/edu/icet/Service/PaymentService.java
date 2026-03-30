package edu.icet.Service;

import edu.icet.Model.Dto.PaymentDto;
import edu.icet.Model.Dto.PaymentRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {

    // Create payment
    PaymentDto createPayment(PaymentRequestDto paymentRequestDto);

    // Get payment by ID
    PaymentDto getPaymentById(Long paymentId);

    // Get payment by booking ID
    PaymentDto getPaymentByBookingId(Long bookingId);

    // Get all payments
    List<PaymentDto> getAllPayments();

    // Get payments by date
    List<PaymentDto> getPaymentsByDate(LocalDate paymentDate);

    // Get payments between dates
    List<PaymentDto> getPaymentsBetweenDates(LocalDate startDate, LocalDate endDate);

    // Update payment
    PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto);

    // Delete payment
    void deletePayment(Long paymentId);

    // Calculate total revenue
    Double calculateTotalRevenue();

    // Calculate revenue for period
    Double calculateRevenueForPeriod(LocalDate startDate, LocalDate endDate);

    // Get payment count for period
    long getPaymentCountForPeriod(LocalDate startDate, LocalDate endDate);
}
