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

### PagingDemo
[PagingDemo](src/main/java/com/speedment/jpastreamer/demo/PagingDemo.java) demonstrates how to serve a page request from a GUI or a similar application.

```java
return jpaStreamer.stream(Film.class)
    .sorted(comparator)
    .skip(page * PAGE_SIZE)
    .limit(PAGE_SIZE)
    .collect(Collectors.toList());
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

### OneToManyDemo
[OneToManyDemo](src/main/java/com/speedment/jpastreamer/demo/OneToManyDemo.java) maps the languages to a list of all films that are spoken in that language.

```java
Map<Language, Set<Film>> languageFilmMap = jpaStreamer.stream(Language.class)
    .limit(10)
    .collect(
        toMap(Function.identity(),Language::getFilms)
    );
```

### ManyToOneDemo
[ManyToOneDemo](src/main/java/com/speedment/jpastreamer/demo/ManyToOneDemo.java) maps every film with rating PG-13 to its spoken language.

```java
Map<Film, Language> languageMap = jpaStreamer.stream(Film.class)
     .filter(Film$.rating.equal("PG-13"))
     .collect(
         Collectors.toMap(Function.identity(), Film::getLanguage)
     );
```


### ManyToManyDemo
[ManyToManyDemo](src/main/java/com/speedment/jpastreamer/demo/ManyToManyDemo.java) demonstrates how to create a filmography that maps every actor to a list of films that they have starred in.

```java
Map<Actor, List<Film>> filmography = jpaStreamer.stream(Actor.class)
     .collect(
         Collectors.toMap(Function.identity(), Actor::getFilms)
     );

```

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


