package com.speedment.jpastreamer.demo;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.model.Film;
import com.speedment.jpastreamer.demo.model.Film$;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This example demonstrates how to serve a page request from a GUI or a similar application.
 */
public class PagingDemo {

    private static final int PAGE_SIZE = 20;

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.createJPAStreamerBuilder("sakila")
                .build();

        getPage(jpaStreamer, 10, Film$.title)
                .forEach(f -> System.out.format("%s\n", f.getTitle()));

        jpaStreamer.close();
    }

    // The page number (starting with page = 0) and ordering will be given as parameters.
    private static List<Film> getPage(JPAStreamer jpaStreamer, int page, Comparator<Film> comparator) {
        return jpaStreamer.stream(Film.class)
                .sorted(comparator)
                .skip(page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .collect(Collectors.toList());
    }
}
