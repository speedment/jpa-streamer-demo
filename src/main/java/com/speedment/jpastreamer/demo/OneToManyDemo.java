package com.speedment.jpastreamer.demo;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.model.Film;
import com.speedment.jpastreamer.demo.model.Language;
import com.speedment.jpastreamer.demo.model.Language$;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;

import javax.persistence.criteria.JoinType;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * A One-to-Many relationship is defined as a relationship between two tables where a row from a first
 * table can have multiple matching rows in a second table. For example, many films can be in the same language.
 * <p>
 * This example maps the languages to a list of all films that are spoken in that language.
 */
public class OneToManyDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        Map<Language, Set<Film>> languageFilmMap = jpaStreamer.stream(of(Language.class).joining(Language$.films))
                .collect(toMap(
                        Function.identity(),
                        Language::getFilms
                        )
                );

        languageFilmMap
                .forEach(
                        (k, v) -> System.out.format("%s: %s\n",
                                k.getName(),
                                v.stream().map(Film::getTitle).collect(toList())
                        )
                );

        jpaStreamer.close();




        // Called due to a bug in the MySQL JDBC driver
        // Thread mysql-cj-abandoned-connection-cleanup gets stuck
        // See https://github.com/speedment/jpa-streamer-demo/issues/1
        System.exit(0);

    }
}
