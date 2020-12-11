package com.speedment.jpastreamer.demo;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.demo.model.Film;
import com.speedment.jpastreamer.demo.model.Film$;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Transactions are an essential part of JPA that determines when modifications to the data is synchronised with the underlying database.
 * JPAstreamer can assist with the modifications while the actual transactions are handled using standard JPA.
 *
 * This example demonstrates a JPA transaction that updates the rental rate of a selection of films.
 */
public class TransactionDemo {

    public static void main(String[] args) {

        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("sakila");
        final EntityManager em = emf.createEntityManager();
        JPAStreamer jpaStreamer = JPAStreamer.of(emf);

        System.out.println("Rental rates before raise:");

        jpaStreamer.stream(Film.class)
            .filter(Film$.rating.equal("R"))
            .sorted()
            .limit(5)
            .forEach(f -> System.out.format("The rental rate for %s is $%f.\n", f.getTitle(), f.getRentalRate()));

        updateRentalRates(jpaStreamer, em);

        System.out.println("Rental rates after the raise:");

        jpaStreamer.stream(Film.class)
            .filter(Film$.rating.equal("R"))
            .sorted()
            .limit(5)
            .forEach(f -> System.out.format("The rental rate for %s is $%f.\n", f.getTitle(), f.getRentalRate()));

        jpaStreamer.close();
        em.close();
        emf.close();




        // Called due to a bug in the MySQL JDBC driver
        // Thread mysql-cj-abandoned-connection-cleanup gets stuck
        // See https://github.com/speedment/jpa-streamer-demo/issues/1
        System.exit(0);
    }

    /* Performs a transaction that increases the rental rate of every R-rated film (for adults only) with one dollar */
    private static void updateRentalRates(final JPAStreamer jpaStreamer, final EntityManager em) {

        try {
            em.getTransaction().begin();
            jpaStreamer.stream(Film.class)
                .filter(Film$.rating.equal("R"))
                .forEach(f -> {
                        f.setRentalRate(f.getRentalRate() + 1);
                        em.merge(f);
                    }
                );

            em.getTransaction().commit();
        } catch(Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

}
