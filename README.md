# JPAstreamer Demo

This repo contains examples showing how [JPAstreamer](https://github.com/speedment/jpa-streamer) can be used in conjunction
with Hibernate. Although, please note that JPAstreamer can be used with **any** JPA provider. 

## Example Database 
The demo makes use of the [MySQL Sakila database](https://dev.mysql.com/doc/sakila/en/). It can either be downloaded from Oracle as described [here](https://dev.mysql.com/doc/sakila/en/sakila-installation.html) or used directly via a [Sakila Docker instance](https://hub.docker.com/r/restsql/mysql-sakila/) by issuing the following commands: 

```shell script
$ docker pull restsql/mysql-sakila
$ docker run -d --publish 3306:3306 --name mysqld restsql/mysql-sakila
```
## Running the Demo Code
The easiest way to run the demo code is to clone the repository as a whole. Then the programs can be run either via your IDE or via Maven commands as described below. Before you run the programs, make sure you have access to the example database either locally or via Docker. 

### Run via IDE
Open the project as a Maven project in your IDE and execute any of the main-methods in one of the Demo-classes. 

### Run via Maven
To execute any of the demos, use the command `mvn install exec:java@` followed by the demo name. For example: 

```shell script
mvn install exec:java@SimpleDemo1 
```
## Demos
Below is a summary of the included examples. 

### SimpleDemo1 
[SimpleDemo1](src/main/java/com/speedment/jpastreamer/demo/SimpleDemo1.java) shows how to create a stream of films of length between 100 and 120 minutes.

```java
jpaStreamer.stream(Film.class)
    .filter(Film$.length.between(100, 120))
    .forEach(SimpleDemo1::printFilm);
```

Prints:
```text
These are the films that are of length between 100 and 120 minutes:
   4 AFFAIR PREJUDICE          G     117
   9 ALABAMA DEVIL             PG-13 114
  19 AMADEUS HOLY              PG    113
  32 APOCALYPSE FLAMINGOS      R     119
  44 ATTACKS HATE              PG-13 113
  46 AUTUMN CROW               G     108
  48 BACKLASH UNDEFEATED       PG-13 118
  54 BANGER PINOCCHIO          R     113
  62 BED HIGHBALL              NC-17 106
  65 BEHAVIOR RUNAWAY          PG    100
```

Automatic Stream to JPA rendering:
```roomsql
select
        Film 
    from
        Film as Film 
    where
        Film.length between 100 and 120 
```

Automatic JPA to Hibernate SQL rendering:
```roomsql
select
    film0_.film_id as film_id1_1_,
    film0_.description as descript2_1_,
    film0_.language_id as languag11_1_,
    film0_.last_update as last_upd3_1_,
    film0_.length as length4_1_,
    film0_.rating as rating5_1_,
    film0_.rental_duration as rental_d6_1_,
    film0_.rental_rate as rental_r7_1_,
    film0_.replacement_cost as replacem8_1_,
    film0_.special_features as special_9_1_,
    film0_.title as title10_1_ 
from
    film film0_ 
where
    film0_.length between 100 and 120
```

### SimpleDemo1WithJoining 
[SimpleDemo1WithJoining](src/main/java/com/speedment/jpastreamer/demo/SimpleDemo1WithJoining.java) as SimpleDemo1 but showing how to join in fields avoiding the "N + 1 select problem".

```java
jpaStreamer.stream(of(Film.class).joining(Film$.actors).joining(Film$.language))
    .filter(Film$.length.between(100, 120))
    .forEach(SimpleDemo1::printFilm);
```

### SimpleDemo2
[SimpleDemo2](src/main/java/com/speedment/jpastreamer/demo/SimpleDemo2.java) shows how to select five films with rating G, sort them by descending length and skip the first ten entries. 

```java
jpaStreamer.stream(Film.class)
    .filter(Film$.rating.equal("G"))
    .sorted(Film$.length.reversed().thenComparing(Film$.title.comparator()))
    .skip(10)
    .limit(5)
    .forEach(SimpleDemo2::printFilm);
```

Prints:
```text
 126 CASUALTIES ENCINO         G     179
 692 POTLUCK MIXED             G     179
 897 TORQUE BOUND              G     179
 958 WARDROBE PHANTOM          G     178
 280 EMPIRE MALKOVICH          G     177
```

### PagingDemo
[PagingDemo](src/main/java/com/speedment/jpastreamer/demo/PagingDemo.java) demonstrates how to serve a page request from a GUI or a similar application.

```java
return jpaStreamer.stream(Film.class)
    .sorted(comparator)
    .skip(page * PAGE_SIZE)
    .limit(PAGE_SIZE)
    .collect(Collectors.toList());
``` 

Prints:
```text
CYCLONE FAMILY
DADDY PITTSBURGH
DAISY MENAGERIE
DALMATIONS SWEDEN
DANCES NONE
DANCING FEVER
DANGEROUS UPTOWN
...
```


### PivotDemo
[PivotDemo](src/main/java/com/speedment/jpastreamer/demo/PivotDemo.java) demonstrates how to make a pivot table containing all the actors and the number of films they have participated in for each film rating category (e.g. “PG-13”).

````java
Map<Actor, Map<String, Long>> pivot = jpaStreamer.stream(Actor.class)
    .collect(
        groupingBy(Function.identity(),
                   Collectors.flatMapping(a -> a.getFilms().stream(),
                                          groupingBy(Film::getRating, counting())
                      )
                  )
            );
```` 

Prints:
```text
SCARLETT BENING: {PG-13=7, R=5, NC-17=4, PG=5, G=5}
WOODY HOFFMAN: {PG-13=7, R=6, NC-17=5, PG=6, G=7}
ED MANSFIELD: {PG-13=4, R=8, NC-17=9, PG=5, G=6}
PARKER GOLDBERG: {PG-13=9, R=4, NC-17=5, G=2, PG=4}
...
```

### OneToManyDemo
[OneToManyDemo](src/main/java/com/speedment/jpastreamer/demo/OneToManyDemo.java) maps the languages to a list of all films that are spoken in that language.

```java
Map<Language, Set<Film>> languageFilmMap = jpaStreamer.stream(Language.class)
    .limit(10)
    .collect(
        toMap(Function.identity(),Language::getFilms)
    );
```

Prints:
```text
French: []
Mandarin: []
Italian: []
English: [INFORMER DOUBLE, CITIZEN SHREK, OCTOBER SUBMARINE, ANACONDA CONFESSIONS, ...]
German: []
...
````

### ManyToOneDemo
[ManyToOneDemo](src/main/java/com/speedment/jpastreamer/demo/ManyToOneDemo.java) maps every film with rating PG-13 to its spoken language.

```java
Map<Film, Language> languageMap = jpaStreamer.stream(Film.class)
     .filter(Film$.rating.equal("PG-13"))
     .collect(
         Collectors.toMap(Function.identity(), Film::getLanguage)
     );
```

Prints:
```text
EXPECATIONS NATURAL: English
SINNERS ATLANTIS: English
TRIP NEWTON: English
WRONG BEHAVIOR: English
STRICTLY SCARFACE: English
MICROCOSMOS PARADISE: English
...
````


### ManyToManyDemo
[ManyToManyDemo](src/main/java/com/speedment/jpastreamer/demo/ManyToManyDemo.java) demonstrates how to create a filmography that maps every actor to a list of films that they have starred in.

```java
Map<Actor, List<Film>> filmography = jpaStreamer.stream(Actor.class)
     .collect(
         Collectors.toMap(Function.identity(), Actor::getFilms)
     );

```

Prints:
```text
MARY TANDY: [BARBARELLA STREETCAR, CHILL LUCK, DANGEROUS UPTOWN, ...]
KEVIN BLOOM: [AMERICAN CIRCUS, BOOGIE AMELIE, CITIZEN SHREK, ...]
DUSTIN TAUTOU: [AFRICAN EGG, AUTUMN CROW, BANGER PINOCCHIO, ...]
...
````

### TransactionDemo 
[TransactionDemo](src/main/java/com/speedment/jpastreamer/demo/TransactionDemo.java) demonstrates a JPA transaction that updates the rental rate of a selection of films.

```java
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
```

Prints:
```text
Rental rates before raise:
The rental rate for AIRPORT POLLOCK is $4.990000.
The rental rate for ALONE TRIP is $0.990000.
The rental rate for AMELIE HELLFIGHTERS is $4.990000.
The rental rate for AMERICAN CIRCUS is $4.990000.
The rental rate for ANACONDA CONFESSIONS is $0.990000.
...
Rental rates after the raise:
The rental rate for AIRPORT POLLOCK is $5.990000.
The rental rate for ALONE TRIP is $1.990000.
The rental rate for AMELIE HELLFIGHTERS is $5.990000.
The rental rate for AMERICAN CIRCUS is $5.990000.
The rental rate for ANACONDA CONFESSIONS is $1.990000.
...
````

## Configuration
This repository is configured to work without modification if you use the Docker instance. However, we provide you with details about the Maven and Hibernate configuration below if you wish to set up your own tests. 

### Maven
The following main dependencies are needed for the project: 

```xml
    <dependencies>

        <dependency>
            <groupId>com.speedment.jpastreamer</groupId>
            <artifactId>core</artifactId>
            <version>${jpa-streamer-version}</version>
        </dependency>

        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.4.17.Final</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.21</version>
        </dependency>

    </dependencies>

```
Here is the complete [`pom.xml`](pom.xml) file.

### JPA Configuration
The demos are using Hibernate as the JPA provider. The [`resources/META-INF/persistence.xml`](src/main/resources/META-INF/persistence.xml) file looks like this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
     http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">

    <persistence-unit name="sakila" transaction-type="RESOURCE_LOCAL">
        <description>Test</description>
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <properties>
            <!-- Configuring The Database Connection Details -->
            <property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver" />
            <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/sakila" />
            <property name="javax.persistence.jdbc.user" value="root" />
            <property name="javax.persistence.jdbc.password" value="sakila" />
<!--        <property name="hibernate.show_sql" value="true"/> -->
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>
<!--        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>-->

        </properties>
    </persistence-unit>
</persistence>
```
As can be seen, the persistence name is "sakila" and this name is consequently used by JPAstreamer in the demos above.

## Resources
- **JPAstreamer on GitHub** - https://github.com/speedment/jpa-streamer
- **Documentation** - https://speedment.github.io/jpa-streamer
- **Gitter Chat** - https://gitter.im/speedment/jpa-streamer
- **Website** - www.jpastreamer.org


