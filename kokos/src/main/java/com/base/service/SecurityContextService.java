package com.base.service;

import com.base.model.User;

import java.util.Optional;

public interface SecurityContextService {
    Optional<User> currentUser();
}
