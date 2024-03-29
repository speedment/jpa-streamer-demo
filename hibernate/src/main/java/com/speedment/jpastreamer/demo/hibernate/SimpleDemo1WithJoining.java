package com.speedment.jpastreamer.demo.hibernate;

import static com.speedment.jpastreamer.field.predicate.Inclusion.START_INCLUSIVE_END_EXCLUSIVE;
import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;
import com.speedment.jpastreamer.demo.hibernate.model.Film$;

/**
 * This example shows how to select films that are between 100 and 120 minutes long.
 * just as in SimpleDemo1 but where we join in elements to avoid the 1 + N select
 * problem.
 *
 */

public class SimpleDemo1WithJoining {

    public static void main(String[] args) throws InterruptedException {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        System.out.println("These are the films that are of length between 100 and 120 minutes:");

        // As the stream below prints out all fields, we are joining
        // in the actor and language columns directly:

        jpaStreamer.stream(of(Film.class).joining(Film$.actors).joining(Film$.language))
            .filter(Film$.length.between(100, 120, START_INCLUSIVE_END_EXCLUSIVE))
            .forEach(System.out::println);

        jpaStreamer.close();
        
        // Called due to a bug in the MySQL JDBC driver
        // Thread mysql-cj-abandoned-connection-cleanup gets stuck
        // See https://github.com/speedment/jpa-streamer-demo/issues/1
        System.exit(0);
    }

}
