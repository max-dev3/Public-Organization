package com.example.backend.service;

import com.example.backend.exception.InvalidInputException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Optional<User> getUserById(Long id)  {
        if (!existsById(id)) {
            throw new InvalidInputException("User with id " + id + " not found.");
        }
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        validateUser(user);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id)  {
        if (!existsById(id)) {
            throw new InvalidInputException("User with id " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User updatedUser)  {
        if (!existsById(id)) {
            throw new InvalidInputException("User with id " + id + " not found.");
        }

        User userToUpdate = userRepository.findById(id).get();

        if (!userToUpdate.getEmail().equals(updatedUser.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(updatedUser.getEmail());
            if (existingUser.isPresent()) {
                throw new InvalidInputException("Email " + updatedUser.getEmail() + " already exists");
            }
            userToUpdate.setEmail(updatedUser.getEmail());
        }

        if (!userToUpdate.getUsername().equals(updatedUser.getUsername())) {
            Optional<User> existingUser = userRepository.findByUsername(updatedUser.getUsername());
            if (existingUser.isPresent()) {
                throw new InvalidInputException("Username " + updatedUser.getUsername() + " already exists");
            }
            userToUpdate.setUsername(updatedUser.getUsername());
        }

        if (!userToUpdate.getPhoneNumber().equals(updatedUser.getPhoneNumber())) {
            Optional<User> existingUser = userRepository.findByPhoneNumber(updatedUser.getPhoneNumber());
            if (existingUser.isPresent()) {
                throw new InvalidInputException("Phone number " + updatedUser.getPhoneNumber() + " already exists");
            }
            if (!isValidPhoneNumber(updatedUser.getPhoneNumber())) {
                throw new InvalidInputException("Invalid phone number format: " + updatedUser.getPhoneNumber());
            }
            userToUpdate.setPhoneNumber(updatedUser.getPhoneNumber());
        }

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
    public static boolean isValidEmail(String email) {
        String emailRegex = "[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Перевіряємо, чи введений номер телефону має відповідний формат
        Pattern pattern = Pattern.compile("\\+38[0-9]{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public void validateUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new InvalidInputException("Username " + user.getUsername() + " already exists");
        }

        existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new InvalidInputException("Email " + user.getEmail() + " already exists");
        }

        existingUser = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (existingUser.isPresent()) {
            throw new InvalidInputException("Phone number " + user.getPhoneNumber() + " already exists");
        }

        if (user.getUsername().length() < 6) {
            throw new InvalidInputException("Username must be at least 6 characters long");
        }

        if (!isValidEmail(user.getEmail())) {
            throw new InvalidInputException("Invalid email");
        }

        if (!isValidPhoneNumber(user.getPhoneNumber())) {
            throw new InvalidInputException("Invalid phone number format: " + user.getPhoneNumber());
        }
    }
}
