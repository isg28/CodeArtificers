package com.codeartificers.schedulingapp.controller;

import com.codeartificers.schedulingapp.model.*;
import com.codeartificers.schedulingapp.repository.*;
import com.codeartificers.schedulingapp.resource.AvailabilityRequest;
import com.codeartificers.schedulingapp.resource.MeetingRequest;
import com.codeartificers.schedulingapp.resource.TimeSlotRequest;
import com.codeartificers.schedulingapp.resource.UserRequest;
import com.codeartificers.schedulingapp.service.TimeSlotService;
import com.codeartificers.schedulingapp.service.TokenUtil;
import com.codeartificers.schedulingapp.service.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.coyote.Response;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
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

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AvailabilityRepository availabilityRepository;
    @Autowired
    private final UserCounterRepository userCounterRepository;
    @Autowired
    private final AvailabilityCounterRepository availabilityCounterRepository;
    @Autowired
    private final MeetingRepository meetingRepository;
    @Autowired
    private final MeetingCounterRepository meetingCounterRepository;
    @Autowired
    private final TimeSlotRepository timeSlotRepository;
    @Autowired
    private TimeSlotService timeSlotService;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ScheduleController(UserRepository userRepository, AvailabilityRepository availabilityRepository, UserCounterRepository userCounterRepository, AvailabilityCounterRepository availabilityCounterRepository1,
                              MeetingRepository meetingRepository, MeetingCounterRepository meetingCounterRepository, TimeSlotRepository timeSlotRepository, TimeSlotService timeSlotService, UserService userService) {
        this.userRepository = userRepository;
        this.availabilityRepository = availabilityRepository;
        this.userCounterRepository = userCounterRepository;
        this.availabilityCounterRepository = availabilityCounterRepository1;
        this.meetingRepository = meetingRepository;
        this.meetingCounterRepository = meetingCounterRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotService = timeSlotService;
        this.userService = userService;
    }


    // ********************* USER MANAGEMENT ENDPOINTS ***********************************

    // GET: Retrieve all user data, Danica
    @GetMapping("/api/user")
    public ResponseEntity<List<User>> getAllUserData() {
        return ResponseEntity.status(200).body(this.userRepository.findAll());
    }

    //POST: Create a new user, Danica and Isabel
    @PostMapping("/api/user")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        try{
            User newUser = userService.createUser(userRequest);
            return ResponseEntity.status(201).body(newUser);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }

    //GET: Retrieve user profile information, Mansoor
    @GetMapping("/api/user/{user_id}")
    public ResponseEntity<?> get_UserProfile(@PathVariable String user_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        // checks if the user profile based on id is available else print error code.
        if (userProfile.isPresent()) {
            return ResponseEntity.status(200).body(userProfile.get());
        } else {
            return ResponseEntity.status(404).body("User " + user_id + " does not exist.");
        }
    }

    //PUT: Edit user info, Isabel
    @PutMapping("/api/user/{user_id}")
    public ResponseEntity<?> edit_UserProfile(@RequestBody UserRequest userRequest, @PathVariable String user_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);

        if (userProfile.isPresent()) {
            User existingProfile = userProfile.get();

            //Updated the user's profile data with the new values, doesn't have to provide new values for each component
            if (userRequest.getFirstName() != null) {
                existingProfile.setFirstName(userRequest.getFirstName());
            }
            if (userRequest.getLastName() != null) {
                existingProfile.setLastName(userRequest.getLastName());
            }
            if (userRequest.getEmail() != null) {
                existingProfile.setEmail(userRequest.getEmail());
            }
            if (userRequest.getDob() != null) {
                existingProfile.setDob(userRequest.getDob());
            }
            if (userRequest.getUsername() != null) {
                existingProfile.setUsername(userRequest.getUsername());
            }

            //error case handling if the JSON request is invalid for any of the User's data fields
            if (userRequest.getFirstName() == null && userRequest.getLastName() == null && userRequest.getEmail() == null
                    && userRequest.getDob() == null && userRequest.getUsername() == null) {
                return ResponseEntity.status(400).body("Malformed request. Missing required user fields.");
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
    public ResponseEntity delete_UserProfile(@PathVariable String user_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        if (userProfile.isPresent()) {
            this.userRepository.deleteById(user_id);

            return ResponseEntity.status(200).body("User " + user_id + " has been deleted");
        }
        return ResponseEntity.status(404).build();
    }

    //********************* AVAILABILITY MANAGEMENT ENDPOINTS ***********************************
    //POST: Create a new availability entry for a user, Danica
    @PostMapping("/api/user/{user_id}/availability")
    public ResponseEntity<?> createNewAvailability(@PathVariable String user_id, @RequestBody AvailabilityRequest availabilityRequest) {
        AvailabilityCounter availabilityCounter = availabilityCounterRepository.findByName("availability_id");

        if (availabilityRequest.getUser_id() != null && availabilityRequest.getDate() != null
                && availabilityRequest.getStartTime() != null && availabilityRequest.getEndTime() != null && availabilityRequest.getTitle() != null) {
            //To ensure that the availabilityCounter remains consistent. If empty, starts at 0 then increments.
            if (availabilityCounter == null) {
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
            availability.setDate(availabilityRequest.getDate());
            availability.setStartTime(availabilityRequest.getStartTime());
            availability.setEndTime(availabilityRequest.getEndTime());
            availability.setTitle(availabilityRequest.getTitle());

            return ResponseEntity.status(201).body(this.availabilityRepository.save(availability));
        } else {
            return ResponseEntity.status(400).body("Malformed request. Missing required user fields.");
        }

    }


    //GET: Retrieve all availabilities for a user (useful for showing your own availability), Brandon
    @GetMapping("/api/user/{user_id}/availability")
    public ResponseEntity<?> getAllAvailability(@RequestBody AvailabilityRequest availabilityRequest, @PathVariable String user_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        if (!userProfile.isPresent()) {
            return ResponseEntity.status(404).body("User ID: " + user_id + " not found");
        }
        List<Availability> allAvailabilities = this.availabilityRepository.findAll();
        List<Availability> userAvailabilities = new ArrayList<>();

        for (Availability availability : allAvailabilities) {
            if (availability.getUser_id().equals(user_id)) {
                userAvailabilities.add(availability);
            }
        }
        if (userAvailabilities.isEmpty()) {
            return ResponseEntity.status(404).body("There is no availability entry for User: " + user_id);
        }
        return ResponseEntity.status(200).body(userAvailabilities);
    }


    //PUT: Update an existing availability entry, Danica
    @PutMapping("/api/user/{user_id}/availability/{availability_id}")
    public ResponseEntity edit_availabilityEntry(@RequestBody AvailabilityRequest availabilityRequest, @PathVariable String user_id, @PathVariable String availability_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Availability> availability = this.availabilityRepository.findById(availability_id);

        if (userProfile.isPresent() && availability.isPresent()) {
            Availability existingAvailability = availability.get();

            if (availabilityRequest.getStartTime() != null) {
                existingAvailability.setStartTime(availabilityRequest.getStartTime());
            }
            if (availabilityRequest.getEndTime() != null) {
                existingAvailability.setEndTime(availabilityRequest.getEndTime());
            }
            if (availabilityRequest.getDate() != null) {
                existingAvailability.setDate(availabilityRequest.getDate());
            }
            if(availabilityRequest.getTitle() != null){
                existingAvailability.setTitle(availabilityRequest.getTitle());
            }
            if (availabilityRequest.getStartTime() == null && availabilityRequest.getDate() == null &&
                    availabilityRequest.getEndTime() == null && availabilityRequest.getTitle() == null) {
                return ResponseEntity.status(400).body("Malformed request. Missing required availability fields.");

            }

            availabilityRepository.save(existingAvailability);
            return ResponseEntity.status(200).body(existingAvailability);

        } else {
            return ResponseEntity.status(404).body("Availability " + availability_id + " for User " + user_id + " does not exist.");

        }
    }


    //DELETE: Delete an available entry, Isabel
    @DeleteMapping("/api/user/{user_id}/availability/{availability_id}")
    public ResponseEntity<?> deleteAvailabilityEntry(@PathVariable String user_id, @PathVariable String availability_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Availability> availabilityEntry = this.availabilityRepository.findById(availability_id);

        if (userProfile.isPresent() && availabilityEntry.isPresent()) {
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
    public ResponseEntity<?> createMeeting(@RequestBody MeetingRequest meetingRequest) {
        MeetingCounter counter = meetingCounterRepository.findByName("meeting_id");

        if (meetingRequest.getDate() != null && meetingRequest.getStartTime() != null && meetingRequest.getEndTime() != null && meetingRequest.getParticipants() != null && meetingRequest.getLocation() != null && meetingRequest.getMeeting_Description() != null) {
            if (counter == null) {
                counter = new MeetingCounter();
                counter.setName("meeting_id");
                counter.setSequence(1L); //sets initial variable to 1
            }
            long nextMeetingId = counter.getSequence() + 1;
            counter.setSequence(nextMeetingId);
            meetingCounterRepository.save(counter);

            Meeting meeting = new Meeting();
            meeting.setMeeting_id(String.valueOf(nextMeetingId));
            meeting.setDate(meetingRequest.getDate());
            meeting.setStartTime(meetingRequest.getStartTime());
            meeting.setEndTime(meetingRequest.getEndTime());
            meeting.setParticipants(meetingRequest.getParticipants());
            meeting.setLocation(meetingRequest.getLocation());
            meeting.setMeeting_Description(meetingRequest.getMeeting_Description());

            return ResponseEntity.status(201).body(this.meetingRepository.save(meeting));
        } else {
            return ResponseEntity.status(400).body("Malformed request. Missing required user data fields.");
        }

    }


    //GET: Retrieve details about a specific meeting, Oscar
    @GetMapping("/api/meeting/{meeting_id}")
    public ResponseEntity<Meeting> getMeetingDetails(@PathVariable String meeting_id) {
        Optional<Meeting> meetingData = this.meetingRepository.findById(meeting_id); // Checks for any meetings that were made
        if (meetingData.isPresent()) {
            return ResponseEntity.status(200).body(meetingData.get()); // returns meeting details
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //GET: Retrieve a list of all available meetings based on user's availability, Isabel
    @GetMapping("/api/meeting")
    public ResponseEntity<List<Meeting>> getAllMeetingData() {
        return ResponseEntity.status(200).body(this.meetingRepository.findAll());
    }

    //PUT: Update an existing meeting (add, remove participants), Mansoor
    @PutMapping("/api/meeting/{meeting_id}")
    public ResponseEntity update_meeting(@RequestBody MeetingRequest meetingRequest, @PathVariable String meeting_id) {
        Optional<Meeting> meetingProfile = this.meetingRepository.findById(meeting_id);
        if (meetingProfile.isPresent()) {
            Meeting existingProfile = meetingProfile.get();

            //Edit meeting information based on JSON request for each meeting data fields
            if (meetingRequest.getMeeting_id() != null) {
                existingProfile.setMeeting_id(meetingRequest.getMeeting_id());
            }
            if (meetingRequest.getStartTime() != null) {
                existingProfile.setStartTime(meetingRequest.getStartTime());
            }
            if (meetingRequest.getEndTime() != null) {
                existingProfile.setEndTime(meetingRequest.getEndTime());
            }
            if (meetingRequest.getDate() != null) {
                existingProfile.setDate(meetingRequest.getDate());
            }
            if (meetingRequest.getParticipants() != null) {
                existingProfile.setParticipants(meetingRequest.getParticipants());
            }
            if (meetingRequest.getLocation() != null) {
                existingProfile.setLocation(meetingRequest.getLocation());
            }
            if (meetingRequest.getMeeting_Description() != null) {
                existingProfile.setMeeting_Description(meetingRequest.getMeeting_Description());
            }
            //error check for invalid JSON request format
            if (meetingRequest.getMeeting_id() == null && meetingRequest.getStartTime() == null && meetingRequest.getEndTime() == null && meetingRequest.getDate()
                    == null && meetingRequest.getParticipants() == null && meetingRequest.getLocation() == null && meetingRequest.getMeeting_Description() == null) {
                return ResponseEntity.status(400).body("Malformed request. Missing required user fields.");
            }

            meetingRepository.save(existingProfile);//store in DB
            return ResponseEntity.status(200).body(existingProfile);
        } else {
            return ResponseEntity.status(404).body("Meeting " + meeting_id + " does not exist.");
        }
    }


    //DELETE: delete a meeting, Brandon
    @DeleteMapping("/api/meeting/{meeting_id}")
    public ResponseEntity delete_Meeting(@PathVariable String meeting_id) {
        Optional<?> meetingProfile = this.meetingRepository.findById(meeting_id);
        if (meetingProfile.isPresent()) {
            this.meetingRepository.deleteById(meeting_id);

            return ResponseEntity.status(200).body("Meeting " + meeting_id + " has been deleted");
        } else {
            return ResponseEntity.status(404).body("Meeting " + meeting_id + " does not exist.");
        }
    }

    //// ********************* SEARCH FOR AVAILABLE TIMESLOTS ENDPOINTS ***********************************
    @GetMapping("/api/timeslots")
    public ResponseEntity<List<UserTimeSlots>> getCommonTimeSlots() {
        List<UserTimeSlots> commonTimeSlotsList = timeSlotService.getCommonTimeSlotsForAllUsers();

        if (commonTimeSlotsList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(commonTimeSlotsList);
    }

    //// ********************* USER REGISTRATION AND AUTHENTICATION ENDPOINTS ***********************************
    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest){
        try {
            User newUser = userService.createUser(userRequest);
            return ResponseEntity.status(201).body("User registered successfully");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }

    @PostMapping("/api/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserRequest userRequest){
        User authenticatedUser = userService.authenticateUser(userRequest.getEmail(), userRequest.getPassword());

        if(authenticatedUser != null){
            String token = TokenUtil.generateToken(authenticatedUser);
            return ResponseEntity.ok(token);
        }
        else{
            return ResponseEntity.status(401).body("Authentication failed.");
        }

    }

    @PutMapping("/api/user/{user_id}/updatePassword")
    public ResponseEntity<?> updateUserPassword(@RequestBody UserRequest userRequest, @PathVariable String user_id){
        try {
            userService.updatePassword(user_id, userRequest);
            return ResponseEntity.status(200).body("Password updated successfully");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (NoSuchElementException e){
            return ResponseEntity.status(404).body("User "+ user_id + " not found.");
        }
    }



    //// ********************* INVITATION AND NOTIFICATION ENDPOINTS ***********************************



}