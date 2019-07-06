package com.demonstrate;

import com.demonstrate.handler.UncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneOffset;
import java.util.TimeZone;

@SpringBootApplication
public class IngestionApp {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    }

    public static void main(String[] args) {
        SpringApplication.run(IngestionApp.class);
    }
}
