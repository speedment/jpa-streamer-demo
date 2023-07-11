package com.speedment.jpastreamer.demo.quarkus;

import com.speedment.jpastreamer.demo.quarkus.model.Film;
import com.speedment.jpastreamer.demo.quarkus.repository.FilmRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.stream.Collectors;

@Path("/")
public class FilmResource {
    
    @Inject
    FilmRepository filmRepository;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("list/{limit}")
    public String list(int limit) {
        return filmRepository.listFilms(limit).map(Film::getTitle).collect(Collectors.joining("\n"));
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("sorted/{limit}")
    public String sorted(int limit) {
        return filmRepository.listFilmsSortedByRatingAndLength(limit)
                .map(f -> String.format("%s (%s): %s min\n", f.getTitle(), f.getRating(), f.getLength()))
                .collect(Collectors.joining());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("paging/{page}")
    public String paging(int page) {
        return filmRepository.paging(page)
                .map(f -> String.format("%s: %s min\n", f.getTitle(), f.getLength()))
                .collect(Collectors.joining());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("startsWithSort/{start}/{limit}")
    public String startsWithSort(String start, int limit) {
        return filmRepository.titleStartsWithSortedByLengthLimited(start, limit)
                .map(f -> String.format("%s: %s min\n", f.getTitle(), f.getLength()))
                .collect(Collectors.joining());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("update/{desc}/{length}")
    public String update(String desc, int length) {
        // Update descriptions for films longer than "length"
        filmRepository.updateDescription(desc, (short) length);

        // List all effected films 
        return filmRepository.listLonger((short) length)
                .map(f -> String.format("%s (%d min): %s\n", f.getTitle(), f.getLength(), f.getDescription()))
                .collect(Collectors.joining());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("actors/{start}")
    public String listActors( String start) {
        final StringBuilder sb = new StringBuilder();
        return filmRepository.getCast(start)
                .entrySet()
                .stream()
                .map(entry -> {
                    return String.format("%s: %s\n", entry.getKey().getTitle(),
                            (entry.getValue().stream().map(a -> {
                                return String.format("%s %s", (a.getFirstName().toLowerCase()), a.getLastName().toLowerCase());
                            }).collect(Collectors.joining(", "))));
                })
                .collect(Collectors.joining());
    }
    
}
