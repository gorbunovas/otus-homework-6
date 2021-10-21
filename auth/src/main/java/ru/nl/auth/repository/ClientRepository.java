package ru.nl.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nl.auth.model.ClientDetails;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientDetails, String> {
    @Query("select u from ClientDetails u where u.email = ?1")
    Optional<ClientDetails> findByClient(String email);
}
