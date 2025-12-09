package edu.icet.Service;

import edu.icet.Model.Dto.CarImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarImageService {

    // Upload single image for a car
    CarImageDto uploadImage(Long carId, MultipartFile file) throws IOException;

    // Upload multiple images for a car
    List<CarImageDto> uploadMultipleImages(Long carId, List<MultipartFile> files) throws IOException;

    // Get image by ID
    CarImageDto getImageById(Long carImageID);

    // Get all images for a car
    List<CarImageDto> getImagesByCarId(Long carId);

    // Delete image by ID
    void deleteImage(Long carImageID);

    // Delete all images for a car
    void deleteAllImagesByCarId(Long carId);

    // Get image data (for downloading/viewing)
    byte[] getImageData(Long carImageID);
}
