package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface UserRepository extends MongoRepository<User, String > {
    User findByUsername(String username);
    User findByEmail(String email);
    @Query("{'user_id': ?0}")
    User findByUser_id(String user_id);

}

