package com.msavenkov.crudgradleproject.view;

import com.msavenkov.crudgradleproject.ApplicationContext;
import com.msavenkov.crudgradleproject.controller.WriterController;
import com.msavenkov.crudgradleproject.model.Writer;

import java.util.List;
import java.util.Scanner;

public class WriterView {
    public WriterView(WriterController writerController) {
        this.writerController = writerController;
    }

    public final WriterController writerController;
    private final Scanner scanner = new Scanner(System.in);


    public void run() {

        boolean running = true;
        ApplicationContext applicationContext = new ApplicationContext();

        while (running) {
            System.out.println("1. Get writer by id");
            System.out.println("2. Get all writers");
            System.out.println("3. Delete writer");
            System.out.println("4. Create writer");
            System.out.println("5. Edit writer");
            System.out.println("6. Back to choose entity");
            System.out.println("0. Exit");

            int numberOption = scanner.nextInt();
            scanner.nextLine();

            switch (numberOption) {
                case 1:
                    getWriterById();
                    break;
                case 2:
                    getAllWriters();
                    break;
                case 3:
                    deleteWriterById();
                    break;
                case 4:
                    createWriter();
                    break;
                case 5:
                    updateWriter();
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

    private void getWriterById() {
        System.out.println("Enter Writer id: ");
        long id = scanner.nextLong();
        Writer writer = writerController.getWriterById(id);
        System.out.println(writer);
    }

    private void getAllWriters() {
        List<Writer> writers = writerController.getAllWriters();
        System.out.println(writers);
    }

    private void deleteWriterById() {
        System.out.println("Enter writer id: ");
        long id = scanner.nextLong();
        writerController.deleteWriter(id);
        System.out.println("Writer with id = " + id + " was deleted");

    }
    private void createWriter() {
        System.out.println("Enter firstName writer");
        String title = scanner.nextLine();
        System.out.println("Enter lastName writer");
        String content = scanner.nextLine();
        System.out.println("Enter count posts: ");
        long countPosts = scanner.nextLong();
        writerController.createWriter(title, content, countPosts);
        System.out.println("New Writer was created");
    }

    private void updateWriter() {
        System.out.println("Enter id to change writer: ");
        long id = scanner.nextLong();
        System.out.println("Enter new firstName for writer: ");
        String firstName = scanner.next();
        System.out.println("Enter new lastName for writer: ");
        String lastName = scanner.next();
        writerController.updateWriter(id, firstName, lastName);
        System.out.println("Writer with id = " + id + " was edited");
    }
}
