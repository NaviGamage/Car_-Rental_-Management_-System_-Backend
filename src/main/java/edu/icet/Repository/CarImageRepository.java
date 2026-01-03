package edu.icet.Repository;

import edu.icet.Model.Entity.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarImageRepository extends JpaRepository<CarImage, Long> {

    // Find all images for a specific car
    List<CarImage> findByCarId(Long carId);

    // Find image by name
    Optional<CarImage> findByImageName(String imageName);

    // Check if car has images
    boolean existsByCarId(Long carId);

    // Count images for a car
    long countByCarId(Long carId);

    // Delete all images for a specific car (for cascade delete)
    @Modifying
    @Query("DELETE FROM CarImage ci WHERE ci.carId = :carId")
    void deleteByCarId(@Param("carId") Long carId);
}