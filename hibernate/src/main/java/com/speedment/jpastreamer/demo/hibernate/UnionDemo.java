package com.speedment.jpastreamer.demo.hibernate;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;
import com.speedment.jpastreamer.demo.hibernate.model.Film$;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;

import java.util.function.Function;
import java.util.stream.Stream;

/** This example shows how to select films that are between 100 and 120 minutes long. */

public class UnionDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        StreamConfiguration<Film> configuration = StreamConfiguration.of(Film.class)
            .joining(Film$.actors)
            .joining(Film$.language);

        Stream.of(
            jpaStreamer.stream(configuration).filter(Film$.length.greaterThan(120)),
            jpaStreamer.stream(configuration).filter(Film$.rating.equal("PG-13"))
        )
            .flatMap(Function.identity())
            .distinct()
            .forEachOrdered(System.out::println);


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
