package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.model.Operation;
import fr.pantheonsorbonne.ufr27.miage.service.OperationService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;

import fr.pantheonsorbonne.ufr27.miage.exception.UnsuficientQuotaForVenueException;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;


@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class OperationRessourceTest {

    @Inject
    OperationService bookingService;

    @Inject
    DBPopulation pop;


    TestData testData;
    @BeforeEach
    @Transactional
    public void setup() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        pop.truncateAllTables();
        testData=pop.createMaterial();
    }

    @Test

    public void testOperation() {
        Response response = given()
                .when()
                .get("vendor/" + testData.op1().getIdAccount())
                .then()
                .statusCode(200)
                .extract()
                .response();

        Operation[] ops = response.as(Operation[].class);
        assertEquals(1, ops.length);

        given()
                .when()
                .get("vendor/" + 999999)
                .then()
                .statusCode(404);
    }

}