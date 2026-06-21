package com.msavenkov.crudgradleproject.repository.impl;


import com.msavenkov.crudgradleproject.config.DatabaseConfig;
import com.msavenkov.crudgradleproject.exception.DatabaseException;
import com.msavenkov.crudgradleproject.exception.NotFoundException;
import com.msavenkov.crudgradleproject.model.Label;
import com.msavenkov.crudgradleproject.model.Post;
import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.repository.PostRepository;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostRepositoryImpl implements PostRepository {

    private static final String DATABASE_URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    private static final String SQL_SELECT = "SELECT *";
    private static final String SQL_INSERT = "INSERT INTO labels (name, status) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE labels SET name = ?, status = ? WHERE id = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM labels WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM labels WHERE id = ?";

    private List<Post> loadAllPosts() {
        List<Post> posts;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            try (ResultSet resultSet = statement.executeQuery(SQL_SELECT)) {
                posts = new ArrayList<>();
                while (resultSet.next()) {
                    long id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String status = resultSet.getString("status");
                    //Post post = new Post();
                    //posts.add(label);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Failed to load posts", e);
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Failed to connect database", e);
        }
        return posts;
    }

    private void writeAllPosts(List<Post> posts) {
//        try (Writer writer = new FileWriter(POSTS_FILE_PATH)) {
//            //gson.toJson(posts, writer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
