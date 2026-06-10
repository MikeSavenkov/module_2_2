package com.msavenkov.crudgradleproject.view;

import com.msavenkov.crudgradleproject.ApplicationContext;
import com.msavenkov.crudgradleproject.controller.PostController;
import com.msavenkov.crudgradleproject.model.Post;

import java.util.List;
import java.util.Scanner;

public class PostView {

    public PostView(PostController postController) {
        this.postController = postController;
    }

    public final PostController postController;
    private final Scanner scanner = new Scanner(System.in);


    public void run() {

        boolean running = true;
        ApplicationContext applicationContext = new ApplicationContext();

        while (running) {
            System.out.println("1. Get post by id");
            System.out.println("2. Get all posts");
            System.out.println("3. Delete post");
            System.out.println("4. Create post");
            System.out.println("5. Edit post");
            System.out.println("6. Back to choose entity");
            System.out.println("0. Exit");

            int numberOption = scanner.nextInt();
            scanner.nextLine();

            switch (numberOption) {
                case 1:
                    getPostById();
                    break;
                case 2:
                    getAllPosts();
                    break;
                case 3:
                    deletePostById();
                    break;
                case 4:
                    createPost();
                    break;
                case 5:
                    updatePost();
                    break;
                case 6:
                    applicationContext.start();
                    break;
                case 0:
                    running = false;
                default:
                    System.out.println("This option does not exist");
            }
        }
    }

    private void getPostById() {
        System.out.println("Enter Post id: ");
        long id = scanner.nextLong();
        Post post = postController.getPostById(id);
        System.out.println(post);
    }

    private void getAllPosts() {
        List<Post> posts = postController.getAllPosts();
        System.out.println(posts);
    }

    private void deletePostById() {
        System.out.println("Enter post id: ");
        long id = scanner.nextLong();
        postController.deletePost(id);
        System.out.println("Post with id = " + id + " was deleted");

    }
    private void createPost() {
        System.out.println("Enter title post");
        String title = scanner.nextLine();
        System.out.println("Enter content post");
        String content = scanner.nextLine();
        System.out.println("Enter count labels: ");
        long countLabels = scanner.nextLong();
        postController.createPost(title, content, countLabels);
        System.out.println("New Post was created");
    }

    private void updatePost() {
        System.out.println("Enter id to change post: ");
        long id = scanner.nextLong();
        System.out.println("Enter new title for post: ");
        String title = scanner.next();
        System.out.println("Enter new content for post: ");
        String content = scanner.next();
        postController.updatePost(id, title, content);
        System.out.println("Post with id = " + id + " was edited");
    }
}
