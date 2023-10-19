package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.AvailabilityCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AvailabilityCounterRepository extends MongoRepository<AvailabilityCounter, String> {
    AvailabilityCounter findByName(String name);
}
