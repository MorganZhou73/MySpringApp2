package com.unistar.myservice1.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {
    @Value("${KAFKA_URI}")
    private String bootstrapServer;

    @Value(value = "${post.kafka.topic}")
    private String postTopicName;

    @Value(value = "${greeting.kafka.topic}")
    private String greetingTopicName;

    @Value(value = "${message.kafka.topic}")
    private String messageTopicName;

    @Value(value = "${filter.kafka.topic}")
    private String filterTopicName;

    @Value(value = "${partition.kafka.topic}")
    private String partitionTopicName;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic(messageTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic(filterTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic topic3() {
        return new NewTopic(partitionTopicName, 6, (short) 1);
    }

    @Bean
    public NewTopic topic4() {
        return new NewTopic(postTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic topic5() {
        return new NewTopic(greetingTopicName, 1, (short) 1);
    }
}
