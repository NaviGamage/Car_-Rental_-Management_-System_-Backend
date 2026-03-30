package edu.icet.Repository;

import edu.icet.Model.Entity.CarDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarDetailsRepository extends JpaRepository<CarDetails, Long> {

    Optional<CarDetails> findByPlateNumber(String plateNumber);

    List<CarDetails> findByStatus(String status);

    List<CarDetails> findByBrand(String brand);

    List<CarDetails> findByTypesOfFuel(String typesOfFuel);

    List<CarDetails> findByRateBetween(Double minRate, Double maxRate);

    List<CarDetails> findBySeatingCapacity(Integer seatingCapacity);

    List<CarDetails> findByBrandAndStatus(String brand, String status);

    boolean existsByPlateNumber(String plateNumber);
}
