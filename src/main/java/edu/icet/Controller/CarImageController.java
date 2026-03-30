package edu.icet.Controller;

import edu.icet.Model.Dto.CarImageDto;
import edu.icet.Service.CarImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/car-images")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CarImageController {

    private final CarImageService carImageService;


    @PostMapping("/upload/{carId}")
    public ResponseEntity<CarImageDto> uploadImage(
            @PathVariable Long carId,
            @RequestParam("file") MultipartFile file) throws IOException {
        CarImageDto uploadedImage = carImageService.uploadImage(carId, file);
        return new ResponseEntity<>(uploadedImage, HttpStatus.CREATED);
    }

    // Upload multiple images for a car
    @PostMapping("/upload-multiple/{carId}")
    public ResponseEntity<List<CarImageDto>> uploadMultipleImages(
            @PathVariable Long carId,
            @RequestParam("files") List<MultipartFile> files) throws IOException {
        List<CarImageDto> uploadedImages = carImageService.uploadMultipleImages(carId, files);
        return new ResponseEntity<>(uploadedImages, HttpStatus.CREATED);
    }

    // Get all images for a car (without image data - metadata only)
    @GetMapping("/car/{carId}")
    public ResponseEntity<List<CarImageDto>> getImagesByCarId(@PathVariable Long carId) {
        List<CarImageDto> images = carImageService.getImagesByCarId(carId);
        // Remove image data to reduce response size
        images.forEach(img -> img.setImageData(null));
        return ResponseEntity.ok(images);
    }

    // Get image by ID (metadata only)
    @GetMapping("/{imageId}")
    public ResponseEntity<CarImageDto> getImageById(@PathVariable Long imageId) {
        CarImageDto image = carImageService.getImageById(imageId);
        image.setImageData(null); // Remove image data
        return ResponseEntity.ok(image);
    }

    // Download/View image by ID
    @GetMapping("/download/{imageId}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long imageId) {
        CarImageDto image = carImageService.getImageById(imageId);
        byte[] imageData = carImageService.getImageData(imageId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getImageType()));
        headers.setContentDispositionFormData("attachment", image.getImageName());

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    // View image in browser
    @GetMapping("/view/{imageId}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long imageId) {
        CarImageDto image = carImageService.getImageById(imageId);
        byte[] imageData = carImageService.getImageData(imageId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getImageType()));

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    // Delete image by ID
    @DeleteMapping("/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId) {
        carImageService.deleteImage(imageId);
        return ResponseEntity.ok("Image deleted successfully");
    }

    // Delete all images for a car
    @DeleteMapping("/car/{carId}")
    public ResponseEntity<String> deleteAllImagesByCarId(@PathVariable Long carId) {
        carImageService.deleteAllImagesByCarId(carId);
        return ResponseEntity.ok("All images deleted successfully for car id: " + carId);
    }

}
