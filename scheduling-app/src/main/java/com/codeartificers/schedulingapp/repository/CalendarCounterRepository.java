package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.CalendarCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CalendarCounterRepository extends MongoRepository<CalendarCounter, String> {
    CalendarCounter findByName(String name);

}
