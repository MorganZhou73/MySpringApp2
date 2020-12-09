package com.unistar.myservice1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.unistar.myservice1.model.Greeting;
import com.unistar.myservice1.model.Post;
import com.unistar.myservice1.service.KafkaMessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Myservice1Application{

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Myservice1Application.class, args);
		// messageTest(args);
	}

	static void messageTest(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(Myservice1Application.class, args);

		KafkaMessage.MessageProducer producer = context.getBean(KafkaMessage.MessageProducer.class);
		KafkaMessage.MessageListener listener = context.getBean(KafkaMessage.MessageListener.class);
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

		context.close();
	}
}
