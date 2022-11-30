package com.backend.dove.scheduling;

import com.backend.dove.service.UserService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@EnableScheduling
public class UserScheduling {

    UserService service;

    public UserScheduling(UserService service) {
        this.service = service;
    }

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedDelay = 1)
    public void clearUnverified() {
        service.clearUnverified();
    }

}
