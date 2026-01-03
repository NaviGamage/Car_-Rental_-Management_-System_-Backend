package edu.icet.Service;

import edu.icet.Model.Dto.CarDetailsDto;
import edu.icet.Model.Entity.CarDetails;
import edu.icet.Model.Entity.Rental;
import edu.icet.Repository.CarDetailsRepository;
import edu.icet.Repository.CarImageRepository;
import edu.icet.Repository.RentalRepository;
import edu.icet.Repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarDetailsServiceImpl implements CarDetailsService{

    private final CarDetailsRepository carDetailsRepository;
    private final RentalRepository rentalRepository;
    private final CarImageRepository carImageRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CarDetailsDto addCar(CarDetailsDto carDetailsDto) {

        if (carDetailsRepository.existsByPlateNumber(carDetailsDto.getPlateNumber())){
            throw new RuntimeException("Car with plate number " + carDetailsDto.getPlateNumber() + " already exists");
        }

        CarDetails carDetails = modelMapper.map(carDetailsDto, CarDetails.class);
        CarDetails savedCar = carDetailsRepository.save(carDetails);
        return modelMapper.map(savedCar, CarDetailsDto.class);
    }

    @Override
    @Transactional
    public CarDetailsDto updateCar(Long carId, CarDetailsDto carDetailsDto) {
        CarDetails existingCar = carDetailsRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + carId));

        if (!existingCar.getPlateNumber().equals(carDetailsDto.getPlateNumber()) &&
                carDetailsRepository.existsByPlateNumber(carDetailsDto.getPlateNumber())) {
            throw new RuntimeException("Car with plate number " + carDetailsDto.getPlateNumber() + " already exists");
        }

        existingCar.setCarModel(carDetailsDto.getCarModel());
        existingCar.setBrand(carDetailsDto.getBrand());
        existingCar.setPlateNumber(carDetailsDto.getPlateNumber());
        existingCar.setTypesOfFuel(carDetailsDto.getTypesOfFuel());
        existingCar.setRate(carDetailsDto.getRate());
        existingCar.setStatus(carDetailsDto.getStatus());
        existingCar.setYear(carDetailsDto.getYear());
        existingCar.setSeatingCapacity(carDetailsDto.getSeatingCapacity());
        existingCar.setDescription(carDetailsDto.getDescription());

        CarDetails updatedCar = carDetailsRepository.save(existingCar);
        return modelMapper.map(updatedCar, CarDetailsDto.class);
    }

    @Override
    @Transactional
    public void deleteCar(Long carId) {
        if (!carDetailsRepository.existsById(carId)) {
            throw new RuntimeException("Car not found with id: " + carId);
        }

        // Delete in order: payments → rentals → images → car
        // 1. Delete all payments associated with rentals for this car
        paymentRepository.deleteByCarId(carId);

        // 2. Delete rentals
        rentalRepository.deleteByCarId(carId);

        // 3. Delete car images
        carImageRepository.deleteByCarId(carId);

        // 4. Finally delete the car
        carDetailsRepository.deleteById(carId);
    }

    @Override
    public CarDetailsDto getCarById(Long carId) {
        CarDetails carDetails = carDetailsRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + carId));
        return modelMapper.map(carDetails, CarDetailsDto.class);
    }

    @Override
    public CarDetailsDto getCarByPlateNumber(String plateNumber) {
        CarDetails carDetails = carDetailsRepository.findByPlateNumber(plateNumber)
                .orElseThrow(() -> new RuntimeException("Car not found with plate number: " + plateNumber));
        return modelMapper.map(carDetails, CarDetailsDto.class);
    }

    @Override
    public List<CarDetailsDto> getAllCars() {
        return carDetailsRepository.findAll().stream()
                .map(car -> modelMapper.map(car, CarDetailsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDetailsDto> getCarsByStatus(String status) {
        return carDetailsRepository.findByStatus(status).stream()
                .map(car -> modelMapper.map(car, CarDetailsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDetailsDto> getCarsByBrand(String brand) {
        return carDetailsRepository.findByBrand(brand).stream()
                .map(car -> modelMapper.map(car, CarDetailsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDetailsDto> getCarsByFuelType(String fuelType) {
        return carDetailsRepository.findByTypesOfFuel(fuelType).stream()
                .map(car -> modelMapper.map(car, CarDetailsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDetailsDto> getCarsByPriceRange(Double minRate, Double maxRate) {
        return carDetailsRepository.findByRateBetween(minRate, maxRate).stream()
                .map(car -> modelMapper.map(car, CarDetailsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDetailsDto> getCarsBySeatingCapacity(Integer seatingCapacity) {
        return carDetailsRepository.findBySeatingCapacity(seatingCapacity).stream()
                .map(car -> modelMapper.map(car, CarDetailsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDetailsDto> searchCars(String brand, String status, String fuelType, Double minRate, Double maxRate, Integer seatingCapacity) {
        List<CarDetails> cars = carDetailsRepository.findAll();

        return cars.stream()
                .filter(car -> brand == null || car.getBrand().equalsIgnoreCase(brand))
                .filter(car -> status == null || car.getStatus().equalsIgnoreCase(status))
                .filter(car -> fuelType == null || car.getTypesOfFuel().equalsIgnoreCase(fuelType))
                .filter(car -> minRate == null || car.getRate() >= minRate)
                .filter(car -> maxRate == null || car.getRate() <= maxRate)
                .filter(car -> seatingCapacity == null || car.getSeatingCapacity().equals(seatingCapacity))
                .map(car -> modelMapper.map(car, CarDetailsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateCarStatus(Long carId, String status) {
        CarDetails carDetails = carDetailsRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + carId));
        carDetails.setStatus(status);
        carDetailsRepository.save(carDetails);
    }
}