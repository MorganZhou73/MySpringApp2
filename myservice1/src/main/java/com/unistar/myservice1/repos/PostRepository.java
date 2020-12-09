package com.unistar.myservice1.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unistar.myservice1.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

}
