package com.codeartificers.schedulingapp.controller;

import com.codeartificers.schedulingapp.model.Counter;
import com.codeartificers.schedulingapp.model.Schedules;
import com.codeartificers.schedulingapp.repository.CounterRepository;
import com.codeartificers.schedulingapp.repository.ScheduleRepository;
import com.codeartificers.schedulingapp.resource.AvailabilityRequest;
import com.codeartificers.schedulingapp.resource.UserRequest;
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

    private final ScheduleRepository scheduleRepository;
    private final CounterRepository counterRepository;

    //private final UserCounterRepository userCounterRepository;
    @Autowired
    public ScheduleController(ScheduleRepository scheduleRepository, CounterRepository counterRepository) {
        this.scheduleRepository = scheduleRepository;
        this.counterRepository = counterRepository;
        //this.userCounterRepository = userCounterRepository;
    }


    // ********************* USER MANAGEMENT ENDPOINTS ***********************************

    // GET: Retrieve all user data, Danica
    @GetMapping("/api/user")
    public ResponseEntity<List<Schedules>> getAllUserData() {
        return ResponseEntity.status(200).body(this.scheduleRepository.findAll());
    }

    //POST: Create a new user, Danica and Isabel
    @PostMapping("/api/user")
    public ResponseEntity<Schedules> createSchedule(@RequestBody UserRequest userRequest) {

        // ISABEL PLS FIX THIS LOL its telling me to make findByName static which is weird (10/18/23) - Danica

        /*UserCounter counter = UserCounterRepository.findByName("user_id");
        if(counter == null){
            counter = new UserCounter();
            counter.setName("user_id");
            counter.setSequence(1L); // Set an initial variable of 1.
        }
        long nextUserId = counter.getSequence() + 1;
        counter.setSequence(nextUserId);
        UserCounterRepository.save(counter);*/

        //Retrieve and increment the counter (this is the counter that works)
        Counter counter = counterRepository.findByName("user_id");
        if(counter == null){
            counter = new Counter();
            counter.setName("user_id");
            counter.setSequence(1L); // Set an initial variable of 1.
        }
        long nextUserId = counter.getSequence() + 1;
        counter.setSequence(nextUserId);
        counterRepository.save(counter);

        Schedules schedule = new Schedules();
        schedule.setUser_id(String.valueOf(nextUserId));
        schedule.setName(userRequest.getName());
        schedule.setEmail(userRequest.getEmail());
        schedule.setDob(userRequest.getDob());
        schedule.setUsername(userRequest.getUsername());

        return ResponseEntity.status(201).body(this.scheduleRepository.save(schedule));

    }
    //GET: Retrieve user profile information, Mansoor

    //PUT: Edit user info, Isabel
    @PutMapping("/api/user/{user_id}")
    public ResponseEntity edit_UserProfile(@RequestBody UserRequest userRequest, @PathVariable String user_id) {
        Optional<Schedules> userProfile = this.scheduleRepository.findById(user_id);

        if (userProfile.isPresent()) {
            Schedules existingProfile = userProfile.get();

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
            scheduleRepository.save(existingProfile);
            return ResponseEntity.status(200).body(existingProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //********************* AVAILABILITY MANAGEMENT ENDPOINTS ***********************************
    //POST: Create a new availability entry for a user, Danica
    @PostMapping("/api/user/{user_id}/availability")
    public ResponseEntity<Schedules> createNewAvailability(@RequestBody UserRequest userRequest, @PathVariable String user_id, @RequestBody AvailabilityRequest availabilityRequest) {
        //this is a work in progress, we still need to fix/ make individual counters for Availability, User, etc (10/18/23) - Danica
        /*Counter counter = counterRepository.findByName("user_id");
        if(counter == null){
            counter = new Counter();
            counter.setName("user_id");
            counter.setSequence(1L); // Set an initial variable of 1.
        }
        long nextUserId = counter.getSequence() + 1;
        counter.setSequence(nextUserId);
        counterRepository.save(counter);

        Schedules schedule = new Schedules();
        schedule.setDays(availabilityRequest.getDays());
        schedule.setTime(availabilityRequest.getTime());*/
        return null;

    }


    //GET: Retrieve all availabilities for a user (useful for showing your own availability), Brandon

    //PUT: Update an existing availability entry

    //DELETE: Delete an available entry

    //// ********************* MEETING MANAGEMENT ENDPOINTS ***********************************



}