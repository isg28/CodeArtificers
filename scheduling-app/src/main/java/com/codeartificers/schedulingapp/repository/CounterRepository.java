package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CounterRepository extends MongoRepository<Counter, String> {
    Counter findByName(String name);
}
