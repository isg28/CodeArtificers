package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvailabilityRepository extends MongoRepository<Availability, String> {
    @Query("{'user_id': ?0}")
    List<Availability> findByUser_id(String user_id);

    @Query("{'calendar_id': ?0}")
    List<Availability> findByCalendar(String calendar_id);
}
