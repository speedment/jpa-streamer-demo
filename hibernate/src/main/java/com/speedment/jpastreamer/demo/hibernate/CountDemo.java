package com.speedment.jpastreamer.demo.hibernate;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;
import com.speedment.jpastreamer.demo.hibernate.model.Film$;

/**
 * This demo shows how to count the number of elements in the database. 
 */
public class CountDemo {

    public static void main(String[] args) {

        // Instantiate JPAStreamer
        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        // Count all films 
        long countAll = jpaStreamer.stream(Film.class).count();
        System.out.println("There are " + countAll + " films in the database.");

        // Count long films
        long countLong = jpaStreamer.stream(Film.class)
                .filter(Film$.length.greaterThan(120))
                .count();
        System.out.println("There are " + countLong + " films that are more than 2 hours long.");

        // Count films with a title that starts with "A"
        long countStartWithA = jpaStreamer.stream(Film.class)
                .filter(f -> f.getTitle().endsWith("A"))
                .count();
        System.out.println("There are " + countStartWithA + " films with a title that starts with the letter A.");

        // Clean up resources
        jpaStreamer.close();
    }
    
}
