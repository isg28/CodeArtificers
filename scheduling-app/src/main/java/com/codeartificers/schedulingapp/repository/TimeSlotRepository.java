package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeSlotRepository extends MongoRepository<TimeSlot, String> {

}
