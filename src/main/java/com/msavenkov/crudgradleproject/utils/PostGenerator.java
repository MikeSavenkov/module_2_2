package com.msavenkov.crudgradleproject.utils;

import com.msavenkov.crudgradleproject.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostGenerator {
    public static List<Post> generatePosts(long count) {
        List<Post> posts = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            Post post = new Post("title" + i, "content" + i, i);
            posts.add(post);
        }
        return posts;
    }
}
