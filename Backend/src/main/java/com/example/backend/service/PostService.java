package com.example.backend.service;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.Post;
import com.example.backend.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Post updatePost(Long id, Post updatedPost) throws ResourceNotFoundException {
        Optional<Post> existingPost = postRepository.findById(id);

        if (!existingPost.isPresent()) {
            throw new ResourceNotFoundException("Post with id " + id + " not found.");
        }

        Post postToUpdate = existingPost.get();

        postToUpdate.setTitle(updatedPost.getTitle());
        postToUpdate.setContent(updatedPost.getContent());
        postToUpdate.setUpdatedAt(new Date());

        return postRepository.save(postToUpdate);
    }

}
