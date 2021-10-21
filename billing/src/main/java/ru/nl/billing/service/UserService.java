package ru.nl.billing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import ru.nl.billing.model.User;
import ru.nl.billing.repository.UserModelRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserModelRepository repository;

    public Long CreateUser (User user) {
       return repository.save(user).getId();
   }

    public User GetUser (Long id) {
        return repository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Integer DepositBalanceUser (Long id, Integer amount) {
        var user = repository.findById(id).orElseThrow(NoSuchElementException::new);
        user.setBalance(user.getBalance() + amount);
        repository.save(user);
        return user.getBalance();
    }

    public Integer WithdrawBalanceUser (Long id, Integer amount) {
        var user = repository.findById(id).orElseThrow(NoSuchElementException::new);
        user.setBalance(user.getBalance() - amount);
        if(user.getBalance() < 0) return -1; //throw new HttpServerErrorException(HttpStatus.PAYMENT_REQUIRED);
        repository.save(user);
        return user.getBalance();
    }
}
