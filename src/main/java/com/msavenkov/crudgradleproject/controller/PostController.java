package com.msavenkov.crudgradleproject.controller;

import com.msavenkov.crudgradleproject.model.Post;
import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.repository.PostRepository;

import java.util.List;

public class PostController {
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post getPostById(Long id) {
        return postRepository.getById(id);
    }

    public List<Post> getAllPosts() {
        return postRepository.getAll();
    }

    public void deletePost(Long id) {
        postRepository.remove(id);
    }

    public void createPost(String title, String content, long countLabels) {
        Post post = new Post(title, content, countLabels);
        postRepository.create(post);
    }

    public void updatePost(long id, String title, String content) {
        Post post = new Post(id, title, content, Status.ACTIVE);
        postRepository.update(post);
    }
}
