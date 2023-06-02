package com.speedment.jpastreamer.demo.hibernate.internal;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/** This example shows how to select films that are between 100 and 120 minutes long. */

public class JdbcCloseIssue {

    public static void main(String[] args) throws InterruptedException {

        /*
        JPAStreamer jpaStreamer = JPAStreamer.createJPAStreamerBuilder("sakila")
                .build();

        jpaStreamer.close();*/

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("sakila");



        entityManagerFactory.close();


    }

}
