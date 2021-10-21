package ru.nl.billing.repository;


import org.springframework.data.jpa.repository.Query;
import ru.nl.billing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserModelRepository extends JpaRepository<User, Long> {
//    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);
}