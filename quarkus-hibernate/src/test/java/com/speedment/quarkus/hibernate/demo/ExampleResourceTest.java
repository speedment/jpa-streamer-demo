package com.speedment.quarkus.hibernate.demo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ExampleResourceTest {

    @Test
    public void testListEndpoint() {
        given()
                .when().get("/list/10")
                .then()
                .statusCode(200)
                .body(containsString("ACADEMY DINOSAUR\n" +
                                     "ACE GOLDFINGER\n" +
                                     "ADAPTATION HOLES\n" +
                                     "AFFAIR PREJUDICE\n" +
                                     "AFRICAN EGG\n" +
                                     "AGENT TRUMAN\n" +
                                     "AIRPLANE SIERRA\n" +
                                     "AIRPORT POLLOCK\n" +
                                     "ALABAMA DEVIL\n" +
                                     "ALADDIN CALENDAR"));
    }

    @Test
    public void testSortedEndpoint() {
        given()
                .when().get("/sorted/10")
                .then()
                .statusCode(200)
                .body(containsString("ADAPTATION HOLES (NC-17): 50 min\n" +
                                     "ALADDIN CALENDAR (NC-17): 63 min\n" +
                                     "ALICE FANTASIA (NC-17): 94 min\n" +
                                     "ALIEN CENTER (NC-17): 46 min\n" +
                                     "ALLEY EVOLUTION (NC-17): 180 min\n" +
                                     "ANONYMOUS HUMAN (NC-17): 179 min\n" +
                                     "ANTITRUST TOMATOES (NC-17): 168 min\n" +
                                     "APACHE DIVINE (NC-17): 92 min\n" +
                                     "ARABIA DOGMA (NC-17): 62 min\n" +
                                     "ARK RIDGEMONT (NC-17): 68 min"));
    }


    @Test
    public void testStartsWithSortEndpoint() {
        given()
                .when().get("/startsWithSort/K/10")
                .then()
                .statusCode(200)
                .body(containsString("KWAI HOMEWARD: 46 min\n" +
                                     "KILL BROTHERHOOD: 54 min\n" +
                                     "KNOCK WARLOCK: 71 min\n" +
                                     "KANE EXORCIST: 92 min\n" +
                                     "KARATE MOON: 120 min\n" +
                                     "KISSING DOLLS: 141 min\n" +
                                     "KILLER INNOCENT: 161 min\n" +
                                     "KISS GLORY: 163 min\n" +
                                     "KENTUCKIAN GIANT: 169 min\n" +
                                     "KRAMER CHOCOLATE: 171 min"));
    }

    @Test
    public void testActors() {
        given()
                .when().get("/actors/CRY")
                .then()
                .statusCode(200)
                .body(containsString("CRYSTAL BREAKING: fay wood, jayne neeson, liza bergman, reese kilmer, penelope cronyn\n"));
    }
    
    @Test
    public void testPaging() {
        given()
                .when().get("/paging/2")
                .then()
                .statusCode(200)
                .body(containsString("FRISCO FORREST: 51 min\n" +
                                     "CHAMPION FLATLINERS: 51 min\n" +
                                     "HALL CASSIDY: 51 min\n" +
                                     "DEEP CRUSADE: 51 min\n" +
                                     "SIDE ARK: 52 min\n" +
                                     "LUST LOCK: 52 min\n" +
                                     "SPARTACUS CHEAPER: 52 min\n" +
                                     "CADDYSHACK JEDI: 52 min\n" +
                                     "TROJAN TOMORROW: 52 min\n" +
                                     "HARPER DYING: 52 min\n" +
                                     "WESTWARD SEABISCUIT: 52 min\n" +
                                     "MAGNIFICENT CHITTY: 53 min\n" +
                                     "BENEATH RUSH: 53 min\n" +
                                     "MOVIE SHAKESPEARE: 53 min\n" +
                                     "SUMMER SCARFACE: 53 min\n" +
                                     "CABIN FLASH: 53 min\n" +
                                     "TEQUILA PAST: 53 min\n" +
                                     "THIN SAGEBRUSH: 53 min\n" +
                                     "GUMP DATE: 53 min\n" +
                                     "PRIMARY GLASS: 53 min\n"));
    }

}
