package com.example.backend.service;

import com.example.backend.model.Like;
import com.example.backend.model.Post;
import com.example.backend.model.User;
import com.example.backend.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Optional<Like> getLikeByUserIdAndPostId(Long userId, Long postId) {
        return likeRepository.findByUserIdAndPostId(userId, postId);
    }

    public void toggleLike(Long userId, Long postId) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndPostId(userId, postId);

        if (!existingLike.isPresent()) {
            Like newLike = new Like();
            newLike.setUser(new User(userId)); // assuming User has a constructor that takes an id
            newLike.setPost(new Post(postId)); // assuming Post has a constructor that takes an id
            newLike.setLiked(true);
            likeRepository.save(newLike);
        } else {
            Like likeToUpdate = existingLike.get();
            likeToUpdate.setLiked(!likeToUpdate.isLiked());
            likeRepository.save(likeToUpdate);
        }
    }


}
