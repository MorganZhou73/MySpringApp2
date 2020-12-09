package com.unistar.myservice1.service;

import com.unistar.myservice1.model.Greeting;
import com.unistar.myservice1.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CountDownLatch;

@Service
public class KafkaMessage {

    @Bean
    public MessageProducer messageProducer() {
        return new MessageProducer();
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }

    public static class MessageProducer {

        @Autowired
        private KafkaTemplate<String, String> kafkaTemplate;

        @Autowired
        private KafkaTemplate<String, Greeting> greetingKafkaTemplate;

        @Autowired
        private KafkaTemplate<String, Post> postKafkaTemplate;

        @Value(value = "${message.kafka.topic}")
        private String messageTopicName;

        @Value(value = "${partition.kafka.topic}")
        private String partitionTopicName;

        @Value(value = "${filter.kafka.topic}")
        private String filterTopicName;

        @Value(value = "${greeting.kafka.topic}")
        private String greetingTopicName;

        @Value(value = "${post.kafka.topic}")
        private String postTopicName;

        public void sendMessage(String message) {

            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(messageTopicName, message);

            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata()
                            .offset() + "]");
                }

                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
                }
            });
        }

        public void sendMessageToPartition(String message, int partition) {
            kafkaTemplate.send(partitionTopicName, partition, null, message);
        }

        public void sendMessageToFiltered(String message) {
            kafkaTemplate.send(filterTopicName, message);
        }

        public void sendGreetingMessage(Greeting greeting) {
            greetingKafkaTemplate.send(greetingTopicName, greeting);
        }

        public void sendPostMessage(Post post) {
            postKafkaTemplate.send(postTopicName, post);
        }
    }

    public static class MessageListener {
        // Multiple listeners can be implemented for a topic, each with a different group Id.
        // Furthermore, one consumer can listen for messages from various topics:
        private CountDownLatch latch = new CountDownLatch(3);

        private CountDownLatch partitionLatch = new CountDownLatch(2);

        private CountDownLatch filterLatch = new CountDownLatch(2);

        private CountDownLatch greetingLatch = new CountDownLatch(1);

        public CountDownLatch getLatch() {
            return latch;
        }

        public CountDownLatch getPartitionLatch() {
            return partitionLatch;
        }

        public CountDownLatch getFilterLatch() {
            return filterLatch;
        }

        public CountDownLatch getGreetingLatch() {
            return greetingLatch;
        }

        @KafkaListener(topics = "${message.kafka.topic}", groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory")
        public void listenGroupFoo(String message) {
            System.out.println("Received Message in group 'foo': " + message);
            latch.countDown();
        }

        @KafkaListener(topics = "${message.kafka.topic}", groupId = "bar", containerFactory = "barKafkaListenerContainerFactory")
        public void listenGroupBar(String message) {
            System.out.println("Received Message in group 'bar': " + message);
            latch.countDown();
        }

        @KafkaListener(topics = "${message.kafka.topic}", containerFactory = "headersKafkaListenerContainerFactory")
        public void listenWithHeaders(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
            System.out.println("Received Message: " + message + " from partition: " + partition);
            latch.countDown();
        }

        @KafkaListener(topicPartitions = @TopicPartition(topic = "${partition.kafka.topic}", partitions = { "0", "3" }), containerFactory = "partitionsKafkaListenerContainerFactory")
        public void listenToPartition(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
            System.out.println("Received Message: " + message + " from partition: " + partition);
            this.partitionLatch.countDown();
        }

        @KafkaListener(topics = "${filter.kafka.topic}", containerFactory = "filterKafkaListenerContainerFactory")
        public void listenWithFilter(String message) {
            System.out.println("Received Message in filtered listener: " + message);
            this.filterLatch.countDown();
        }

        @KafkaListener(topics = "${greeting.kafka.topic}", containerFactory = "greetingKafkaListenerContainerFactory")
        public void greetingListener(Greeting greeting) {
            System.out.println("Received greeting message: " + greeting);
            this.greetingLatch.countDown();
        }

    }
}
