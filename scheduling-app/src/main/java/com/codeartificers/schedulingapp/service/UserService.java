package com.codeartificers.schedulingapp.service;

import com.codeartificers.schedulingapp.model.Calendar;
import com.codeartificers.schedulingapp.model.User;
import com.codeartificers.schedulingapp.model.UserCounter;
import com.codeartificers.schedulingapp.repository.CalendarRepository;
import com.codeartificers.schedulingapp.repository.UserCounterRepository;
import com.codeartificers.schedulingapp.repository.UserRepository;
import com.codeartificers.schedulingapp.resource.UpdatePasswordRequest;
import com.codeartificers.schedulingapp.resource.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCounterRepository userCounterRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    private CalendarRepository calendarRepository;

    @Autowired
    public UserService(UserRepository userRepository, CalendarRepository calendarRepository) {
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
    }

    public User createUser(UserRequest userRequest){
        if (isValidUserRequest(userRequest)) {
            if(isUsernameUnique(userRequest.getUsername()) && isEmailUnique(userRequest.getEmail())){
                return saveUserToDatabase(userRequest);
            }
            else {
                throw new IllegalArgumentException("Username or email already exists.");
            }
        }
        else{
            throw new IllegalArgumentException("Malformed request. Missing required user fields");
        }
    }
    private User saveUserToDatabase(UserRequest userRequest) {
        UserCounter counter = userCounterRepository.findByName("user_id");
        if (counter == null) {
            counter = new UserCounter();
            counter.setName("user_id");
            counter.setSequence(1L); // Set an initial variable of 1.
        }
        long nextUserId = counter.getSequence() + 1;
        counter.setSequence(nextUserId);
        userCounterRepository.save(counter);

        User user = new User();
        user.setUser_id(String.valueOf(nextUserId));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setDob(userRequest.getDob());
        user.setUsername(userRequest.getUsername());

        //Hashing the password before saving it to the database
        String hashPassword = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(hashPassword);

        String token = jwtUtil.generateToken(user.getUsername(), userRequest.getUser_Id());
        user.setToken(token);

        return userRepository.save(user);
    }
    private boolean isValidUserRequest(UserRequest userRequest){
        return userRequest.getFirstName() != null && userRequest.getLastName() != null && userRequest.getEmail() != null &&
                userRequest.getDob() != null && userRequest.getUsername() != null && userRequest.getPassword() != null;
    }

    public User authenticateUser(String email, String password){
        User user = userRepository.findByEmail(email);

        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            String token = jwtUtil.generateToken(user.getUsername(), user.getUser_id());
            user.setToken(token);
            return user;
        }
        else{
            return null; // Authentication failed
        }
    }

    private boolean isUsernameUnique(String username){
        User existingUser = userRepository.findByUsername(username);
        return existingUser == null;
    }
    private boolean isEmailUnique(String email){
        User existingUser = userRepository.findByEmail(email);
        return existingUser == null;
    }

    public void updatePassword(String user_id, UpdatePasswordRequest passwordRequest){
        User existingUser = userRepository.findById(user_id).orElseThrow(() -> new NoSuchElementException("User not found"));

        if (passwordEncoder.matches(passwordRequest.getOldPassword(), existingUser.getPassword())) {
            String hashedNewPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
            existingUser.setPassword(hashedNewPassword);
            userRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("Incorrect original password");
        }

    }
    public boolean isUserIdValid(String userId){
        return userRepository.existsById(userId);
    }

    // Stores a list of calendarIds on the homepage
    public void addCalendarToHomePage(User user, String calendar_id) {
        try {
            List<String> userHomePageCalendars = user.getHomepageCalendars();

            // Check if the calendarId is not already in the list
            if (!userHomePageCalendars.contains(calendar_id)) {
                userHomePageCalendars.add(calendar_id);
                userRepository.save(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
