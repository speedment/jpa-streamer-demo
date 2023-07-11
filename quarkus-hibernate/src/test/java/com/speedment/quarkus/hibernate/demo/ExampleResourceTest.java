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
                .body(containsString("""
                        ACADEMY DINOSAUR
                        ACE GOLDFINGER
                        ADAPTATION HOLES
                        AFFAIR PREJUDICE
                        AFRICAN EGG
                        AGENT TRUMAN
                        AIRPLANE SIERRA
                        AIRPORT POLLOCK
                        ALABAMA DEVIL
                        ALADDIN CALENDAR"""));
    }

    @Test
    public void testSortedEndpoint() {
        given()
                .when().get("/sorted/10")
                .then()
                .statusCode(200)
                .body(containsString("""
                        ADAPTATION HOLES (NC-17): 50 min
                        ALADDIN CALENDAR (NC-17): 63 min
                        ALICE FANTASIA (NC-17): 94 min
                        ALIEN CENTER (NC-17): 46 min
                        ALLEY EVOLUTION (NC-17): 180 min
                        ANONYMOUS HUMAN (NC-17): 179 min
                        ANTITRUST TOMATOES (NC-17): 168 min
                        APACHE DIVINE (NC-17): 92 min
                        ARABIA DOGMA (NC-17): 62 min
                        ARK RIDGEMONT (NC-17): 68 min"""));
    }


    @Test
    public void testStartsWithSortEndpoint() {
        given()
                .when().get("/startsWithSort/K/10")
                .then()
                .statusCode(200)
                .body(containsString("""
                        KWAI HOMEWARD: 46 min
                        KILL BROTHERHOOD: 54 min
                        KNOCK WARLOCK: 71 min
                        KANE EXORCIST: 92 min
                        KARATE MOON: 120 min
                        KISSING DOLLS: 141 min
                        KILLER INNOCENT: 161 min
                        KISS GLORY: 163 min
                        KENTUCKIAN GIANT: 169 min
                        KRAMER CHOCOLATE: 171 min"""));
    }

    @Test
    public void testActors() {
        given()
                .when().get("/actors/CRY")
                .then()
                .statusCode(200)
                .body(containsString("""
                        CRYSTAL BREAKING: fay wood, jayne neeson, liza bergman, reese kilmer, penelope cronyn
                        """));
    }
    
    @Test
    public void testPaging() {
        given()
                .when().get("/paging/2")
                .then()
                .statusCode(200)
                .body(containsString("""
                        FRISCO FORREST: 51 min
                        CHAMPION FLATLINERS: 51 min
                        HALL CASSIDY: 51 min
                        DEEP CRUSADE: 51 min
                        SIDE ARK: 52 min
                        LUST LOCK: 52 min
                        SPARTACUS CHEAPER: 52 min
                        CADDYSHACK JEDI: 52 min
                        TROJAN TOMORROW: 52 min
                        HARPER DYING: 52 min
                        WESTWARD SEABISCUIT: 52 min
                        MAGNIFICENT CHITTY: 53 min
                        BENEATH RUSH: 53 min
                        MOVIE SHAKESPEARE: 53 min
                        SUMMER SCARFACE: 53 min
                        CABIN FLASH: 53 min
                        TEQUILA PAST: 53 min
                        THIN SAGEBRUSH: 53 min
                        GUMP DATE: 53 min
                        PRIMARY GLASS: 53 min
                        """));
    }

}
