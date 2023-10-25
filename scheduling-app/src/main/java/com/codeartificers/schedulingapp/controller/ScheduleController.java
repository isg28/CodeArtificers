package com.codeartificers.schedulingapp.controller;

import com.codeartificers.schedulingapp.model.Availability;
import com.codeartificers.schedulingapp.model.AvailabilityCounter;
import com.codeartificers.schedulingapp.model.User;
import com.codeartificers.schedulingapp.model.UserCounter;
import com.codeartificers.schedulingapp.repository.*;
import com.codeartificers.schedulingapp.resource.AvailabilityRequest;
import com.codeartificers.schedulingapp.resource.MeetingRequest;
import com.codeartificers.schedulingapp.resource.UserRequest;
import com.codeartificers.schedulingapp.model.Meeting;
import com.codeartificers.schedulingapp.model.MeetingCounter;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
class ScheduleController {

    @Autowired
    private MongoTemplate mongoTemplate;

    //We will delete later, but this is to check whether you connected
    //successfully with MongoDB via local host
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
    private final MeetingRepository meetingRepository;
    private final MeetingCounterRepository meetingCounterRepository;

    @Autowired
    public ScheduleController(UserRepository userRepository, AvailabilityRepository availabilityRepository, UserCounterRepository userCounterRepository, AvailabilityCounterRepository availabilityCounterRepository1, MeetingRepository meetingRepository,MeetingCounterRepository meetingCounterRepository) {
        this.userRepository = userRepository;
        this.availabilityRepository = availabilityRepository;
        this.userCounterRepository = userCounterRepository;
        this.availabilityCounterRepository = availabilityCounterRepository1;
        this.meetingRepository = meetingRepository;
        this.meetingCounterRepository = meetingCounterRepository;
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
            //error case if the JSON request is invalid for any of the User's data fields
            if(userRequest.getName() == null && userRequest.getEmail() == null && userRequest.getDob() == null && userRequest.getUsername() == null){
                return ResponseEntity.status(404).body("Malformed request. Missing required user fields.");
            }

            //saving the updated profile to the DB
            userRepository.save(existingProfile);
            return ResponseEntity.status(200).body(existingProfile);
        } else {
            return ResponseEntity.status(404).body("User " + user_id + " does not exist.");
        }
    }
    
    //DELETE: deletes user, Oscar
    @DeleteMapping("/api/user/{user_id}")
    public ResponseEntity delete_UserProfile(@PathVariable String user_id)
    {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        if (userProfile.isPresent())
        {
            this.userRepository.deleteById(user_id);

            return ResponseEntity.status(200).body("User " + user_id + " has been deleted");
        }
    return ResponseEntity.status(404).build();
    }

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
    @PutMapping ("/api/user/{user_id}/availability/{availability_id}")
    public ResponseEntity edit_availabilityEntry(@RequestBody AvailabilityRequest availabilityRequest, @PathVariable String user_id, @PathVariable String availability_id){
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Availability> availability = this.availabilityRepository.findById(availability_id);

        if(userProfile.isPresent() && availability.isPresent()) {
            Availability existingAvailability = availability.get();

            if (availabilityRequest.getTime() != null) {
                existingAvailability.setTime(availabilityRequest.getTime());
            }
            if (availabilityRequest.getDays() != null) {
                existingAvailability.setDays(availabilityRequest.getDays());
            }
            if(availabilityRequest.getTime() == null && availabilityRequest.getDays() == null){
                return ResponseEntity.status(404).body("Malformed request. Missing required availability fields.");

            }

            availabilityRepository.save(existingAvailability);
            //userRepository.save(userAvailability);
            return ResponseEntity.status(200).body(existingAvailability);

        } else {
            return ResponseEntity.status(404).body("Availability " + availability_id + " for User " + user_id + " does not exist.");

        }
    }


    //DELETE: Delete an available entry, Isabel
    @DeleteMapping ("/api/user/{user_id}/availability/{availability_id}")
    public ResponseEntity deleteAvailabilityEntry(@PathVariable String user_id, @PathVariable String availability_id){
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Availability> availabilityEntry = this.availabilityRepository.findById(availability_id);

        if(userProfile.isPresent() && availabilityEntry.isPresent()){
            Availability deletedEntry = availabilityEntry.get();
            this.availabilityRepository.deleteById(availability_id);
            String errorMessage = "Availability entry with ID " + deletedEntry.getAvailability_Id() + " has been deleted";
            return ResponseEntity.status(200).body(errorMessage);
        }
        return ResponseEntity.status(404).body("Availability entry " + availability_id + " not found for User " + user_id);
    }


    //// ********************* MEETING MANAGEMENT ENDPOINTS ***********************************
    //POST: Create a new meeting, Danica
    @PostMapping("/api/meeting")
    public ResponseEntity<Meeting> createMeeting(@RequestBody MeetingRequest meetingRequest){
        MeetingCounter counter = meetingCounterRepository.findByName("meeting_id");
        if(counter == null){
            counter = new MeetingCounter();
            counter.setName("meeting_id");
            counter.setSequence(1L); //sets initial variable to 1
        }
        long nextMeetingId = counter.getSequence() + 1;
        counter.setSequence(nextMeetingId);
        meetingCounterRepository.save(counter);

        Meeting meeting = new Meeting();
        meeting.setMeeting_id(String.valueOf(nextMeetingId));
        meeting.setDay(meetingRequest.getDay());
        meeting.setTime(meetingRequest.getTime());
        meeting.setParticipants(meetingRequest.getParticipants());
        meeting.setMeeting_Description(meetingRequest.getMeeting_Description());

        return ResponseEntity.status(201).body(this.meetingRepository.save(meeting));

    }


    //GET: Retrieve details about a specific meeting, Oscar


    //GET: Retrieve a list of all available meetings based on user's availability, Isabel
    @GetMapping ("/api/meeting")
    public ResponseEntity<List<Meeting>> getAllMeetingData(){
        return ResponseEntity.status(200).body(this.meetingRepository.findAll());
    }

    //PUT: Update an existing meeting (add, remove participants), Mansoor


    //DELETE: delete a meeting, Brandon


    //// ********************* SEARCH FOR AVAILABLE TIMESLOTS ENDPOINTS ***********************************
    //GET: Retrieve available timeslots for scheduling a meeting with one or more users


    //// ********************* USER REGISTRATION AND AUTHENTICATION ENDPOINTS ***********************************


    //// ********************* INVITATION AND NOTIFICATION ENDPOINTS ***********************************



}