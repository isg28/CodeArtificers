package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvailabilityRepository extends MongoRepository<Availability, String> {
    //List<Availability> findByUserIdAndDate(@Param("userId") String userId, @Param("date") String date);
}
