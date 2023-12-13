package com.codeartificers.schedulingapp.controller;

import com.codeartificers.schedulingapp.model.*;
import com.codeartificers.schedulingapp.model.Calendar;
import com.codeartificers.schedulingapp.repository.*;
import com.codeartificers.schedulingapp.resource.*;
import com.codeartificers.schedulingapp.service.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
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
    @Autowired InvitationRepository invitationRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private InvitationCounter invitationCounter;
    @Autowired
    private InvitationCounterRepository invitationCounterRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CalendarCounter calendarCounter;
    @Autowired
    private CalendarCounterRepository calendarCounterRepository;
    @Autowired
    private CalendarRepository calendarRepository;



    @Autowired
    public ScheduleController(UserRepository userRepository, AvailabilityRepository availabilityRepository, UserCounterRepository userCounterRepository, AvailabilityCounterRepository availabilityCounterRepository1,
                              MeetingRepository meetingRepository, MeetingCounterRepository meetingCounterRepository, TimeSlotRepository timeSlotRepository, TimeSlotService timeSlotService,
                              UserService userService, InvitationRepository invitationRepository, NotificationService notificationService, InvitationCounter invitationCounter
                                , InvitationCounterRepository invitationCounterRepository, JwtUtil jwtUtil, CalendarCounter calendarCounter,
                              CalendarCounterRepository calendarCounterRepository, CalendarRepository calendarRepository) {
        this.userRepository = userRepository;
        this.availabilityRepository = availabilityRepository;
        this.userCounterRepository = userCounterRepository;
        this.availabilityCounterRepository = availabilityCounterRepository1;
        this.meetingRepository = meetingRepository;
        this.meetingCounterRepository = meetingCounterRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotService = timeSlotService;
        this.userService = userService;
        this.invitationRepository = invitationRepository;
        this.notificationService = notificationService;
        this.invitationCounter = invitationCounter;
        this.invitationCounterRepository = invitationCounterRepository;
        this.jwtUtil = jwtUtil;
        this.calendarCounter = calendarCounter;
        this.calendarCounterRepository = calendarCounterRepository;
        this.calendarRepository = calendarRepository;
    }

    @GetMapping("/api/user/{user_id}/calendar/{calendar_id}")
    public ResponseEntity<List<UserInformationOnCalendar>> getUsersByCalendarId(@PathVariable String user_id, @PathVariable String calendar_id) {
        Optional<Calendar> optionalCalendar = calendarRepository.findById(calendar_id);

        if (optionalCalendar.isPresent()) {
            Calendar calendar = optionalCalendar.get();

            if (calendar.getSharedWith() != null) {
                List<String> userIDs = new ArrayList<>(calendar.getSharedWith());
                userIDs.add(calendar.getUser_id());

                List<User> users = userRepository.findAllById(userIDs);

                List<UserInformationOnCalendar> userInformationOnCalendarsList = users.stream()
                        .map(user -> {
                            UserInformationOnCalendar userInformationOnCalendar = new UserInformationOnCalendar();
                            userInformationOnCalendar.setFirstName(user.getFirstName());
                            userInformationOnCalendar.setLastName(user.getLastName());
                            userInformationOnCalendar.setEmail(user.getEmail());
                            userInformationOnCalendar.setDob(user.getDob());
                            userInformationOnCalendar.setUsername(user.getUsername());
                            return userInformationOnCalendar;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.status(200).body(userInformationOnCalendarsList);
            } else {
                return ResponseEntity.status(404).body(Collections.emptyList());
            }
        } else {
            return ResponseEntity.status(404).body(Collections.emptyList());
        }
    }

    @PostMapping("/api/user")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        try{
            User newUser = userService.createUser(userRequest);
            return ResponseEntity.status(201).body(newUser);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }

    @GetMapping("/api/user/{user_id}")
    public ResponseEntity<?> get_UserProfile(@PathVariable String user_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        if (userProfile.isPresent()) {
            return ResponseEntity.status(200).body(userProfile.get());
        } else {
            return ResponseEntity.status(404).body("User " + user_id + " does not exist.");
        }
    }

    @PutMapping("/api/user/{user_id}")
    public ResponseEntity<?> edit_UserProfile(@RequestBody UserRequest userRequest, @PathVariable String user_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);

        if (userProfile.isPresent()) {
            User existingProfile = userProfile.get();

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

            if (userRequest.getFirstName() == null && userRequest.getLastName() == null && userRequest.getEmail() == null
                    && userRequest.getDob() == null && userRequest.getUsername() == null) {
                return ResponseEntity.status(400).body("Malformed request. Missing required user fields.");
            }

            userRepository.save(existingProfile);
            return ResponseEntity.status(200).body(existingProfile);
        } else {
            return ResponseEntity.status(404).body("User " + user_id + " does not exist.");
        }
    }

    @DeleteMapping("/api/user/{user_id}")
    public ResponseEntity delete_UserProfile(@PathVariable String user_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        if (userProfile.isPresent()) {
            this.userRepository.deleteById(user_id);

            return ResponseEntity.status(200).body("User " + user_id + " has been deleted");
        }
        return ResponseEntity.status(404).build();
    }

    @PostMapping("/api/user/{user_id}/calendar/{calendar_id}/availability")
    public ResponseEntity<?> createNewAvailability(@PathVariable String user_id, @PathVariable String calendar_id, @RequestBody AvailabilityRequest availabilityRequest) {
        AvailabilityCounter availabilityCounter = availabilityCounterRepository.findByName("availability_id");

        if (availabilityRequest.getDate() != null && availabilityRequest.getTitle() != null) {
            if (availabilityCounter == null) {
                availabilityCounter = new AvailabilityCounter();
                availabilityCounter.setName("availability_id");
                availabilityCounter.setSequence(1L);
            }
            long nextAvailabilityId = availabilityCounter.getSequence() + 1;
            availabilityCounter.setSequence(nextAvailabilityId);
            availabilityCounterRepository.save(availabilityCounter);
            Availability availability = new Availability();
            availability.setAvailability_id(String.valueOf(nextAvailabilityId));
            availability.setUser_id(user_id);
            availability.setCalendar_id(calendar_id);
            availability.setDate(availabilityRequest.getDate());
            availability.setTitle(availabilityRequest.getTitle());
            availability.setMeeting(false);

            String availabilityToken = TokenUtil.generateAvailabilityToken(availability, jwtUtil.getSecretKey());

                if (availabilityRequest.getStart() != null && availabilityRequest.getEnd() != null) {
                    LocalDateTime startDateTime = availabilityRequest.getStart();
                    LocalDateTime endDateTime = availabilityRequest.getEnd();
                    availability.setStart(startDateTime);
                    availability.setEnd(endDateTime);
                } else {
                    return ResponseEntity.status(400).body("Malformed request. Missing required start and end times.");
                }

            return ResponseEntity.status(201)
                    .header("Authorization", "Bearer " + availabilityToken)
                    .body(this.availabilityRepository.save(availability));
        } else {
            return ResponseEntity.status(400).body("Malformed request. Missing required user fields.");
        }

    }

    @GetMapping("/api/user/{user_id}/calendar/{calendar_id}/availability")
    public ResponseEntity<?> getAllAvailability(@PathVariable String user_id, @PathVariable String calendar_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Calendar> calendarProfile = this.calendarRepository.findById(calendar_id);
        if (!userProfile.isPresent()) {
            return ResponseEntity.status(404).body("User ID: " + user_id + " not found");
        } else if (!calendarProfile.isPresent()) {
            return ResponseEntity.status(404).body("Calendar ID: " + calendar_id + " not found");

        }
        List<Availability> userAvailabilities = this.availabilityRepository.findByCalendar_id(calendar_id);

        if (userAvailabilities.isEmpty()) {
            return ResponseEntity.status(404).body("There is no availability entry for User: " + user_id);
        }

        return ResponseEntity.status(200).body(userAvailabilities);
    }

    @PutMapping("/api/user/{user_id}/calendar/{calendar_id}/availability/{availability_id}")
    public ResponseEntity editAvailabilityEntry(@RequestBody AvailabilityRequest availabilityRequest, @PathVariable String user_id, @PathVariable String calendar_id, @PathVariable String availability_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Calendar> calendarProfile = this.calendarRepository.findById(calendar_id);
        Optional<Availability> availability = this.availabilityRepository.findById(availability_id);

        if (userProfile.isPresent() && calendarProfile.isPresent() && availability.isPresent()) {
            Availability existingAvailability = availability.get();

            if (availabilityRequest.getStart() != null) {
                existingAvailability.setStart(availabilityRequest.getStart());
            }
            if (availabilityRequest.getEnd() != null) {
                existingAvailability.setEnd(availabilityRequest.getEnd());
            }
            if (availabilityRequest.getDate() != null) {
                existingAvailability.setDate(availabilityRequest.getDate());
            }
            if(availabilityRequest.getTitle() != null) {
                existingAvailability.setTitle(availabilityRequest.getTitle());
            }
            if (availabilityRequest.getStart() == null && availabilityRequest.getDate() == null &&
                    availabilityRequest.getEnd() == null && availabilityRequest.getTitle() == null) {
                return ResponseEntity.status(400).body("Malformed request. Missing required availability fields.");

            }

            availabilityRepository.save(existingAvailability);
            return ResponseEntity.status(200).body(existingAvailability);

        } else {
            return ResponseEntity.status(404).body("Availability " + availability_id + " for User " + user_id + " does not exist.");

        }
    }

    @DeleteMapping("/api/user/{user_id}/calendar/{calendar_id}/availability/{availability_id}")
    public ResponseEntity<?> deleteAvailabilityEntry(@PathVariable String user_id, @PathVariable String calendar_id, @PathVariable String availability_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Calendar> calendarProfile = this.calendarRepository.findById(calendar_id);
        Optional<Availability> availabilityEntry = this.availabilityRepository.findById(availability_id);

        if (userProfile.isPresent() && calendarProfile.isPresent() && availabilityEntry.isPresent()) {
            Availability deletedEntry = availabilityEntry.get();
            this.availabilityRepository.deleteById(availability_id);
            String errorMessage = "Availability entry with ID " + deletedEntry.getAvailability_id() + " has been deleted";
            return ResponseEntity.status(200).body(errorMessage);
        }
        return ResponseEntity.status(404).body("Availability entry " + availability_id + " not found for User " + user_id);
    }

    @PostMapping("/api/user/{user_id}/calendar/{calendar_id}/meeting")
    public ResponseEntity<?> createMeeting(@PathVariable String user_id, @PathVariable String calendar_id, @RequestBody MeetingRequest meetingRequest) {
        MeetingCounter counter = meetingCounterRepository.findByName("meeting_id");

        if (meetingRequest.getDate() != null && meetingRequest.getStart() != null && meetingRequest.getEnd() != null
                && meetingRequest.getLocation() != null && meetingRequest.getMeeting_Description() != null) {
            if (counter == null) {
                counter = new MeetingCounter();
                counter.setName("meeting_id");
                counter.setSequence(1L);
            }
            long nextMeetingId = counter.getSequence() + 1;
            counter.setSequence(nextMeetingId);
            meetingCounterRepository.save(counter);

            Meeting meeting = new Meeting();
            meeting.setMeeting_id(String.valueOf(nextMeetingId));
            meeting.setUser_id(user_id);
            meeting.setCalendar_id(calendar_id);
            meeting.setDate(meetingRequest.getDate());
            meeting.setStart(meetingRequest.getStart());
            meeting.setEnd(meetingRequest.getEnd());
            meeting.setLocation(meetingRequest.getLocation());
            meeting.setMeeting_Description(meetingRequest.getMeeting_Description());
            meeting.setIsMeeting(true);

            String meetingToken = TokenUtil.generateMeetingToken(meeting, jwtUtil.getSecretKey());

            return ResponseEntity.status(201)
                    .header("Authorization", "Bearer " + meetingToken)
                    .body(this.meetingRepository.save(meeting));
        } else {
            return ResponseEntity.status(400).body("Malformed request. Missing required user data fields.");
        }

    }

    @GetMapping("/api/user/{user_id}/calendar/{calendar_id}/meeting/{meeting_id}")
    public ResponseEntity<?> getMeetingDetails(@PathVariable String user_id, @PathVariable String calendar_id, @PathVariable String meeting_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Calendar> calendarProfile = this.calendarRepository.findById(calendar_id);
        Optional<Meeting> meetingData = this.meetingRepository.findById(meeting_id);
        if (!userProfile.isPresent()) {
            return ResponseEntity.status(404).body("User ID: " + user_id + " not found");
        } else if (!calendarProfile.isPresent()) {
            return ResponseEntity.status(404).body("Calendar ID: " + calendar_id + " not found");
        } else {
            return ResponseEntity.status(200).body(meetingData.get());
        }
    }

    @GetMapping("/api/user/{user_id}/calendar/{calendar_id}/meeting")
    public ResponseEntity<?> getAllMeetingData(@PathVariable String user_id, @PathVariable String calendar_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Calendar> calendarProfile = this.calendarRepository.findById(calendar_id);
        if (!userProfile.isPresent()) {
            return ResponseEntity.status(404).body("User ID: " + user_id + " not found");
        } else if (!calendarProfile.isPresent()) {
            return ResponseEntity.status(404).body("Calendar ID: " + calendar_id + " not found");
        }
        List<Meeting> userMeetings = this.meetingRepository.findByCalendar(calendar_id);

        if (userMeetings.isEmpty()) {
            return ResponseEntity.status(404).body("There is no meeting entry for User: " + user_id);
        }

        return ResponseEntity.status(200).body(userMeetings);
    }

    @PutMapping("/api/user/{user_id}/calendar/{calendar_id}/meeting/{meeting_id}")
    public ResponseEntity update_meeting(@RequestBody MeetingRequest meetingRequest, @PathVariable String user_id, @PathVariable String calendar_id, @PathVariable String meeting_id) {
        Optional<Meeting> meetingProfile = this.meetingRepository.findById(meeting_id);
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Calendar> calendarProfile = this.calendarRepository.findById(calendar_id);
        if (meetingProfile.isPresent() && userProfile.isPresent() && calendarProfile.isPresent()) {
            Meeting existingProfile = meetingProfile.get();

            if (meetingRequest.getStart() != null) {
                existingProfile.setStart(meetingRequest.getStart());
            }
            if (meetingRequest.getEnd() != null) {
                existingProfile.setEnd(meetingRequest.getEnd());
            }
            if (meetingRequest.getDate() != null) {
                existingProfile.setDate(meetingRequest.getDate());
            }
            if (meetingRequest.getLocation() != null) {
                existingProfile.setLocation(meetingRequest.getLocation());
            }
            if (meetingRequest.getMeeting_Description() != null) {
                existingProfile.setMeeting_Description(meetingRequest.getMeeting_Description());
            }
            if (meetingRequest.getStart() == null && meetingRequest.getEnd() == null && meetingRequest.getDate()
                    == null && meetingRequest.getLocation() == null && meetingRequest.getMeeting_Description() == null) {
                return ResponseEntity.status(400).body("Malformed request. Missing required user fields.");
            }

            meetingRepository.save(existingProfile);
            return ResponseEntity.status(200).body(existingProfile);
        } else {
            return ResponseEntity.status(404).body("Meeting " + meeting_id + " does not exist.");
        }
    }


    @DeleteMapping("/api/user/{user_id}/calendar/{calendar_id}/meeting/{meeting_id}")
    public ResponseEntity delete_Meeting(@PathVariable String meeting_id, @PathVariable String calendar_id, @PathVariable String user_id) {
        Optional<?> meetingProfile = this.meetingRepository.findById(meeting_id);
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Calendar> calendarProfile = this.calendarRepository.findById(calendar_id);
        if (meetingProfile.isPresent() && userProfile.isPresent() && calendarProfile.isPresent()) {
            this.meetingRepository.deleteById(meeting_id);

            return ResponseEntity.status(200).body("Meeting " + meeting_id + " has been deleted");
        } else {
            return ResponseEntity.status(404).body("Meeting " + meeting_id + " does not exist.");
        }
    }

    @GetMapping("/api/calendar/{calendar_id}/timeslots")
    public ResponseEntity<List<UserTimeSlots>> getCommonTimeSlots(@PathVariable String calendar_id) {

        List<UserTimeSlots> commonTimeSlotsList = timeSlotService.getCommonTimeSlotsForCalendar(calendar_id);

        List<Availability> allAvailability = availabilityRepository.findByCalendar_id(calendar_id);

        if (allAvailability.isEmpty()) {
            System.out.println("No availabilities found for Calendar ID: " + calendar_id);
        } else {
            System.out.println("Availabilities found for Calendar ID: " + calendar_id);
            for (Availability availability : allAvailability) {
                System.out.println("Availability: " + availability);
            }
        }

        if (commonTimeSlotsList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(commonTimeSlotsList);
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest){
        try {
            User newUser = userService.createUser(userRequest);

            String token = userRequest.getToken();

            return ResponseEntity.status(HttpStatus.CREATED).header("Authorization", "Bearer " + token)
                    .body("User registered successfully");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }

    @PostMapping("/api/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        if(loginRequest.getEmail() != null && loginRequest.getPassword() != null){
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            User authenticatedUser = userService.authenticateUser(email, password);

            if(authenticatedUser != null){
                return ResponseEntity.status(200).body(authenticatedUser);
            }
            else {
                return ResponseEntity.status(401).body("Authentication failed.");
            }
        }
        else{
            return ResponseEntity.status(400).body("Bad Request - Missing email or password");
        }


    }

    @PutMapping("/api/user/{user_id}/updatePassword")
    public ResponseEntity<?> updateUserPassword(@RequestBody UpdatePasswordRequest passwordRequest, @PathVariable String user_id){
        try {
            userService.updatePassword(user_id, passwordRequest);
            return ResponseEntity.status(200).body("Password updated successfully");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (NoSuchElementException e){
            return ResponseEntity.status(404).body("User "+ user_id + " not found.");
        }
    }


    @PostMapping("/api/user/{user_id}/calendar/{calendar_id}/invite")
    public ResponseEntity<?> createInvitations(@RequestBody InvitationRequest invitationRequest, @PathVariable String user_id, @PathVariable String calendar_id) {
        try {
            if (invitationRequest == null ||
                    invitationRequest.getUser_id() == null ||
                    invitationRequest.getCalendar_id() == null ||
                    !invitationRequest.getUser_id().equals(user_id) ||
                    !invitationRequest.getCalendar_id().equals(calendar_id)) {
                return ResponseEntity.status(400).body("Invalid input parameters");
            }

            String senderId = invitationRequest.getUser_id();
            if (!userService.isUserIdValid(senderId)) {
                return ResponseEntity.status(400).body("Invalid senderId");
            }

            List<InvitedUser> invitedUsers = new ArrayList<>();
            List<InvitedUser> invitedUserRequests = invitationRequest.getInvitedUsers();

            if (invitedUserRequests != null && !invitedUserRequests.isEmpty()) {
                for (InvitedUser invitedUserRequest : invitedUserRequests) {
                    User userData = userRepository.findByEmail(invitedUserRequest.getEmail());

                    if (userData != null) {
                        String email = userData.getEmail();
                        String firstName = userData.getFirstName();
                        String lastName = userData.getLastName();
                        String username = userData.getUsername();
                        String userId = userData.getUser_id();

                        InvitedUser newUser = new InvitedUser(userId, firstName, lastName, username, email);
                        invitedUsers.add(newUser);
                    } else {
                        System.out.println("User not found for email: " + invitedUserRequest.getEmail());
                    }
                }
            }

            InvitationCounter counter = invitationCounterRepository.findByName("invitationId");
            if (counter == null) {
                counter = new InvitationCounter();
                counter.setName("invitationId");
                counter.setSequence(1L);
            }
            long nextInvitationId = counter.getSequence() + 1;
            counter.setSequence(nextInvitationId);
            invitationCounterRepository.save(counter);

            Invitation invitation = new Invitation();
            invitation.setInvitation_id(String.valueOf(nextInvitationId));
            invitation.setCalendar_id(calendar_id);
            invitation.setUser_id(user_id);
            invitation.setInvitedUsers(invitedUsers);

            Optional<Calendar> sharedCalendarOptional = calendarRepository.findById(calendar_id);
            if (sharedCalendarOptional.isPresent()) {
                Calendar sharedCalendar = sharedCalendarOptional.get();
                List<String> sharedWith = sharedCalendar.getSharedWith();

                for (InvitedUser invitedUser : invitedUsers) {
                    User invitedUserProfile = userRepository.findByEmail(invitedUser.getEmail());

                    if (invitedUserProfile != null) {
                        userService.addCalendarToHomePage(invitedUserProfile, calendar_id);

                        String invitedUserId = invitedUserProfile.getUser_id();

                        if (!sharedWith.contains(invitedUserId)) {
                            sharedWith.add(invitedUserId);
                        }
                    }
                }

                sharedCalendar.setSharedWith(sharedWith);
                calendarRepository.save(sharedCalendar);
            } else {
                System.out.println("Calendar not found for calendar_id: " + calendar_id);
            }

            return ResponseEntity.status(201).body("Invitations sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @GetMapping("/api/user/{user_id}/calendar/{calendar_id}/invite/{invitation_id}")
    public ResponseEntity<?> getInvitation(@PathVariable String user_id, @PathVariable String calendar_id,
                                           @PathVariable String invitation_id) {
        Optional invitationData = this.invitationRepository.findById(invitation_id);
        if (invitationData.isPresent()) {
            return ResponseEntity.status(200).body(invitationData.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/user/{user_id}/calendar/{calendar_id}/invite/{invitation_id}")
    public ResponseEntity<?> updateInvitation(@RequestBody InvitationRequest invitationRequest, @PathVariable String user_id,
                                              @PathVariable String calendar_id, @PathVariable String invitation_id)  {
        Optional<Invitation> invitationProfile = this.invitationRepository.findById(invitation_id);
        if (invitationProfile.isPresent()) {
            Invitation existingProfile = invitationProfile.get();

            if (!invitationRequest.getUser_id().equals(existingProfile.getUser_id()) ||
                    !invitationRequest.getCalendar_id().equals(existingProfile.getCalendar_id()) ||
                    !user_id.equals(existingProfile.getUser_id())) {
                return ResponseEntity.status(400).body("Cannot modify sender_id or calendar_id.");
            }

            List<InvitedUser> updatedInvitedUsers = existingProfile.getInvitedUsers();
            if (invitationRequest.getInvitedUsers() != null) {
                for (InvitedUser updatedUser : invitationRequest.getInvitedUsers()) {
                    boolean userExists = updatedInvitedUsers.stream()
                            .anyMatch(user -> user.getEmail().equals(updatedUser.getEmail()));

                    if (!userExists) {
                        User userData = userRepository.findByEmail(updatedUser.getEmail());

                        String email = userData.getEmail();
                        String firstName = userData.getFirstName();
                        String lastName = userData.getLastName();
                        String username = userData.getUsername();
                        String userId = userData.getUser_id();

                        InvitedUser newUser = new InvitedUser(userId, firstName, lastName, username, email);

                        if (updatedUser.getUser_id() != null) {
                            newUser.setUser_id(updatedUser.getUser_id());
                        }
                        if (updatedUser.getFirstName() != null) {
                            newUser.setFirstName(updatedUser.getFirstName());
                        }
                        if (updatedUser.getLastName() != null) {
                            newUser.setLastName(updatedUser.getLastName());
                        }
                        if (updatedUser.getUsername() != null) {
                            newUser.setUsername(updatedUser.getUsername());
                        }

                        updatedInvitedUsers.add(newUser);
                    }
                }

                existingProfile.setInvitedUsers(updatedInvitedUsers);
            }
            invitationRepository.save(existingProfile);
            return ResponseEntity.status(200).body(existingProfile);
        } else {
            return ResponseEntity.status(404).body("Invitation " + invitation_id + " does not exist.");
        }
    }

    @DeleteMapping("/api/user/{user_id}/calendar/{calendar_id}/invite/{invitation_id}/delete-invited-user/{emailToDelete}")
    public ResponseEntity<?> deleteInvitedUser(@PathVariable String user_id, @PathVariable String calendar_id,
                                               @PathVariable String invitation_id, @PathVariable String emailToDelete) {

        Optional<Invitation> invitationProfile = this.invitationRepository.findById(invitation_id);
        if (invitationProfile.isPresent()) {
            Invitation existingProfile = invitationProfile.get();

            if (!user_id.equals(existingProfile.getUser_id())) {
                return ResponseEntity.status(403).body("You are not authorized to delete invited users for this invitation.");
            }

            List<InvitedUser> invitedUsers = existingProfile.getInvitedUsers();

            InvitedUser userToDelete = invitedUsers.stream()
                    .filter(user -> emailToDelete.equals(user.getEmail()))
                    .findFirst()
                    .orElse(null);

            if (userToDelete != null) {
                invitedUsers.remove(userToDelete);
                existingProfile.setInvitedUsers(invitedUsers);

                invitationRepository.save(existingProfile);
                return ResponseEntity.status(200).body(existingProfile);
            } else {
                return ResponseEntity.status(404).body("InvitedUser with email " + emailToDelete + " not found.");
            }
        } else {
            return ResponseEntity.status(404).body("Invitation " + invitation_id + " does not exist.");
        }
    }

    @PostMapping("/api/user/{user_id}/calendar")
    public ResponseEntity<?> createNewCalendar(@PathVariable String user_id, @RequestBody CalendarRequest calendarRequest){
        CalendarCounter calendarCounter = calendarCounterRepository.findByName("calendar_id");

        if (calendarRequest.getCalendarTitle() != null) {
            if (calendarCounter == null) {
                calendarCounter = new CalendarCounter();
                calendarCounter.setName("calendar_id");
                calendarCounter.setSequence(1L);
            }
            long nextCalendarId = calendarCounter.getSequence() + 1;
            calendarCounter.setSequence(nextCalendarId);
            calendarCounterRepository.save(calendarCounter);

            Calendar calendar = new Calendar();
            calendar.setCalendar_id(String.valueOf(nextCalendarId));
            calendar.setUser_id(user_id);
            calendar.setCalendarTitle(calendarRequest.getCalendarTitle());
            calendar.setSharedWith(new ArrayList<>());

            String calendarToken = TokenUtil.generateCalendarToken(calendar, jwtUtil.getSecretKey());

            User user = userRepository.findById(user_id).orElse(null);
            if (user != null) {
                userService.addCalendarToHomePage(user, String.valueOf(nextCalendarId));
            }

            return ResponseEntity.status(201)
                    .header("Authorization", "Bearer " + calendarToken)
                    .body(this.calendarRepository.save(calendar));
        } else {
            return ResponseEntity.status(400).body("Malformed request. Missing required Calendar fields.");
        }
    }

    @GetMapping("/api/user/{user_id}/calendar")
    public ResponseEntity<?> getAllCalendar(@PathVariable String user_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        if (!userProfile.isPresent()) {
            return ResponseEntity.status(404).body("User ID: " + user_id + " not found");
        }
        List<Calendar> userCalendars = this.calendarRepository.findByUser_id(user_id);
        List<Calendar> sharedCalendars = this.calendarRepository.findBySharedWithContaining(user_id);

        List<Calendar> allCalendars = new ArrayList<>();
        allCalendars.addAll(userCalendars);
        allCalendars.addAll(sharedCalendars);

        if (allCalendars.isEmpty()) {
            return ResponseEntity.status(404).body("There is no calendar entry for User: " + user_id);
        }

        return ResponseEntity.status(200).body(allCalendars);
    }

    @PutMapping("/api/user/{user_id}/calendar/{calendar_id}")
    public ResponseEntity<?> edit_calendarEntry(@RequestBody CalendarRequest calendarRequest, @PathVariable String user_id, @PathVariable String calendar_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Calendar> calendar = this.calendarRepository.findById(calendar_id);

        if (userProfile.isPresent() && calendar.isPresent()) {
            Calendar existingCalendar = calendar.get();

            if (calendarRequest.getCalendarTitle() != null) {
                existingCalendar.setCalendarTitle(calendarRequest.getCalendarTitle());
            }
            if (calendarRequest.getCalendarTitle() == null) {
                return ResponseEntity.status(400).body("Malformed request. Missing required Calendar fields.");

            }

            calendarRepository.save(existingCalendar);
            return ResponseEntity.status(200).body(existingCalendar);

        } else {
            return ResponseEntity.status(404).body("Calendar " + calendar_id + " for User " + user_id + " does not exist.");

        }
    }


    @DeleteMapping("/api/user/{user_id}/calendar/{calendar_id}")
    public ResponseEntity<?> deleteCalendarEntry(@PathVariable String user_id, @PathVariable String calendar_id) {
        Optional<User> userProfile = this.userRepository.findById(user_id);
        Optional<Calendar> calendarEntry = this.calendarRepository.findById(calendar_id);

        if (userProfile.isPresent() && calendarEntry.isPresent()) {
            Calendar deletedEntry = calendarEntry.get();
            this.calendarRepository.deleteById(calendar_id);
            String errorMessage = "Calendar entry with ID " + deletedEntry.getCalendar_id() + " has been deleted";
            return ResponseEntity.status(200).body(errorMessage);
        }
        return ResponseEntity.status(404).body("Calendar entry " + calendar_id + " not found for User " + user_id);
    }


}