package com.codeartificers.schedulingapp.controller;

import com.codeartificers.schedulingapp.model.Availability;
import com.codeartificers.schedulingapp.model.AvailabilityCounter;
import com.codeartificers.schedulingapp.model.User;
import com.codeartificers.schedulingapp.model.UserCounter;
import com.codeartificers.schedulingapp.repository.AvailabilityCounterRepository;
import com.codeartificers.schedulingapp.repository.AvailabilityRepository;
import com.codeartificers.schedulingapp.repository.UserRepository;
import com.codeartificers.schedulingapp.repository.UserCounterRepository;
import com.codeartificers.schedulingapp.resource.AvailabilityRequest;
import com.codeartificers.schedulingapp.resource.UserRequest;
import com.codeartificers.schedulingapp.model.Meeting;
import com.codeartificers.schedulingapp.model.MeetingCounter;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

//We Will delete later, but this is to check whether you connected
//successfully with MongoDB via local host
@RestController
class ScheduleController {

    @Autowired
    private MongoTemplate mongoTemplate;


    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        try {
            mongoTemplate.getDb().runCommand(new Document("ping", 1));
            return ResponseEntity.ok("MongoDB is UP");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("MongoDB is DOWN");
        }
    }

    private final UserRepository userRepository;
    private final AvailabilityRepository availabilityRepository;
    private final UserCounterRepository userCounterRepository;
    private final AvailabilityCounterRepository availabilityCounterRepository;
    @Autowired
    public ScheduleController(UserRepository userRepository, AvailabilityRepository availabilityRepository, UserCounterRepository userCounterRepository, AvailabilityCounterRepository availabilityCounterRepository1) {
        this.userRepository = userRepository;
        this.availabilityRepository = availabilityRepository;
        this.userCounterRepository = userCounterRepository;
        this.availabilityCounterRepository = availabilityCounterRepository1;
    }


    // ********************* USER MANAGEMENT ENDPOINTS ***********************************

    // GET: Retrieve all user data, Danica
    @GetMapping("/api/user")
    public ResponseEntity<List<User>> getAllUserData() {
        return ResponseEntity.status(200).body(this.userRepository.findAll());
    }

    //POST: Create a new user, Danica and Isabel
    @PostMapping("/api/user")
    public ResponseEntity<User> createSchedule(@RequestBody UserRequest userRequest) {

        UserCounter counter = userCounterRepository.findByName("user_id");
        if(counter == null){
            counter = new UserCounter();
            counter.setName("user_id");
            counter.setSequence(1L); // Set an initial variable of 1.
        }
        long nextUserId = counter.getSequence() + 1;
        counter.setSequence(nextUserId);
        userCounterRepository.save(counter);


        User user = new User();
        user.setUser_id(String.valueOf(nextUserId));
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setDob(userRequest.getDob());
        user.setUsername(userRequest.getUsername());

        return ResponseEntity.status(201).body(this.userRepository.save(user));

    }
    //GET: Retrieve user profile information, Mansoor
    @GetMapping("/api/user/{user_id}")
    public ResponseEntity<User> get_UserProfile(@PathVariable String user_id){
        Optional<User> userProfile = this.userRepository.findById(user_id);
        // checks if the user profile based on id is available else print error code.
        if (userProfile.isPresent()) {
            return ResponseEntity.status(200).body(userProfile.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    //PUT: Edit user info, Isabel
    @PutMapping("/api/user/{user_id}")
    public ResponseEntity edit_UserProfile(@RequestBody UserRequest userRequest, @PathVariable String user_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);

        if (userProfile.isPresent()) {
            User existingProfile = userProfile.get();

            //Updated the user's profile data with the new values, doesn't have to provide new values for each component
            if(userRequest.getName() != null){
                existingProfile.setName(userRequest.getName());
            }
            if(userRequest.getEmail() != null){
                existingProfile.setEmail(userRequest.getEmail());
            }
            if(userRequest.getDob() != null){
                existingProfile.setDob(userRequest.getDob());
            }
            if(userRequest.getUsername() != null){
                existingProfile.setUsername(userRequest.getUsername());
            }

            //saving the updated profile to the DB
            userRepository.save(existingProfile);
            return ResponseEntity.status(200).body(existingProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //DELETE: deletes user, Oscar

    //********************* AVAILABILITY MANAGEMENT ENDPOINTS ***********************************
    //POST: Create a new availability entry for a user, Danica
    @PostMapping("/api/user/{user_id}/availability")
    public ResponseEntity<Availability> createNewAvailability(@PathVariable String user_id, @RequestBody AvailabilityRequest availabilityRequest) {
        AvailabilityCounter availabilityCounter = availabilityCounterRepository.findByName("availability_id");
        //To ensure that the availabilityCounter remains consistent. If empty, starts at 0 then increments.
        if(availabilityCounter == null){
            availabilityCounter = new AvailabilityCounter();
            availabilityCounter.setName("availability_id");
            availabilityCounter.setSequence(1L); //sets initial variable to 1
        }
        long nextAvailabilityId = availabilityCounter.getSequence() + 1;
        availabilityCounter.setSequence(nextAvailabilityId);
        availabilityCounterRepository.save(availabilityCounter);

        Availability availability = new Availability();
        availability.setAvailability_Id(String.valueOf(nextAvailabilityId));
        availability.setUser_id(user_id);
        availability.setDays(availabilityRequest.getDays());
        availability.setTime(availabilityRequest.getTime());

        return ResponseEntity.status(201).body(this.availabilityRepository.save(availability));

    }


    //GET: Retrieve all availabilities for a user (useful for showing your own availability), Brandon


    //PUT: Update an existing availability entry, Danica


    //DELETE: Delete an available entry


    //// ********************* MEETING MANAGEMENT ENDPOINTS ***********************************
    //POST: Create a new meeting, Danica


    //GET: Retrieve details about a specific meeting


    //GET: Retrieve a list of all available meetings based on user's availability


    //PUT: Update an existing meeting (add, remove participants)


    //DELETE: delete a meeting


    //// ********************* SEARCH FOR AVAILABLE TIMESLOTS ENDPOINTS ***********************************
    //GET: Retrieve available timeslots for scheduling a meeting with one or more users


    //// ********************* USER REGISTRATION AND AUTHENTICATION ENDPOINTS ***********************************


    //// ********************* INVITATION AND NOTIFICATION ENDPOINTS ***********************************



}