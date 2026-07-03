package menu;

import java.util.Scanner;
import java.util.UUID;

import annotations.Component;
import contexts.ApplicationContext;
import models.*;
import services.UserService;
import validators.Validator;

@Component
public class MenuManager implements Menu{
    private static Scanner s = new Scanner(System.in);
    public static void displayMenu(){
        System.out.println("What would you like to do?");
        System.out.println("1. Create user and add him to the repository");
        System.out.println("0. Exit");
        System.out.println("Enter your choice:");
        System.out.print(">>> ");
    }

    private static void createAndRegisterUser(){
        String name;
        int age;

        try{
            ApplicationContext context = new ApplicationContext(MenuManager.class);
            UserService userService = context.getComponent(UserService.class);

            System.out.println("Enter name: ");
            System.out.print(">>> ");
            name = s.nextLine();
            System.out.println();
            System.out.println("Enter the age: ");
            System.out.print(">>> ");
            age = s.nextInt();
            s.nextLine();
            
            User user = new User(name, age);
            Validator.validate(user);
            UUID id = UUID.randomUUID();

            userService.registerUser(id, user);
        }catch(Exception e){
            System.out.println("Something went wrong");
            e.printStackTrace();
        }
    }

    private static void greetind(){
        System.out.println("===============================================");
        System.out.println("Hello! This is UserRepository by @dmitry_rspkn!");
        System.out.println("===============================================");
    }

    public void run(){
        boolean running = true;
        greetind();
        while (running) {
            displayMenu();
            int choice = s.nextInt();
            s.nextLine();
            switch (choice) {
                case 1:
                    createAndRegisterUser();
                    break;
                case 0:
                    System.out.println("Thank you very much!");
                    running = false;
                    break;

                default:
                    break;
            }
            if(running){
                System.out.println("Press any key to continue...");
                s.nextLine();
            }
        }
        s.close();
    }
}
