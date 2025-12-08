package edu.icet.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Cars")

public class CarDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long carId;

    @Column(name = "car_model", nullable = false)
    private String carModel;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "plate_number", unique = true, nullable = false)
    private String plateNumber;

    @Column(name = "types_of_fuel", nullable = false)
    private String typesOfFuel;

    @Column(name = "rate", nullable = false)
    private Double rate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "seating_capacity", nullable = false)
    private Integer seatingCapacity;

    @Column(name = "description", length = 1000)
    private String description;
}
