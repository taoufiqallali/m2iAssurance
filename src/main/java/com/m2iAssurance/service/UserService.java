package com.m2iAssurance.service;


import com.m2iAssurance.enums.Role;
import com.m2iAssurance.model.Policy;
import com.m2iAssurance.model.User;
import com.m2iAssurance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    public void saveUser(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already used!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public User updateUser(String username, User updatedUser) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Update user fields with the new values
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setNationality(updatedUser.getNationality());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Handle password carefully (e.g., hashing)

        userRepository.save(existingUser); // Save the updated user back to the database
        return existingUser;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }


    public Page<User> getRecentUsers(){
        return userRepository.findAll(PageRequest.of(0, 4));
    }

    public void deleteById(String id){
        userRepository.deleteById(id);
    }

}
