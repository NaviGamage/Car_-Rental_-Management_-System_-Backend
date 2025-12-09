package edu.icet.Service;

import edu.icet.Model.Dto.CarImageDto;
import edu.icet.Model.Entity.CarImage;
import edu.icet.Repository.CarDetailsRepository;
import edu.icet.Repository.CarImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarImageServiceImpl implements CarImageService{

    private final CarImageRepository carImageRepository;
    private final CarDetailsRepository carDetailsRepository;
    private final ModelMapper modelMapper;

    @Override
    public CarImageDto uploadImage(Long carId, MultipartFile file) throws IOException {
        // Verify car exists
        if (!carDetailsRepository.existsById(carId)) {
            throw new RuntimeException("Car not found with id: " + carId);
        }

        // Validate file
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Validate file type (only images)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Only image files are allowed");
        }

        // Create CarImage entity
        CarImage carImage = new CarImage();
        carImage.setCarId(carId);
        carImage.setImageName(file.getOriginalFilename());
        carImage.setImageType(file.getContentType());
        carImage.setImageData(file.getBytes());

        // Save to database
        CarImage savedImage = carImageRepository.save(carImage);

        return modelMapper.map(savedImage, CarImageDto.class);
    }

    @Override
    public List<CarImageDto> uploadMultipleImages(Long carId, List<MultipartFile> files) throws IOException {
        if (!carDetailsRepository.existsById(carId)) {
            throw new RuntimeException("Car not found with id: " + carId);
        }

        List<CarImageDto> uploadedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                CarImageDto uploadedImage = uploadImage(carId, file);
                uploadedImages.add(uploadedImage);
            }
        }

        return uploadedImages;
    }

    @Override
    public CarImageDto getImageById(Long carImageID) {
        CarImage carImage = carImageRepository.findById(carImageID).orElseThrow(() -> new RuntimeException("Image not found with id: " + carImageID));
        return modelMapper.map(carImage, CarImageDto.class);
    }

    @Override
    public List<CarImageDto> getImagesByCarId(Long carId) {
        List<CarImage> images = carImageRepository.findByCarId(carId);
        return images.stream().map(image -> modelMapper.map(image, CarImageDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteImage(Long carImageID) {
        if (!carImageRepository.existsById(carImageID)) {throw new RuntimeException("Image not found with id: " + carImageID);}
        carImageRepository.deleteById(carImageID);
    }

    @Override
    @Transactional
    public void deleteAllImagesByCarId(Long carId) {
        carImageRepository.deleteByCarId(carId);
    }

    @Override
    public byte[] getImageData(Long carImageID) {
        CarImage carImage = carImageRepository.findById(carImageID)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + carImageID));
        return carImage.getImageData();
    }
}
