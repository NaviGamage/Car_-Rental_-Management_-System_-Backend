package edu.icet.Model.Dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalDto {

    private Long bookingId;

    @NotNull(message = "Car ID is required")
    private Long carId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    private Double totalPrice;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "pending|approved|active|completed|cancelled",
            message = "Status must be: pending, approved, active, completed, or cancelled")
    private String status;

    private String carModel;
    private String carBrand;
    private String userName;
    private String userEmail;

}
