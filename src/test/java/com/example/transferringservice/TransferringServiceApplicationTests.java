package com.example.transferringservice;

import com.example.transferringservice.controller.entities.Amount;
import com.example.transferringservice.controller.entities.SuccessResponse;
import com.example.transferringservice.controller.entities.TransferRequestBody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferringServiceApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;
    @Container
    private static final GenericContainer<?> myAppFirst = new GenericContainer<>("myapp:1.0")
            .withExposedPorts(8080);

    @Test
    public void correctTransferResponseTest() {
        myAppFirst.start();

        SuccessResponse expected = new SuccessResponse("1");

        TransferRequestBody transferRequestBody = new TransferRequestBody(
                "7896214556985874",
                "12/23",
                "568",
                "7896214556555558",
                new Amount(300, "RU")
                );

        Integer firstAppPort = myAppFirst.getMappedPort(8080);

        SuccessResponse actualBody = restTemplate
                .postForEntity("http://localhost" + firstAppPort + "/transfer", transferRequestBody, SuccessResponse.class)
                .getBody();

        assertEquals(expected, actualBody);
    }
}
