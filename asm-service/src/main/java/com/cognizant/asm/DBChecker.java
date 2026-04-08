package com.cognizant.asm;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.CommandLineRunner;

import java.sql.Connection;
import javax.sql.DataSource;

@Profile({"dev", "test"})
@Component
public class DBChecker implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("DATABASE CONNECTION SUCCESSFUL!");
            System.out.println("DATABASE PRODUCT NAME: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DATABASE PRODUCT VERSION: " + connection.getMetaData().getDatabaseProductVersion());
        } catch (Exception e) {
            System.err.println("DATABASE CONNECTION FAILED!");
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
