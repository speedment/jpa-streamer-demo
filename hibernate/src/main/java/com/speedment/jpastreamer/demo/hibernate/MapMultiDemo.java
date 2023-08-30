package com.speedment.jpastreamer.demo.hibernate;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;
import com.speedment.jpastreamer.demo.hibernate.model.Film$;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;

public class MapMultiDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        jpaStreamer.stream(StreamConfiguration.of(Film.class).joining(Film$.actors))
                .filter(Film$.length.greaterThan(120))
                .mapMulti((f, mapper) -> {
                    f.getActors().stream()
                            .filter(a -> a.getFirstName().startsWith("A"))
                            .forEach(mapper);
                })
                .forEach(System.out::println);

        jpaStreamer.close();

    }
    
}
