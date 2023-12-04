package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.Availability;
import com.codeartificers.schedulingapp.model.Calender;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CalenderRepository extends MongoRepository<Calender, String> {
    @Query("{'user_id': ?0}")
    List<Calender> findByUser_id(String user_id);
}
