package com.msavenkov.crudgradleproject.repository.impl;


import com.msavenkov.crudgradleproject.config.DatabaseConfig;
import com.msavenkov.crudgradleproject.exception.DatabaseException;
import com.msavenkov.crudgradleproject.exception.NotFoundException;
import com.msavenkov.crudgradleproject.model.Label;
import com.msavenkov.crudgradleproject.model.Post;
import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.model.Writer;
import com.msavenkov.crudgradleproject.repository.PostRepository;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostRepositoryImpl implements PostRepository {

    private static final String DATABASE_URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    private static final String SQL_SELECT = "SELECT * FROM posts";
    private static final String SQL_INSERT =
            "INSERT INTO posts (title, content, status, created, updated, writer_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE posts SET title = ?, content = ?, status = ?, writer_id = ? WHERE id = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM posts WHERE id = ?";
    //todo поменять на update, т.к. мы меняем статус а не удаляем запись
    private static final String SQL_DELETE = "DELETE FROM posts WHERE id = ?";

    private List<Post> loadAllPosts() {
        List<Post> posts;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            try (ResultSet resultSet = statement.executeQuery(SQL_SELECT)) {
                posts = new ArrayList<>();
                while (resultSet.next()) {
                    long id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String status = resultSet.getString("status");
                    Date created = resultSet.getDate("created");
                    Date updated = resultSet.getDate("updated");
                    int writerId = resultSet.getInt("writer_id");
                    //Нужно будет сделать join постов, лейблов и врайтеров и возможно post_label
                    //Post post = new Post(id, title, content, status, created, updated, new Writer("1","2",1));
                    //posts.add(post);
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

    @Override
    public List<Post> getAll() {
        return loadAllPosts();
    }
    //todo Когда мы создаем новый пост нужно-ли заполнять post_label
    @Override
    public Post create(Post postToCreate) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT)) {

            pstmt.setString(1, postToCreate.getTitle());
            pstmt.setString(2, postToCreate.getContent());
            pstmt.setString(3, Status.ACTIVE.name());
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(6, postToCreate.getWriter().getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to create new post", e);
        }
        return postToCreate;
    }

    @Override
    public Post update(Post postToUpdate) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {

            pstmt.setString(1, postToUpdate.getTitle());
            pstmt.setString(2, postToUpdate.getContent());
            pstmt.setString(3, postToUpdate.getStatus().name());
            pstmt.setLong(4, postToUpdate.getWriter().getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to update post", e);
        }
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
