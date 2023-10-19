package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.UserCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserCounterRepository extends MongoRepository<UserCounter, String> {
    UserCounter findByName(String name);

}
