package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AvailabilityRepository extends MongoRepository<Availability, String> {
}
