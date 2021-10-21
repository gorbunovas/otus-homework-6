package ru.nl.billing.api;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.nl.billing.model.Cash;
import ru.nl.billing.model.User;
import ru.nl.billing.service.UserService;

@RestController
@RequestMapping("billing")
@RequiredArgsConstructor
public class BillingController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<Long> CreateUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.CreateUser(user));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> GetUser( @PathVariable("userId") Long id) {
        return ResponseEntity.ok( userService.GetUser(id));
    }

    @PostMapping("/deposit/{userId}")
    public ResponseEntity<Integer> Deposit(@PathVariable("userId") Long id, @RequestBody Cash amount) {
        return ResponseEntity.ok(userService.DepositBalanceUser(id, amount.getAmount()));
    }

    @PostMapping("/withdraw/{userId}")
    public ResponseEntity<String> Withdraw(@PathVariable("userId") Long id, @RequestBody Cash amount)  {
        var cach = userService.WithdrawBalanceUser(id, amount.getAmount());
        if(cach == -1) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("error");
        }
        return ResponseEntity.ok(cach.toString());
    }

}
