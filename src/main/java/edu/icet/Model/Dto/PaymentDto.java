package edu.icet.Model.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaymentDto {

    private Long paymentId;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private Double amount;

    // Additional fields for display (optional)
    private Long carId;           // Add this
    private Long userId;          // Add this
    private String carModel;      // Add this
    private String carBrand;      // Add this
    private String userName;      // Add this
    private String userEmail;     // Add this
    private String rentalStatus;  // Add this
}
