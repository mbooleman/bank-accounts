package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.BMG.TracingUtils;
import com.bank.marwin.gans.BMG.models.User;
import com.bank.marwin.gans.BMG.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TracingUtils tracingUtils;

    public void createUser(User user) {
        tracingUtils.withSpan("create user", () -> userRepository.save(user));
    }

    public Optional<User> findUserById(UUID userId) {
        return tracingUtils.withSpan("create user", () -> userRepository.findById(userId));
    }
}
