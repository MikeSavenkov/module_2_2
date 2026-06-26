package com.msavenkov.crudgradleproject.repository.impl;


import com.msavenkov.crudgradleproject.config.DatabaseConfig;
import com.msavenkov.crudgradleproject.exception.DatabaseException;
import com.msavenkov.crudgradleproject.exception.NotFoundException;
import com.msavenkov.crudgradleproject.model.Label;
import com.msavenkov.crudgradleproject.model.Post;
import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.repository.PostRepository;
import com.msavenkov.crudgradleproject.utils.DBUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostRepositoryImpl implements PostRepository {

    private static final String SQL_SELECT = "SELECT * FROM posts";
    private static final String SQL_INSERT =
            "INSERT INTO posts (title, content, status, created, updated, writer_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE posts SET title = ?, content = ?, status = ?, writer_id = ? WHERE id = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM posts WHERE id = ?";
    private static final String SQL_DELETE = "UPDATE posts SET status = ? WHERE id = ?";

    private List<Post> loadAllPosts() {
        List<Post> posts;
        try (Statement statement = DBUtils.getPreparedStatement(SQL_SELECT)) {
            ResultSet resultSet = statement.executeQuery(SQL_SELECT);
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
        }
        catch (SQLException e) {
            throw new DatabaseException("Failed to connect database", e);
        }
        return posts;
    }

    @Override
    public List<Post> getAll() {
        return loadAllPosts();
    }
    //todo Когда мы создаем новый пост нужно-ли заполнять post_label
    @Override
    public Post create(Post postToCreate) {
        try (PreparedStatement pstmt = DBUtils.getPreparedStatement(SQL_INSERT)) {

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
        try (PreparedStatement pstmt = DBUtils.getPreparedStatement(SQL_UPDATE)) {

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
        Label label = null;
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            pstmt.setLong(1, id);

            ResultSet rs = pstmt.executeQuery();
                long labelId = 0;
                if (rs.next()) {
                    labelId = rs.getLong("id");
                    String name = rs.getString("name");
                    String status = rs.getString("status");
                    label = new Label(labelId, name, Status.valueOf(status));

                } else {
                    System.out.println("Пользователь с ID " + labelId + " не найден");
                }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect database", e);
        }
        return label;
    }

    @Override
    public void remove(Long id) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {

            pstmt.setString(1, Status.DELETED.name());
            pstmt.setLong(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed remove post by id = " + id, e);
        }
    }
}
