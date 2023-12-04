package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.CalenderCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CalenderCounterRepository extends MongoRepository<CalenderCounter, String> {
    CalenderCounter findByName(String name);

}
