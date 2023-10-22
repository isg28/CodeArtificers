package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MeetingRepository extends MongoRepository<Meeting, String > {

}
