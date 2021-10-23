package ru.nl.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nl.order.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select u from Order u where u.idempotencyKey = ?1")
    Optional<Order> findByIdempotency(UUID id);
}