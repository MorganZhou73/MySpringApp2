package com.unistar.myservice1.service;

import com.unistar.myservice1.repos.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.unistar.myservice1.model.Post;

@Service
public class KafkaConsumer {

	private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

	@Autowired
	private PostRepository postRepository;

	@KafkaListener(topics = "${post.kafka.topic}",containerFactory = "postKafkaListenerContainerFactory")
	public Post consumePost(Post post) {
		log.info("Consumed Post Message: " + post);

		this.postRepository.save(post);

		return post;
	}
}	