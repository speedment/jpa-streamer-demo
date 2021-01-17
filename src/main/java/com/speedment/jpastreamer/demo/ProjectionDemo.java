package com.speedment.jpastreamer.demo;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.model.Film;
import com.speedment.jpastreamer.demo.model.Film$;
import com.speedment.jpastreamer.projection.Projection;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;

import javax.persistence.Tuple;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectionDemo {

    public static void main(String[] args) {

        JPAStreamer jpaStreamer = JPAStreamer.of("sakila");

        List<Tuple> tupleList = jpaStreamer.stream(Film.class)
                .sorted(Film$.length.reversed())
                .limit(3)
                .map(Projection.select(Film$.filmId, Film$.title))
                .collect(Collectors.toList());

        System.out.println("tupleList = " + tupleList);

        final Tuple tuple = tupleList.get(0);

        Integer id = tuple.get(0, Integer.class);
        System.out.println("id = " + id);

        String title = tuple.get("title", String.class);
        System.out.println("title = " + title);


    }
}
