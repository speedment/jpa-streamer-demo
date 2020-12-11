package com.speedment.jpastreamer.demo.hibernate;

import static java.util.stream.Collectors.partitioningBy;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;
import com.speedment.jpastreamer.demo.hibernate.model.Film$;

import java.util.List;
import java.util.Map;

/**
 * This example demonstrates how to partition data in two buckets,
 * one for films of length greater than 120 minutes and one for films
 * of length less or equal to 120 minutes.
 */

public class PartitionDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        Map<Boolean, List<Film>> map = jpaStreamer.stream(Film.class)
            .collect(partitioningBy(Film$.length.greaterThan(120)));

        map.forEach((k, v) -> System.out.format("long is %5s has %d films%n", k, v.size()));

        jpaStreamer.close();




        // Called due to a bug in the MySQL JDBC driver
        // Thread mysql-cj-abandoned-connection-cleanup gets stuck
        // See https://github.com/speedment/jpa-streamer-demo/issues/1
        System.exit(0);
    }
}
