package com.msavenkov.crudgradleproject.model;

import com.msavenkov.crudgradleproject.utils.PostGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Writer {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts;
    private Status status;

    public Writer(String firstName, String lastName, long countPosts) {
        this.firstName = firstName;
        this.lastName = lastName;
        posts = PostGenerator.generatePosts(countPosts);
    }

    public Writer(Long id, String firstName, String lastName, Status status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }
}
