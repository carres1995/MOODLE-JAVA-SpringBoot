package com.riwi.hamilton.config;


import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.repository.EventRepository;
import com.riwi.hamilton.repository.VenueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    CommandLineRunner loadData(EventRepository event, VenueRepository venue){
        return args -> {
            event.save(new Event(1L,"RiwiEnvent","2026-05-10"));
            venue.save(new Venue(1L,"Plaza Mayor", "Medellin"));
        };
    }
}
