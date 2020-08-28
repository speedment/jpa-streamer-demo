package com.speedment.jpastreamer.demo;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.model.Film;
import com.speedment.jpastreamer.demo.model.Language;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * A One-to-Many relationship is defined as a relationship between two tables where a row from a first
 * table can have multiple matching rows in a second table. For example, many films can be in the same language.
 *
 * This example maps the languages to a list of all films that are spoken in that language.
 */
public class OneToManyDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.createJPAStreamerBuilder("sakila")
                .build();

        Map<Language, Set<Film>> languageFilmMap = jpaStreamer.stream(Language.class)
                .limit(10)
                .collect(
                        toMap(Function.identity(),
                                Language::getFilms)
                );

        languageFilmMap
                .forEach(
                        (k, v) -> System.out.format("%s: %s\n", k.getName(),
                                v.stream().map(Film::getTitle).collect(toList())));

        jpaStreamer.close();

    }
}
