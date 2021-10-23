package ru.nl.order.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nl.order.model.Error;
import ru.nl.order.model.Order;
import ru.nl.order.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private static final String AUTH_CLIENT_HEADER = "X-Auth-Client";

    private static final String IDEMPOTENCY_KEY_HEADER = "Idempotency-Key";

    final private OrderService orderService;

    @PostMapping()
    public ResponseEntity<Object> CreateOrder(@RequestHeader(value = IDEMPOTENCY_KEY_HEADER) UUID idempotencyKey, @RequestBody Order order) {
        System.out.println(idempotencyKey);
        order = orderService.CreateOrder(order.getUserId(), order.getPrice(), idempotencyKey);
        switch (order.getStatus()) {
            case "PAYMENT_REQUIRED":
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(order.getId());
            case "EXISTS":
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(409, "Заказ ранее был создан"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> GetOrder(@PathVariable("orderId") Long id) {
        return ResponseEntity.ok(orderService.GetOrder(id));
    }
}
