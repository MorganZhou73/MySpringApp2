package com.unistar.myservice1;

import com.unistar.myservice1.model.Post;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class PostKafkaTest {

    @Autowired
    public KafkaTemplate<String, String> template;

    @Autowired
    public KafkaTemplate<String, Post> postTemplate;

    @Test
    void sendPostTest(){
        postTemplate.send("post-topic", new Post("t1", "this is for titl1"));
    }

    @Test
    void sendStringTest(){
        template.send("partition-topic", "partition message 1");

        template.send("filter-topic", "filter message 1");
        template.send("filter-topic", "filter message 2");
    }}
