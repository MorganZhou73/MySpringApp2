package com.unistar.myservice1.controllers;

import com.unistar.myservice1.Myservice1Application;
import com.unistar.myservice1.model.Greeting;
import com.unistar.myservice1.model.Post;
import com.unistar.myservice1.service.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/kafka/")
public class KafkaController {
    @Autowired
    KafkaMessage.MessageProducer producer;

    @Autowired
    KafkaMessage.MessageListener listener;

    @GetMapping(value = "/message")
    public String sendMessage(@RequestParam("msg") String message) {
        producer.sendMessage(message);
        return "Message sent to message-topic Successfully: " + message;
    }

    @GetMapping(value = "/filter")
    public String sendFilterMessage(@RequestParam("msg") String message) {
        producer.sendMessageToFiltered(message);
        return "Message sent to filter-topic Successfully: " + message;
    }

    @GetMapping(value = "/greeting")
    public String sendGreetingMessage(@RequestParam("name") String name, @RequestParam("msg") String message) {
        Greeting obj = new Greeting(name, message);
        producer.sendGreetingMessage(obj);
        return "Message sent to greeting-topic Successfully: " + obj.toString();
    }

    @GetMapping(value = "/post")
    public String sendPostMessage(@RequestParam("title") String title, @RequestParam("desc") String description) {
        Post obj = new Post(title, description);
        producer.sendPostMessage(obj);
        return "Message sent to post-topic Successfully: " + obj.toString();
    }

    @GetMapping(value = "/test")
    public String kafkaTest() throws Exception {
        /*
         * Sending a Hello World message to topic 'message-topic'.
         * Must be received by both listeners with group foo
         * and bar with containerFactory fooKafkaListenerContainerFactory
         * and barKafkaListenerContainerFactory respectively.
         * It will also be received by the listener with
         * headersKafkaListenerContainerFactory as container factory.
         */
        producer.sendMessage("Hello, World!");
        listener.getLatch().await(100, TimeUnit.SECONDS);

        /*
         * Sending message to a topic with 5 partitions, each message to a different partition. But as per
         * listener configuration, only the messages from partition 0 and 3 will be consumed.
         */
        for (int i = 0; i < 5; i++) {
            producer.sendMessageToPartition("Hello To Partitioned Topic! " + i, i);
        }
        listener.getPartitionLatch().await(100, TimeUnit.SECONDS);

        /*
         * Sending message to 'filtered' topic. As per listener configuration,
         * all messages with char sequence 'World' will be discarded.
         */
        producer.sendMessageToFiltered("filter Hello joe!");
        producer.sendMessageToFiltered("filter Hello World!");
        listener.getFilterLatch().await(100, TimeUnit.SECONDS);

        /*
         * Sending message to 'greeting' topic. This will send
         * and received a java object with the help of
         * greetingKafkaListenerContainerFactory.
         */
        producer.sendGreetingMessage(new Greeting("Greetings", "World!"));
        listener.getGreetingLatch().await(100, TimeUnit.SECONDS);

        producer.sendPostMessage(new Post("post title1", "this is for post 1"));

        return "Kafka Test finished.";
    }
}
