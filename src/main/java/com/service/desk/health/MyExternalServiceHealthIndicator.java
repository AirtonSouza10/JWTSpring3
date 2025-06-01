package com.service.desk.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("myExternalService")
public class MyExternalServiceHealthIndicator implements HealthIndicator{
    private final RestTemplate restTemplate = new RestTemplate();
    private final String externalServiceUrl;

    public MyExternalServiceHealthIndicator(@Value("${external.service.url}") String externalServiceUrl) {
        this.externalServiceUrl = externalServiceUrl;
    }

    @Override
    public Health health() {
        try {
            restTemplate.getForObject(externalServiceUrl, String.class);
            return Health.up()
                    .withDetail("url", externalServiceUrl)
                    .withDetail("message", "Successfully connected to external service.")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("url", externalServiceUrl)
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
