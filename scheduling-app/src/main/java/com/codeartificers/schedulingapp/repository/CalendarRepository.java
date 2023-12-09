package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.Calendar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CalendarRepository extends MongoRepository<Calendar, String> {
    @Query("{'user_id': ?0}")
    List<Calendar> findByUser_id(String user_id);
}
