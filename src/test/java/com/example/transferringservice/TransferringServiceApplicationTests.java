package com.example.transferringservice;

import com.example.transferringservice.controller.entities.Amount;
import com.example.transferringservice.controller.entities.SuccessResponse;
import com.example.transferringservice.controller.entities.TransferRequestBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferringServiceApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;
    @Container
    private static final GenericContainer<?> myAppFirst = new GenericContainer<>("transferapp:1.0")
            .withExposedPorts(8080);

    @Test
    public void correctTransferResponseTest() {
        myAppFirst.start();

        String expectedOperationId = "1";

        TransferRequestBody transferRequestBody = new TransferRequestBody(
                "7896214556985874",
                "12/25",
                "568",
                "7896214556555558",
                new Amount(300, "RUR")
                );

        Integer firstAppPort = myAppFirst.getMappedPort(8080);

        String actualOperationId = Objects.requireNonNull(restTemplate
                .postForEntity("http://localhost:" + firstAppPort + "/transfer", transferRequestBody, SuccessResponse.class)
                .getBody()).getOperationId();

        assertEquals(expectedOperationId, actualOperationId);
    }
}
