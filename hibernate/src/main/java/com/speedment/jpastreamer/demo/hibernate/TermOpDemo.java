package com.speedment.jpastreamer.demo.hibernate;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;
import com.speedment.jpastreamer.demo.hibernate.model.Film$;

import java.util.Optional;

public class TermOpDemo {
    
    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");
        
        Optional<String> anyFilm = jpaStreamer.stream(Film.class)
                .filter(Film$.length.greaterThan(100).and(Film$.title.startsWith("C")))
                .map(Film$.title)
                .findAny();

        anyFilm.ifPresent(film -> System.out.format("%s starts with C is longer than 100 minutes.\n", film));

        Optional<String> firstFilm = jpaStreamer.stream(Film.class)
                .filter(Film$.length.greaterThan(100).and(Film$.title.startsWith("C")))
                .map(Film$.title)
                .findFirst();

        firstFilm.ifPresent(film -> System.out.format("%s starts with C and is longer than 100 minutes.\n", film));
        
        boolean anyLongFilm = jpaStreamer.stream(Film.class)
                .anyMatch(Film$.length.greaterThan(180));

        System.out.format("There is %s film that is longer than 180 minutes.\n", anyLongFilm ? "at least one" : "no");
        
        boolean noMatch = jpaStreamer.stream(Film.class)
                .noneMatch(Film$.length.greaterThan(180));
        
        System.out.format("There is %s film that is longer than 180 minutes.\n", !noMatch ? "at least one" : "no");
        
        jpaStreamer.close();

    }
    
}
