package com.codeartificers.schedulingapp.service;

import com.codeartificers.schedulingapp.model.Availability;
import com.codeartificers.schedulingapp.model.Calender;
import com.codeartificers.schedulingapp.model.Meeting;
import com.codeartificers.schedulingapp.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class TokenUtil {
    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateAvailabilityToken(Availability availability, Key secretKey) {
        long expirationTimeMillis = System.currentTimeMillis() + 3600000; // 1 hour
        Date expirationDate = new Date(expirationTimeMillis);


        String token = Jwts.builder()
                .setSubject(availability.getAvailability_id())
                .claim("availability_Id", availability.getAvailability_id())
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        return token;
    }

    public static String generateCalenderToken(Calender calender, Key secretKey) {
        long expirationTimeMillis = System.currentTimeMillis() + 3600000; // 1 hour
        Date expirationDate = new Date(expirationTimeMillis);

        String token = Jwts.builder()
                .setSubject(calender.getCalender_id())
                .claim("calender_id", calender.getCalender_id())
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        return token;
    }
    public static String generateMeetingToken(Meeting meeting, Key secretKey) {
        long expirationTimeMillis = System.currentTimeMillis() + 3600000; // 1 hour
        Date expirationDate = new Date(expirationTimeMillis);

        String token = Jwts.builder()
                .setSubject(meeting.getMeeting_id())
                .claim("meeting_id", meeting.getMeeting_id())
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        return token;
    }
}
