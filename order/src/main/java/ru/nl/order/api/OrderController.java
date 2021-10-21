package ru.nl.order.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nl.order.model.Order;
import ru.nl.order.service.OrderService;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    final private OrderService orderService;

    @PostMapping()
    public ResponseEntity<Long> CreateUser(@RequestBody Order order) {
        order = orderService.CreateOrder(order.getUserId(), order.getPrice());
        if(order.getStatus() == "PAYMENT_REQUIRED") return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(order.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> GetOrder(@PathVariable("orderId") Long id) {
        return ResponseEntity.ok(orderService.GetOrder(id));
    }
}
