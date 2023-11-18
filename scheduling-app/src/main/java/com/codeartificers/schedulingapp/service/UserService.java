package com.codeartificers.schedulingapp.service;

import com.codeartificers.schedulingapp.model.User;
import com.codeartificers.schedulingapp.model.UserCounter;
import com.codeartificers.schedulingapp.repository.UserCounterRepository;
import com.codeartificers.schedulingapp.repository.UserRepository;
import com.codeartificers.schedulingapp.resource.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCounterRepository userCounterRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User createUser(UserRequest userRequest){
        if (isValidUserRequest(userRequest)) {
            return saveUserToDatabase(userRequest);
        }else{
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

        return userRepository.save(user);
    }
    private boolean isValidUserRequest(UserRequest userRequest){
        return userRequest.getFirstName() != null && userRequest.getLastName() != null && userRequest.getEmail() != null &&
                userRequest.getDob() != null && userRequest.getUsername() != null && userRequest.getPassword() != null;
    }

    public User authenticateUser(String email, String password){
        User user = userRepository.findByEmail(email);

        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            return user;
        }
        else{
            return null; // Authentication failed
        }
    }


}
