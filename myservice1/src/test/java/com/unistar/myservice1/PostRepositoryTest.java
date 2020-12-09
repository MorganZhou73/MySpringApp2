package com.unistar.myservice1;

import com.unistar.myservice1.model.Comment;
import com.unistar.myservice1.model.Post;
import com.unistar.myservice1.repos.CommentRepository;
import com.unistar.myservice1.repos.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void verifySavePost(){
        long countPost1 = postRepository.count();
        long countComment1 = commentRepository.count();
        Post post = new Post("title1", "description of post1");

        Comment comment1 = new Comment("comment 1");
        Comment comment2 = new Comment("comment 2");

        post.getComments().add(comment1);
        post.getComments().add(comment2);

        this.postRepository.save(post);

        long countPost2 = postRepository.count();
        long countComment2 = commentRepository.count();

        Assertions.assertEquals(countPost2, countPost1 + 1);
        Assertions.assertEquals(countComment2, countComment1 + 2);
    }
}
