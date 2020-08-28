# jpa-streamer-demo

This repo shows some demos of how JPAstreamer can be used in conjunction
with a JPA provider such as Hibernate.

## Short example
JPAstreamer allows one to express JPA queries using standard Java Streams. The following
example TBW: 

```java
// TBW
```

## Example Database
The demos are all using the Sakila database which can be downloaded directly from Oracle as described [Here](https://dev.mysql.com/doc/sakila/en/sakila-installation.html) or can be
used directly via a Docker instance by issuing the following commands:

```shell script
$ docker pull restsql/mysql-sakila
$ docker run -d --publish 3306:3306 --name mysqld restsql/mysql-sakila
```
Read more on the Sakila Docker image [here](https://hub.docker.com/r/restsql/mysql-sakila/)

## Demos
This repo contains a handful demo programs that can be run directly from your IDE or via Maven commands.

### Demo 1 - Filers
This demo shows how to create a stream of films of length between 100 and 120 minutes.

#### Run via IDE

Open the class [Demo1.java](src/main/java/com/speedment/jpastreamer/demo/Demo1.java) in your IDE and run the main class.

#### Run via Maven

```shell script
mvn exec:java@Demo1
```

### Demo 2 - TBW


## Configuration

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
Here is the complete [`pom.xml`](pom.xml) file 


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
        <class>com.speedment.jpa.app.model.embed.FilmRatingConverter</class>
        <properties>
            <!-- Configuring The Database Connection Details -->
            <property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver" />
            <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/sakila" />
            <property name="javax.persistence.jdbc.user" value="root" />
            <property name="javax.persistence.jdbc.password" value="sakila" />

            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>

<!--            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>-->

        </properties>
    </persistence-unit>
</persistence>
```
As can be seen, the persistence name is "sakila" and this name is consequently used by JPAstreamer in the demos above.