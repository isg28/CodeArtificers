package com.codeartificers.schedulingapp.repository;

import com.codeartificers.schedulingapp.model.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvitationRepository extends MongoRepository<Invitation, String> {
}
