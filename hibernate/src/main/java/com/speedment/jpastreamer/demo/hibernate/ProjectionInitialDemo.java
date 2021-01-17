package com.speedment.jpastreamer.demo.hibernate;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;
import com.speedment.jpastreamer.demo.hibernate.model.Film$;
import com.speedment.jpastreamer.projection.Projection;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;

public class ProjectionInitialDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        StreamConfiguration<Film> sc = StreamConfiguration.of(Film.class).selecting(Projection.select(Film$.filmId, Film$.title));

        jpaStreamer.stream(sc)
                .sorted(Film$.length.reversed())
                .limit(3)
                .forEach(System.out::println);

    }
}