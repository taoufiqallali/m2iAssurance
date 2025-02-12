package com.m2iAssurance.repository;


import com.m2iAssurance.model.Policy;
import com.m2iAssurance.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    List<User> findAll();

}