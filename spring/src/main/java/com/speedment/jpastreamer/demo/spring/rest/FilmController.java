package com.speedment.jpastreamer.demo.spring.rest;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.spring.model.Film;
import com.speedment.jpastreamer.demo.spring.viewmodel.FilmViewModel;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;

import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
public class FilmController {

    private final JPAStreamer jpaStreamer;

    @Autowired
    public FilmController(JPAStreamer jpaStreamer) {
        this.jpaStreamer = jpaStreamer;
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(value = "/films", produces = MediaType.APPLICATION_JSON_VALUE)
    public Stream<FilmViewModel> films(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        return jpaStreamer.stream(Film.class)
                .skip((long) page * pageSize)
                .limit(pageSize)
                .map(FilmViewModel::from);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(value = "/stream-films", produces = MediaType.APPLICATION_JSON_VALUE)
    public Stream<FilmViewModel> stream() {
        return jpaStreamer.stream(
                StreamConfiguration.of(Film.class)
                        .withHint(QueryHints.FETCH_SIZE, 50)
                        .withHint(QueryHints.READ_ONLY, true))
                .map(FilmViewModel::from);
    }
}

