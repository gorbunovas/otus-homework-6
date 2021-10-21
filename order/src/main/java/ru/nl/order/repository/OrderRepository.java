package ru.nl.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nl.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}