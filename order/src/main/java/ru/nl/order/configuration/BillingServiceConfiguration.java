package ru.nl.order.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "billing-service")
public class BillingServiceConfiguration {

    private String name;
    private String user;
    private String withdraw;

    public String getName() {
        return this.name;
    }

    public String getUser() {
        return this.user;
    }

    public String getWithdraw() {
        return this.withdraw;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }

}
