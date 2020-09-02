package com.speedment.jpastreamer.demo;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.model.Film;
import com.speedment.jpastreamer.demo.model.Film$;

/** This example shows how to select films that are between 100 and 120 minutes long. */

public class SimpleDemo1 {

    public static void main(String[] args) throws InterruptedException {

        JPAStreamer jpaStreamer = JPAStreamer.createJPAStreamerBuilder("sakila")
                .build();

        System.out.println("These are the films that are of length between 100 and 120 minutes:");

        jpaStreamer.stream(Film.class)
                .filter(Film$.length.between(100, 120))
                .forEach(SimpleDemo1::printFilm);

        jpaStreamer.close();




        // Called due to a bug in the MySQL JDBC driver
        // Thread mysql-cj-abandoned-connection-cleanup gets stuck
        // See https://github.com/speedment/jpa-streamer-demo/issues/1
        System.exit(0);
    }

    private static void printFilm(Film f) {
        System.out.printf("%4d %-25s %-5s %d%n", f.getFilmId(), f.getTitle(), f.getRating(), f.getLength());
    }

}