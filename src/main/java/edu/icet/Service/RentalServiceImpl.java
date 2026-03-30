package edu.icet.Service;

import edu.icet.Model.Dto.RentalDto;
import edu.icet.Model.Dto.RentalRequestDto;
import edu.icet.Model.Entity.CarDetails;
import edu.icet.Model.Entity.Rental;
import edu.icet.Model.Entity.Users;
import edu.icet.Repository.CarDetailsRepository;
import edu.icet.Repository.RentalRepository;
import edu.icet.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService{

    private final RentalRepository rentalRepository;
    private final CarDetailsRepository carDetailsRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public RentalDto createRental(RentalRequestDto rentalRequestDto) {
        // Validate car exists
        CarDetails car = carDetailsRepository.findById(rentalRequestDto.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + rentalRequestDto.getCarId()));

        // Validate user exists
        Users user = userRepository.findById(rentalRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + rentalRequestDto.getUserId()));

        // Validate dates
        if (rentalRequestDto.getEndDate().isBefore(rentalRequestDto.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        // Check car availability
        if (!isCarAvailable(rentalRequestDto.getCarId(), rentalRequestDto.getStartDate(), rentalRequestDto.getEndDate())) {
            throw new RuntimeException("Car is not available for the selected dates");
        }

        // Calculate total price
        long days = ChronoUnit.DAYS.between(rentalRequestDto.getStartDate(), rentalRequestDto.getEndDate()) + 1;
        double totalPrice = car.getRate() * days;

        // Create rental
        Rental rental = new Rental();
        rental.setCarId(rentalRequestDto.getCarId());
        rental.setUserId(rentalRequestDto.getUserId());
        rental.setStartDate(rentalRequestDto.getStartDate());
        rental.setEndDate(rentalRequestDto.getEndDate());
        rental.setTotalPrice(totalPrice);
        rental.setStatus("pending"); // Initial status

        Rental savedRental = rentalRepository.save(rental);
        RentalDto rentalDto = modelMapper.map(savedRental, RentalDto.class);
        rentalDto.setCarModel(car.getCarModel());
        rentalDto.setCarBrand(car.getBrand());
        rentalDto.setUserName(user.getFullName());
        rentalDto.setUserEmail(user.getEmail());

        return rentalDto;

    }

    @Override
    public RentalDto getRentalById(Long bookingId) {
        Rental rental = rentalRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Rental not found with id: " + bookingId));
        RentalDto rentalDto = modelMapper.map(rental, RentalDto.class);
        enrichRentalDto(rentalDto, rental);
        return rentalDto;
    }



    @Override
    public List<RentalDto> getAllRentals() {
        return rentalRepository.findAll().stream().map(rental -> {
                    RentalDto dto = modelMapper.map(rental, RentalDto.class);
                    enrichRentalDto(dto, rental);
                    return dto;}).collect(Collectors.toList());
    }

    @Override
    public List<RentalDto> getRentalsByUserId(Long userId) {
        return rentalRepository.findByUserId(userId).stream().map(rental -> {
                    RentalDto dto = modelMapper.map(rental, RentalDto.class);
                    enrichRentalDto(dto, rental);
                    return dto;}).collect(Collectors.toList());
    }

    @Override
    public List<RentalDto> getRentalsByCarId(Long carId) {
        return rentalRepository.findByCarId(carId).stream().map(rental -> {
                    RentalDto dto = modelMapper.map(rental, RentalDto.class);
                    enrichRentalDto(dto, rental);
                    return dto;}).collect(Collectors.toList());
    }

    @Override
    public List<RentalDto> getRentalsByStatus(String status) {
        return rentalRepository.findByStatus(status).stream().map(rental -> {
                    RentalDto dto = modelMapper.map(rental, RentalDto.class);
                    enrichRentalDto(dto, rental);
                    return dto;}).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RentalDto updateRentalStatus(Long bookingId, String status) {
        Rental rental = rentalRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Rental not found with id: " + bookingId));

        rental.setStatus(status);

        // If approved, update car status to rented
        if ("approved".equals(status)) {
            CarDetails car = carDetailsRepository.findById(rental.getCarId())
                    .orElseThrow(() -> new RuntimeException("Car not found"));
            car.setStatus("rented");
            carDetailsRepository.save(car);
        }

        // If completed or cancelled, update car status to available
        if ("completed".equals(status) || "cancelled".equals(status)) {
            CarDetails car = carDetailsRepository.findById(rental.getCarId())
                    .orElseThrow(() -> new RuntimeException("Car not found"));
            car.setStatus("available");
            carDetailsRepository.save(car);
        }

        Rental updatedRental = rentalRepository.save(rental);
        RentalDto dto = modelMapper.map(updatedRental, RentalDto.class);
        enrichRentalDto(dto, updatedRental);
        return dto;
    }

    @Override
    @Transactional
    public RentalDto updateRental(Long bookingId, RentalDto rentalDto) {
        Rental existingRental = rentalRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Rental not found with id: " + bookingId));

        // Validate dates if changed
        if (rentalDto.getEndDate().isBefore(rentalDto.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        existingRental.setStartDate(rentalDto.getStartDate());
        existingRental.setEndDate(rentalDto.getEndDate());
        existingRental.setTotalPrice(rentalDto.getTotalPrice());
        existingRental.setStatus(rentalDto.getStatus());

        Rental updatedRental = rentalRepository.save(existingRental);
        RentalDto dto = modelMapper.map(updatedRental, RentalDto.class);
        enrichRentalDto(dto, updatedRental);
        return dto;
    }

    @Override
    @Transactional
    public void cancelRental(Long bookingId) {
        updateRentalStatus(bookingId, "cancelled");
    }


    @Override
    public void deleteRental(Long bookingId) {
        if (!rentalRepository.existsById(bookingId)) {
            throw new RuntimeException("Rental not found with id: " + bookingId);
        }
        rentalRepository.deleteById(bookingId);
    }

    @Override
    public boolean isCarAvailable(Long carId, LocalDate startDate, LocalDate endDate) {
        long overlappingRentals = rentalRepository.countOverlappingRentals(carId, startDate, endDate);
        return overlappingRentals == 0;
    }

    @Override
    public List<RentalDto> getActiveRentals() {
        return rentalRepository.findActiveRentals(LocalDate.now()).stream().map(rental -> {
                    RentalDto dto = modelMapper.map(rental, RentalDto.class);
                    enrichRentalDto(dto, rental);
                    return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RentalDto> getUpcomingRentals() {
        return rentalRepository.findUpcomingRentals(LocalDate.now()).stream().map(rental -> {
                    RentalDto dto = modelMapper.map(rental, RentalDto.class);
                    enrichRentalDto(dto, rental);
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<RentalDto> getCompletedRentals() {
        return rentalRepository.findCompletedRentals(LocalDate.now()).stream()
                .map(rental -> {
                    RentalDto dto = modelMapper.map(rental, RentalDto.class);
                    enrichRentalDto(dto, rental);
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<RentalDto> getRentalsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return rentalRepository.findRentalsBetweenDates(startDate, endDate).stream()
                .map(rental -> {
                    RentalDto dto = modelMapper.map(rental, RentalDto.class);
                    enrichRentalDto(dto, rental);
                    return dto;
                }).collect(Collectors.toList());
    }

    private void enrichRentalDto(RentalDto rentalDto, Rental rental) {
        // Find car by ID and add car details to DTO
        carDetailsRepository.findById(rental.getCarId()).ifPresent(car -> {
            rentalDto.setCarModel(car.getCarModel());
            rentalDto.setCarBrand(car.getBrand());
        });

        // Find user by ID and add user details to DTO
        userRepository.findById(rental.getUserId()).ifPresent(user -> {
            rentalDto.setUserName(user.getFullName());
            rentalDto.setUserEmail(user.getEmail());
        });
    }
}
