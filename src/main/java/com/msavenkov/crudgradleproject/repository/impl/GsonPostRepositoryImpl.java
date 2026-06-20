package com.msavenkov.crudgradleproject.repository.impl;


import com.msavenkov.crudgradleproject.exception.NotFoundException;
import com.msavenkov.crudgradleproject.model.Post;
import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.repository.PostRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GsonPostRepositoryImpl implements PostRepository {

    private static final String POSTS_FILE_PATH = "src/main/resources/posts.json";

    //private final Gson gson = new Gson();

    private List<Post> loadAllPosts() {
        try (Reader reader = new FileReader(POSTS_FILE_PATH)) {
            //Type type = new TypeToken<List<Post>>(){}.getType();
            //List<Post> posts = gson.fromJson(reader, type);
            return new ArrayList<>();

        } catch (IOException e) {
            return Collections.EMPTY_LIST;
        }
    }

    private void writeAllPosts(List<Post> posts) {
        try (Writer writer = new FileWriter(POSTS_FILE_PATH)) {
            //gson.toJson(posts, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long generateAutoIncrementId(List<Post> posts) {
        return posts.stream().mapToLong(Post::getId).max().orElse(0L) + 1;
    }

    @Override
    public List<Post> getAll() {
        return loadAllPosts();
    }

    @Override
    public Post create(Post postToCreate) {
        List<Post> currentPosts = loadAllPosts();
        postToCreate.setId(generateAutoIncrementId(currentPosts));
        postToCreate.setStatus(Status.ACTIVE);
        currentPosts.add(postToCreate);
        writeAllPosts(currentPosts);
        return postToCreate;
    }

    @Override
    public Post update(Post postToUpdate) {
        List<Post> currentPosts = loadAllPosts();
        List<Post> updatedPosts = currentPosts.stream().map(existingPosts -> {
            if (existingPosts.getId().equals(postToUpdate.getId())) {
                postToUpdate.setLabels(existingPosts.getLabels());
                return postToUpdate;
            }
            return existingPosts;
        }).collect(Collectors.toList());

        writeAllPosts(updatedPosts);
        return postToUpdate;
    }

    @Override
    public Post getById(Long id) {
        return loadAllPosts().stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));
    }

    @Override
    public void remove(Long id) {
        List<Post> posts = getAll();
        for (Post post : posts) {
            if (Objects.equals(post.getId(), id)) {
                post.setStatus(Status.DELETED);
            }
        }
        writeAllPosts(posts);
    }
}
