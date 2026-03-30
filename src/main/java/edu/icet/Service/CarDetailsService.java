package edu.icet.Service;

import edu.icet.Model.Dto.CarDetailsDto;

import java.util.List;

public interface CarDetailsService {

    CarDetailsDto addCar(CarDetailsDto carDetailsDto);

    CarDetailsDto updateCar(Long carId,CarDetailsDto carDetailsDto);

    void deleteCar(Long carId);

    CarDetailsDto getCarById(Long carId);

    CarDetailsDto getCarByPlateNumber(String plateNumber);

    List<CarDetailsDto> getAllCars();

    List<CarDetailsDto> getCarsByStatus(String status);

    List<CarDetailsDto> getCarsByBrand(String brand);

    List<CarDetailsDto> getCarsByFuelType(String fuelType);

    List<CarDetailsDto> getCarsByPriceRange(Double minRate, Double maxRate);

    List<CarDetailsDto> getCarsBySeatingCapacity(Integer seatingCapacity);

    List<CarDetailsDto> searchCars(String brand, String status, String fuelType,
                                   Double minRate, Double maxRate, Integer seatingCapacity);

    void updateCarStatus(Long carId, String status);
}
