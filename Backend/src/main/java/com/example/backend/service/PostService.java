package com.example.backend.service;

import com.example.backend.exception.InvalidInputException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.Post;
import com.example.backend.model.Status;
import com.example.backend.model.User;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        Optional<User> userOptional = userRepository.findById(post.getUser().getId());

        if (!userOptional.isPresent()) {
            throw new InvalidInputException("User with id " + post.getUser().getId() + " not found.");
        }

        post.setUser(userOptional.get());
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
        postToUpdate.setImageUrl(updatedPost.getImageUrl());
        postToUpdate.setUpdatedAt(new Date());

        return postRepository.save(postToUpdate);
    }

    public Post approvePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new InvalidInputException("Post not found with id: " + postId));
        post.setStatus(Status.APPROVED);
        return postRepository.save(post);
    }

    public Post rejectPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new InvalidInputException("Post not found with id: " + postId));
        post.setStatus(Status.REJECTED);
        return postRepository.save(post);
    }
}
