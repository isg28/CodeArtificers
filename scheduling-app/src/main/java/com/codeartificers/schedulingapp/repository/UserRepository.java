package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String > {
    User findByUsername(String username);
    User findByEmail(String email);

}

