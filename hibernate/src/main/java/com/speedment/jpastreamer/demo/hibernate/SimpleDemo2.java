package com.speedment.jpastreamer.demo.hibernate;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;
import com.speedment.jpastreamer.demo.hibernate.model.Film$;

/** This example shows how to select five films with rating G, sort them by descending length and skip the first ten entries. */

public class SimpleDemo2 {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        jpaStreamer.stream(Film.class)
            .filter(Film$.rating.equal("G"))
            .sorted(Film$.length.reversed().thenComparing(Film$.title.comparator()))
            .skip(10)
            .limit(5)
            .forEach(SimpleDemo2::printFilm);

        jpaStreamer.close();

    }

    private static void printFilm(Film f) {
        System.out.printf("%4d %-25s %-5s %d%n", f.getFilmId(), f.getTitle(), f.getRating(), f.getLength());
    }

}
