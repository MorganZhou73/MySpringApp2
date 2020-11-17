package com.unistar.myservice2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Myservice2ApplicationTest {

    @Test
    void contextLoads() {
        // this test will connect to: [localhost:5672] and receive the RabbitMQConsumer.recievedMessage
    }
}
