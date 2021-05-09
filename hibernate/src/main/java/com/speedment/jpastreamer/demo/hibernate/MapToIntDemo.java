package com.speedment.jpastreamer.demo.hibernate;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;
import com.speedment.jpastreamer.demo.hibernate.model.Film$;

public class MapToIntDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        jpaStreamer.stream(Film.class)
                .sorted(Film$.length.reversed())
                .limit(15)
                // Film$.length is nullable so we have to provide
                // a default .orElse() value
                .mapToInt(Film$.length.asInt().orElse(0))
                .forEach(System.out::println);

    }
}
