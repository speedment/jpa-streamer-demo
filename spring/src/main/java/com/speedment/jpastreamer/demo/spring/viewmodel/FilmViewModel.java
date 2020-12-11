package com.speedment.jpastreamer.demo.spring.viewmodel;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.speedment.jpastreamer.demo.spring.model.Film;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public final class FilmViewModel {

    private final int id;
    private final String title;
    private final String description;
    private final String language;
    private final List<String> actors;

    @JsonCreator
    public FilmViewModel(
            @JsonProperty("id") final Integer id,
            @JsonProperty("title") final String title,
            @JsonProperty("description") final String description,
            @JsonProperty("language") final String language,
            @JsonProperty("actors") final List<String> actors
    ) {
        this.id = requireNonNull(id);
        this.title = requireNonNull(title);
        this.description = requireNonNull(description);
        this.language = requireNonNull(language);
        this.actors = requireNonNull(actors);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getActors() {
        return actors;
    }

    public static FilmViewModel from(final Film film) {
        requireNonNull(film);

        return new FilmViewModel(
            film.getFilmId(),
            film.getTitle(),
            film.getDescription(),
            film.getLanguage().getName(),
            film.getActors().stream().map(actor -> actor.getFirstName() + " " + actor.getLastName()).collect(toList())
        );
    }
}

