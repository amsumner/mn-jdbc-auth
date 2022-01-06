package com.practice.persistence;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

@Singleton
public class TestDataProvider {

    private final UserRepository users;

    public TestDataProvider(UserRepository users) {
        this.users = users;
        }

    @EventListener
    public void init(StartupEvent event){
        if (users.findByEmail("aaron@example.com").isEmpty()) {
            UserEntity aaron = new UserEntity();
            aaron.setEmail("aaron@example.com");
            aaron.setPassword("secret");
            users.save(aaron);
        }
    }
}

