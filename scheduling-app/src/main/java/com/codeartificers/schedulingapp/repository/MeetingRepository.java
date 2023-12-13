package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MeetingRepository extends MongoRepository<Meeting, String > {
    @Query("{'calendar_id': ?0}")
    List<Meeting> findByCalendar(String calendar_id);

}
