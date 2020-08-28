package com.speedment.jpastreamer.demo;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.model.Film;
import com.speedment.jpastreamer.demo.model.Film$;

public class Demo1 {

    public static void main(String[] args) throws InterruptedException {

        JPAStreamer jpaStreamer = JPAStreamer.createJPAStreamerBuilder("sakila")
                .build();

        System.out.println("These are the films that are of length between 100 and 120 minutes.");

        jpaStreamer.stream(Film.class)
                .filter(Film$.length.between(100, 120))
                .forEach(System.out::println);


        jpaStreamer.close();


    }
}
