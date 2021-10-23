package ru.nl.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import ru.nl.order.configuration.*;
import ru.nl.order.exception.BillingException;
import ru.nl.order.model.Cash;
import ru.nl.order.model.Notification;
import ru.nl.order.model.Order;
import ru.nl.order.model.User;
import ru.nl.order.repository.OrderRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    private final static String godMessage = "Заказ успешно создан";

    private final static String badMessage = "Ошибка создания заказа";

    private final OrderRepository repository;

    private final BillingServiceConfiguration billingProperties;
    private final NotificationServiceConfiguration notificationProperties;

    public OrderService(OrderRepository repository,
                        BillingServiceConfiguration billingProperties,
                        NotificationServiceConfiguration notificationProperties) {
        this.repository = repository;
        this.billingProperties = billingProperties;
        this.notificationProperties = notificationProperties;
    }

    public Order GetOrder(Long id){
        return repository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Order CreateOrder(Long userId, Integer price, UUID idempotencyKey){
        var order = new Order(null, price, "CREATE", userId, idempotencyKey);
        if(repository.findByIdempotency(idempotencyKey).isPresent())  {
            order.setStatus("EXISTS");
            return order;
        }
        repository.save(order);
        var usr = GetUser(userId);
        SendNotification(userId, usr.getEmail(), "Заказ N" + order.getId(), Withdraw(userId, order));
        return repository.save(order);
    }

    private User GetUser(Long client) {

        RestTemplate restTemplate = new RestTemplate();
        var user = new User();
        try {
           user = restTemplate.getForEntity(billingProperties.getName() + billingProperties.getUser() + "/" + client, User.class).getBody();
        } catch (HttpClientErrorException e) {
            throw new BillingException(e.getStatusCode() + " Ошибка запроса клиента");
        }
        return user;
    }
    //
    private String Withdraw(Long client, Order order) {
        RestTemplate restTemplate = new RestTemplate();

        var cash = new Cash();
        cash.setAmount(order.getPrice());
        var msg = "Заказ создан";
        try {
            restTemplate.postForEntity (billingProperties.getName() + billingProperties.getWithdraw() + "/" + client,
                    cash, Integer.class);
            order.setStatus("PAYMENT_PASSED");
        } catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.PAYMENT_REQUIRED ) {
                msg = "Ошибка: недостаточно средств";
            } else {
                msg = "Ошибка при списание средств " + e.getStatusCode();
            }
            order.setStatus("PAYMENT_REQUIRED");
        } catch (HttpServerErrorException e) {
            log.error(e.getStatusCode() + " " + e.getMessage());
        }

        return msg;
    }
    private void SendNotification(Long client, String email, String title, String text) {

        RestTemplate restTemplate = new RestTemplate();

        var msg = new Notification();
        msg.setEmail(email);
        msg.setTitle(title);
        msg.setBody(text);

        var notify = restTemplate.postForEntity (notificationProperties.getName() + "/notification/" + client, msg, Integer.class);

        if(notify.getStatusCode() != HttpStatus.CREATED ) {
            log.error(" Ошибка отправки сообщения");
        }
    }

}
