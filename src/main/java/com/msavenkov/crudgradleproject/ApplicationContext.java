package com.msavenkov.crudgradleproject;

import com.msavenkov.crudgradleproject.controller.LabelController;
import com.msavenkov.crudgradleproject.controller.PostController;
import com.msavenkov.crudgradleproject.controller.WriterController;
import com.msavenkov.crudgradleproject.repository.LabelRepository;
import com.msavenkov.crudgradleproject.repository.PostRepository;
import com.msavenkov.crudgradleproject.repository.WriterRepository;
import com.msavenkov.crudgradleproject.repository.impl.LabelRepositoryImpl;
import com.msavenkov.crudgradleproject.repository.impl.PostRepositoryImpl;
import com.msavenkov.crudgradleproject.repository.impl.GsonWriterRepositoryImpl;
import com.msavenkov.crudgradleproject.view.LabelView;
import com.msavenkov.crudgradleproject.view.PostView;
import com.msavenkov.crudgradleproject.view.WriterView;

import java.util.Scanner;

public class ApplicationContext {

    private Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("Select entity for update: [Label, Post, Writer]. 'Exit' for exit from program.");
        boolean running = true;
        while (running) {
            String entity = scanner.nextLine();
            switch (entity) {
                case "Label":
                    LabelRepository labelRepository = new LabelRepositoryImpl();
                    LabelController labelController = new LabelController(labelRepository);
                    LabelView labelView = new LabelView(labelController);
                    labelView.run();
                    running = false;
                    break;
                case "Post":
                    PostRepository postRepository = new PostRepositoryImpl();
                    PostController postController = new PostController(postRepository);
                    PostView postView = new PostView(postController);
                    postView.run();
                    running = false;
                    break;
                case "Writer":
                    WriterRepository writerRepository = new GsonWriterRepositoryImpl();
                    WriterController writerController = new WriterController(writerRepository);
                    WriterView writerView = new WriterView(writerController);
                    writerView.run();
                    running = false;
                    break;
                case "Exit":
                    System.out.println("Bye!");
                    running = false;
                    break;
                default:
                    System.out.println("You entered an incorrect entity. Enter 'Label', 'Post' or 'Writer'");
                    break;
            }
        }
    }
}
