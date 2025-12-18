package edu.icet.Model.Dto;

import edu.icet.Model.Entity.Users;
import lombok.Data;

@Data
public class UserResponseDto {

    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String nic;
    private Users.Role role;


    public UserResponseDto (Long userId, String fullName, String email, String phoneNumber, String address, String nic, Users.Role role) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.nic = nic;
        this.role = role;

    }
}
