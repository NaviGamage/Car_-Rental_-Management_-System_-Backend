package edu.icet.Repository;

import edu.icet.Model.Entity.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarImageRepository extends JpaRepository<CarImage,Long> {

    // Find all images for a specific car
    List<CarImage> findByCarId(Long carId);

    // Find image by name
    Optional<CarImage> findByImageName(String imageName);

    // Delete all images for a specific car
    void deleteByCarId(Long carId);

    // Check if car has images
    boolean existsByCarId(Long carId);

    // Count images for a car
    long countByCarId(Long carId);

}
