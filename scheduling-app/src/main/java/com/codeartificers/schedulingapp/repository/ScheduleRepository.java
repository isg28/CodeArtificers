package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.Schedules;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ScheduleRepository extends MongoRepository<Schedules, String > {

}
