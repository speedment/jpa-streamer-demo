package com.speedment.jpastreamer.demo.hibernate;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;
import com.speedment.jpastreamer.demo.hibernate.model.Film$;

import java.util.List;
import java.util.stream.Collectors;

import static com.speedment.jpastreamer.field.predicate.Inclusion.START_INCLUSIVE_END_EXCLUSIVE;

/** This example shows how to select films that are between 100 and 120 minutes long. */

public class SimpleDemo1 {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");
        
        System.out.println("These are the films that are of length between 100 and 120 minutes:");

        jpaStreamer.stream(Film.class)
            .filter(Film$.length.between(100, 120, START_INCLUSIVE_END_EXCLUSIVE))
            .forEach(SimpleDemo1::printFilm);

        jpaStreamer.close();

    }

    private static void printFilm(Film f) {
        System.out.printf("%4d %-25s %-5s %d%n", f.getFilmId(), f.getTitle(), f.getRating(), f.getLength());
    }

}
