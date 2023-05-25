package com.creppyfm.server.authentication;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SessionService {

    @Autowired
    private final SessionRepository sessionRepository;

    public Session findSession(String sessionId) {
        return sessionRepository.findById(sessionId);
    }
}
