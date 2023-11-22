package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.Invitation;
import com.codeartificers.schedulingapp.model.InvitationCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvitationCounterRepository extends MongoRepository<InvitationCounter, String> {
    InvitationCounter findByName(String name);
}
