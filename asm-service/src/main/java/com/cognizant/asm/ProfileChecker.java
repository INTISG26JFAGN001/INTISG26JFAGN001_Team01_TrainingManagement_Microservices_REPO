package com.cognizant.asm;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.boot.CommandLineRunner;

@Profile({"dev", "test"})
@Component
public class ProfileChecker implements CommandLineRunner {

    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("ACTIVE PROFILES: ");
        for (String profile : environment.getActiveProfiles()) {
            System.out.println(profile);
        }
    }
}
