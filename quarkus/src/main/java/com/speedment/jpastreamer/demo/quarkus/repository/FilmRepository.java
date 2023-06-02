package com.speedment.jpastreamer.demo.quarkus.repository;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.quarkus.Film$;
import com.speedment.jpastreamer.demo.quarkus.model.Actor;
import com.speedment.jpastreamer.demo.quarkus.model.Film;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class FilmRepository implements PanacheRepository<Film> {

    private final static int PAGE_SIZE = 20;
    private final JPAStreamer jpaStreamer = JPAStreamer.of(this::getEntityManager);

    /**
     * Lists limit number of films 
     *
     * @param limit the maximum number of films to list 
     * @return a list of all films 
     */
    public Stream<Film> listFilms(int limit) {
        return filmStream().limit(limit);
    }

    /**
     * Finds the first film with a title that matches the provided {@code title}. 
     *
     * @param title of the film 
     * @return first film with a matching title, or null if no film matches the query 
     */
    public Film findByTitle(String title) {
        return filmStream()
                .filter(Film$.title.equal(title))
                .findFirst().orElse(null);
    }

    /**
     * Returns the description of the film with the provided {@code title}. 
     *
     * @param title title of the film 
     * @return the description of the first film with a matching title, or null if no film matches the title 
     */
    public String findDescriptionByTitle(String title) {
        return filmStream()
                .filter(Film$.title.equal(title))
                .map(Film$.description)
                .findFirst().orElse(null);
    }

    /**
     * Lists all films sorted first by reversed rating and then by title 
     *
     * @return list of films sorted first by rating and then by title 
     */
    public Stream<Film> listFilmsSortedByRatingAndLength(int limit) {
        return filmStream()
                .sorted(Film$.rating.reversed().thenComparing(Film$.title.comparator()))
                .limit(limit);
    }

    /**
     * Sorts the films by length, and pages the result. 
     *
     * @param page the page number 
     * @return a stream of the paged results
     */
    public Stream<Film> paging(int page) {
        return filmStream()
                .sorted(Film$.length)
                .skip((long) PAGE_SIZE * page)
                .limit(PAGE_SIZE);
    }

    /**
     * Lists {@code limit} films that starts with the provided String {@code startsWith}, 
     * sorted from shortest to longest.
     *
     * @param start start string 
     * @param limit maximum number of results to retrieve 
     * @return a list of films sorted in ascending length 
     */
    public Stream<Film> titleStartsWithSortedByLengthLimited(String start, int limit) {
        return filmStream()
                .filter(Film$.title.startsWith(start))
                .sorted(Film$.length)
                .limit(limit);
    }


    /**
     * Lists {@code limit} films that are at least as long as the provided {@code length}. 
     *
     * @param length minimum film length 
     * @return films that are at least as long as the provided length 
     */
    public Stream<Film> listLonger(short length) {
        return filmStream()
                .filter(Film$.length.greaterThan(length));
    }

    /**
     * Updates the description of all films that are longer than the provided length to desc.  
     *
     * @param desc the new description 
     * @param length the minimum length of films that should be updated, exclusive 
     */
    @Transactional
    public void updateDescription(String desc, short length) {
        filmStream()
                .filter(Film$.length.greaterThan(length))
                .forEach(f -> {
                    f.setDescription(desc);
                    persist(f);
                });
    }

    /**
     * Returns a map containing all films with a title that starts with the provided String. 
     *
     * @param startsWith the start of the film title 
     * @return a mapping between films and their cast 
     */
    public Map<Film, Set<Actor>> getCast(String startsWith) {
        return jpaStreamer.stream(StreamConfiguration.of(Film.class).joining(Film$.actors))  // Left join is default
                .filter(Film$.title.startsWith(startsWith))
                .collect(
                        Collectors.toMap(Function.identity(),
                                Film::getActors
                        )
                );
    }

    private Stream<Film> filmStream() {
        return jpaStreamer.stream(Film.class);
    }

}
