package com.speedment.jpastreamer.demo;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.model.Actor;
import com.speedment.jpastreamer.demo.model.Film;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/** This example demonstrates how to make a pivot table containing all the actors and the number of
films they have participated in for each film rating category (e.g. “PG-13”). */

public class PivotDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.createJPAStreamerBuilder("sakila")
                .build();

        Map<Actor, Map<String, Long>> pivot = jpaStreamer.stream(Actor.class)
                .collect(
                        groupingBy(Function.identity(),
                                Collectors.flatMapping(a -> a.getFilms().stream(),
                                        groupingBy(Film::getRating, counting())
                                )
                        )
                );

        pivot.forEach((k, v) -> System.out.format("%s %s: %s\n", k.getFirstName(), k.getLastName(), v));

        jpaStreamer.close();

    }
}
