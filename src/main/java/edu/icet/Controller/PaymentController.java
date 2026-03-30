package edu.icet.Controller;

import edu.icet.Model.Dto.PaymentDto;
import edu.icet.Model.Dto.PaymentRequestDto;
import edu.icet.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PaymentController {

    private final PaymentService paymentService;

    // Create payment
    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        PaymentDto createdPayment = paymentService.createPayment(paymentRequestDto);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    // Get payment by ID
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long paymentId) {
        PaymentDto payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }

    // Get payment by booking ID
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentDto> getPaymentByBookingId(@PathVariable Long bookingId) {
        PaymentDto payment = paymentService.getPaymentByBookingId(bookingId);
        return ResponseEntity.ok(payment);
    }

    // Get all payments
    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // Get payments by date
    @GetMapping("/date/{paymentDate}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDate) {
        List<PaymentDto> payments = paymentService.getPaymentsByDate(paymentDate);
        return ResponseEntity.ok(payments);
    }

    // Get payments between dates
    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentDto>> getPaymentsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PaymentDto> payments = paymentService.getPaymentsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(payments);
    }

    // Update payment
    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> updatePayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody PaymentDto paymentDto) {
        PaymentDto updatedPayment = paymentService.updatePayment(paymentId, paymentDto);
        return ResponseEntity.ok(updatedPayment);
    }

    // Delete payment
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<String> deletePayment(@PathVariable Long paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok("Payment deleted successfully");
    }

    // Get total revenue
    @GetMapping("/revenue/total")
    public ResponseEntity<Map<String, Double>> getTotalRevenue() {
        Double totalRevenue = paymentService.calculateTotalRevenue();
        Map<String, Double> response = new HashMap<>();
        response.put("totalRevenue", totalRevenue);
        return ResponseEntity.ok(response);
    }

    // Get revenue for period
    @GetMapping("/revenue/period")
    public ResponseEntity<Map<String, Object>> getRevenueForPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Double revenue = paymentService.calculateRevenueForPeriod(startDate, endDate);
        long paymentCount = paymentService.getPaymentCountForPeriod(startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("totalRevenue", revenue);
        response.put("paymentCount", paymentCount);
        response.put("averagePayment", paymentCount > 0 ? revenue / paymentCount : 0.0);

        return ResponseEntity.ok(response);
    }
}
