package com.m2iAssurance.model;

import com.m2iAssurance.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users") // MongoDB collection name
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String phone;
    private String nationality;
    private String address;
    private String username;
    private String email;
    private String password;
    private Role role;
}
