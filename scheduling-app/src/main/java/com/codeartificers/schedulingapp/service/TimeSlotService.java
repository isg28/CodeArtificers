package com.codeartificers.schedulingapp.service;

import com.codeartificers.schedulingapp.model.Availability;
import com.codeartificers.schedulingapp.model.TimeSlot;
import com.codeartificers.schedulingapp.model.UserTimeSlots;
import com.codeartificers.schedulingapp.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeSlotService {
    @Autowired
    private AvailabilityRepository availabilityRepository;

    private TimeSlot mapAvailabilityToTimeSlot(Availability availability){
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setUser_id(availability.getUser_id());
        timeSlot.setDate(availability.getDate());
        timeSlot.setStart(availability.getStart());
        timeSlot.setEnd(availability.getEnd());
        return timeSlot;
    }

    private boolean hasSameDateAndTimeAsOthers(TimeSlot timeSlot, List<Availability> allAvailability, String userId) {
        for (Availability otherAvailability : allAvailability) {
            // Check if the availability belongs to a different user
            if (!otherAvailability.getUser_id().equals(userId)) {
                // Map the other availability to time slot
                TimeSlot otherTimeSlot = mapAvailabilityToTimeSlot(otherAvailability);

                // Check if the time slots have the same date and time
                if (areTimeSlotsEqual(timeSlot, otherTimeSlot)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean areTimeSlotsEqual(TimeSlot timeSlot1, TimeSlot timeSlot2) {
        LocalDate date1 = timeSlot1.getDate();
        LocalDateTime startTime1 = timeSlot1.getStart();
        LocalDateTime endTime1 = timeSlot1.getEnd();

        LocalDate date2 = timeSlot2.getDate();
        LocalDateTime startTime2 = timeSlot2.getStart();
        LocalDateTime endTime2 = timeSlot2.getEnd();

        return date1.equals(date2) && ((startTime1.isBefore(endTime2)&& startTime2.isBefore(endTime1)) || (startTime2.isBefore(endTime1) && startTime1.isBefore(endTime2)) || (startTime1.equals(startTime2) && (endTime1.equals(endTime2))));
    }

    public List<UserTimeSlots> getCommonTimeSlotsForAllUsers() {
        List<Availability> allAvailability = availabilityRepository.findAll();
        List<UserTimeSlots> commonTimeSlotsList = new ArrayList<>();

        for (Availability availability : allAvailability) {
            String userId = availability.getUser_id();
            TimeSlot timeSlot = mapAvailabilityToTimeSlot(availability);

            if(hasSameDateAndTimeAsOthers(timeSlot, allAvailability, userId)){
                UserTimeSlots userTimeSlotsObj = new UserTimeSlots(userId, List.of(timeSlot));
                commonTimeSlotsList.add(userTimeSlotsObj);
            }
        }

        return commonTimeSlotsList;
    }
}
