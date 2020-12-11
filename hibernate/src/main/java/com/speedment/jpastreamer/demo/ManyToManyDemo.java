package com.speedment.jpastreamer.demo;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.model.Actor;
import com.speedment.jpastreamer.demo.model.Actor$;
import com.speedment.jpastreamer.demo.model.Film;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.*;
import static java.util.stream.Collectors.toMap;

/**
 * A Many-to-Many relationship is defined as a relationship between two tables where many multiple rows
 * from a first table can match multiple rows in a second table. Often a third table is used to form these relations.
 * For example, an actor may participate in several films and a film usually have several actors.
 * <p>
 * This example demonstrates how to create a filmography that maps every actor to a list of films that they have starred in.
 */
public class ManyToManyDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        Map<Actor, List<Film>> filmography = jpaStreamer.stream(of(Actor.class).joining(Actor$.films))
            .collect(toMap(
                Function.identity(),
                Actor::getFilms
            )
        );

        filmography.forEach(
            (k, v) -> System.out.format("%s: %s\n",
                k.getFirstName() + " " + k.getLastName(),
                v.stream().map(Film::getTitle).collect(Collectors.toList())
            )
        );

        jpaStreamer.close();




        // Called due to a bug in the MySQL JDBC driver
        // Thread mysql-cj-abandoned-connection-cleanup gets stuck
        // See https://github.com/speedment/jpa-streamer-demo/issues/1
        System.exit(0);
    }
}