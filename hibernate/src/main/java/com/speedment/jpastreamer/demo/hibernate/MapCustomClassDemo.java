package com.speedment.jpastreamer.demo.hibernate;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.hibernate.model.Film;

/** This example shows how to map an entity to a custom class. */

public class MapCustomClassDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        // This will be replaced by a better way in future versions

        jpaStreamer.stream(Film.class)
                .map(TitleLength::new)
                .forEach(System.out::println);

        jpaStreamer.close();

    }

    private static void printFilm(Film f) {
        System.out.printf("%4d %-25s %-5s %d%n", f.getFilmId(), f.getTitle(), f.getRating(), f.getLength());
    }

    public static final class TitleLength {

        private final String title;
        private final int length;

        public TitleLength(Film film) {
            this.title = film.getTitle();
            this.length= film.getLength();
        }

        public String title() {
            return title;
        }

        public int length() {
            return length;
        }

        @Override
        public String toString() {
            return "TitleLength{" +
                    "title='" + title + '\'' +
                    ", length=" + length +
                    '}';
        }
    }


}