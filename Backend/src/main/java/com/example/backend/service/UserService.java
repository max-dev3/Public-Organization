package com.example.backend.service;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found.");
        }
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User updatedUser) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found.");
        }

        User userToUpdate = userRepository.findById(id).get();
        userToUpdate.setUsername(updatedUser.getUsername());
        userToUpdate.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        userToUpdate.setEmail(updatedUser.getEmail());
        userToUpdate.setFirstName(updatedUser.getFirstName());
        userToUpdate.setLastName(updatedUser.getLastName());
        userToUpdate.setPhoneNumber(updatedUser.getPhoneNumber());
        userToUpdate.setUpdatedAt(new Date());

        return userRepository.save(userToUpdate);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public boolean verifyUser(String email, String rawPassword) {
        Optional<User> userOptional = getUserByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }
}
