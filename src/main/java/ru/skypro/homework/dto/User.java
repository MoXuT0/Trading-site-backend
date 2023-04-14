package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String regDate;
    private String city;
    private String image;
}
