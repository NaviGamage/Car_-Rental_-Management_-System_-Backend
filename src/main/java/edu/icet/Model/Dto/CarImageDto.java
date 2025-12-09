package edu.icet.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarImageDto {

    private Long carImageId;
    private Long carId; // Car ID (Foreign Key)
    private String imageName;
    private String imageType;
    private byte[] imageData;
}
