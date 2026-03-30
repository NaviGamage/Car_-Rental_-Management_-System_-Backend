package edu.icet.Model.Dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CarDetailsDto {

    private Long carId;

    @NotBlank(message = "Car model is required")
    private String carModel;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Plate number is required")
    private String plateNumber;

    @NotBlank(message = "Fuel type is required")
    private String typesOfFuel;

    @NotNull(message = "Rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Rate must be greater than 0")
    private Double rate;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "available|rented|under maintenance")
    private String status;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be at least 1900")
    @Max(value = 2100, message = "Year must not exceed 2100")
    private Integer year;

    @NotNull(message = "Seating capacity is required")
    @Min(value = 1, message = "Seating capacity must be at least 1")
    @Max(value = 50, message = "Seating capacity must not exceed 50")
    private Integer seatingCapacity;

    private String description;
}
