package com.msavenkov.crudgradleproject.view;

import com.msavenkov.crudgradleproject.ApplicationContext;
import com.msavenkov.crudgradleproject.controller.LabelController;
import com.msavenkov.crudgradleproject.model.Label;

import java.util.List;
import java.util.Scanner;

public class LabelView {

    public LabelView(LabelController labelController) {
        this.labelController = labelController;
    }

    public final LabelController labelController;
    private final Scanner scanner = new Scanner(System.in);


    public void run() {

        boolean running = true;
        ApplicationContext applicationContext = new ApplicationContext();

        while (running) {
            System.out.println("1. Get label by id");
            System.out.println("2. Get all labels");
            System.out.println("3. Delete label");
            System.out.println("4. Create label");
            System.out.println("5. Edit label");
            System.out.println("6. Back to choose entity");
            System.out.println("0. Exit");

            int numberOption = scanner.nextInt();
            scanner.nextLine();

            switch (numberOption) {
                case 1:
                    getLabelById();
                    break;
                case 2:
                    getAllLabels();
                    break;
                case 3:
                    deleteLabelById();
                    break;
                case 4:
                    createLabel();
                    break;
                case 5:
                    updateLabel();
                    break;
                case 6:
                    applicationContext.start();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("This option does not exist");
            }
        }
    }

    private void getLabelById() {
        System.out.println("Enter Label id: ");
        long id = scanner.nextLong();
        Label label = labelController.getLabelById(id);
        System.out.println(label);
    }

    private void getAllLabels() {
        List<Label> labels = labelController.getAllLabels();
        System.out.println(labels);
    }

    private void deleteLabelById() {
        System.out.println("Enter label id: ");
        long id = scanner.nextLong();
        labelController.deleteLabel(id);
        System.out.println("Label with id = " + id + " was deleted");

    }
    private void createLabel() {
        System.out.println("Enter name label");
        String name = scanner.nextLine();
        labelController.createLabel(name);
        System.out.println("New Label was created");
    }

    private void updateLabel() {
        System.out.println("Enter id to change label: ");
        long id = scanner.nextLong();
        System.out.println("Enter new name for label: ");
        String name = scanner.next();
        labelController.updateLabel(id, name);
        System.out.println("Label with id = " + id + " was edited");
    }
}
