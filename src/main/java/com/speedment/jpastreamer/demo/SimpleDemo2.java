package com.speedment.jpastreamer.demo;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.model.Film;
import com.speedment.jpastreamer.demo.model.Film$;

/** This example shows how to select five films with rating G, sort them by descending length and skip the first ten entries. */

public class SimpleDemo2 {

    public static void main(String[] args) throws InterruptedException {

        JPAStreamer jpaStreamer = JPAStreamer.createJPAStreamerBuilder("sakila")
                .build();

        jpaStreamer.stream(Film.class)
                .filter(Film$.rating.equal("G"))
                .sorted(Film$.length.reversed().thenComparing(Film$.title.comparator()))
                .skip(10)
                .limit(5)
                .forEach(System.out::println);

        jpaStreamer.close();

    }
}
