package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.MeetingCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MeetingCounterRepository extends MongoRepository<MeetingCounter, String> {
    MeetingCounter findByName(String name);
}
