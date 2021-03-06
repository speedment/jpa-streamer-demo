package com.speedment.jpastreamer.demo.hibernate;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.groupingBy;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Actor;
import com.speedment.jpastreamer.demo.hibernate.model.Actor$;
import com.speedment.jpastreamer.demo.hibernate.model.Film;

import java.util.Map;
import java.util.function.Function;

/**
 * This example demonstrates how to make a pivot table containing all the actors and the number of
 * films they have participated in for each film rating category (e.g. “PG-13”).
 */

public class PivotDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");;

        Map<Actor, Map<String, Long>> pivot = jpaStreamer.stream(of(Actor.class).joining(Actor$.films))
            .collect(
                groupingBy(
                    Function.identity(),
                    flatMapping(
                        a -> a.getFilms().stream(),
                        groupingBy(Film::getRating, counting())
                    )
                )
            );

        pivot.forEach((k, v) -> System.out.format("%s %s: %s\n", k.getFirstName(), k.getLastName(), v));

        jpaStreamer.close();


    }
}
