package com.codeartificers.schedulingapp.controller;

import com.codeartificers.schedulingapp.model.Schedules;
import com.codeartificers.schedulingapp.repository.ScheduleRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

/*
@RestController
@RequestMapping("/api") //specifying a base path for the API endpoints
public class ScheduleController{
    private final ScheduleRepository scheduleRepository;
    public ScheduleController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;

    }
    @GetMapping("/schedules")
    public ResponseEntity<List<Schedules>> getAllSchedules(){
        List<Schedules> schedules = scheduleRepository.findAll();
        return ResponseEntity.ok(schedules);
    }
    @GetMapping("/")
    public String APIroot(){
        return "Hello World";
    }
  //  @PostMapping("/schedules")
    //public ResponseEntity<Schedules> createSchedule(@RequestAttribute Schedules schedules){
      //  Schedules createdSchedules = scheduleRepository.save(schedules);
        //return ResponseEntity.ok(createdSchedules);
  //  }
}

 */