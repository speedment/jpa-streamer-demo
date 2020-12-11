package com.speedment.jpastreamer.demo.hibernate;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;
import static java.util.stream.Collectors.toMap;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;
import com.speedment.jpastreamer.demo.hibernate.model.Film$;
import com.speedment.jpastreamer.demo.hibernate.model.Language;

import java.util.Map;
import java.util.function.Function;

/**
 * A Many-to-One relationship is defined as a relationship between two tables where many multiple rows
 * from a first table can match the same single row in a second table.
 * For example, a single language may be used in many films.
 *
 * This example maps every film with rating PG-13 to its spoken language.
 */
public class ManyToOneDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        Map<Film, Language> languageMap = jpaStreamer.stream(of(Film.class).joining(Film$.language))
            .filter(Film$.rating.equal("PG-13"))
            .collect(toMap(
                Function.identity(),
                Film::getLanguage
            )
        );

        languageMap.forEach(
            (k, v) -> System.out.format("%s: %s\n",
                k.getTitle(),
                v.getName()
            )
        );

        jpaStreamer.close();




        // Called due to a bug in the MySQL JDBC driver
        // Thread mysql-cj-abandoned-connection-cleanup gets stuck
        // See https://github.com/speedment/jpa-streamer-demo/issues/1
        System.exit(0);
    }
}
