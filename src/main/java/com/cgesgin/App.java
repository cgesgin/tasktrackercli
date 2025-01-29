package com.cgesgin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.cgesgin.model.Task;
import com.cgesgin.model.enums.Status;
import com.cgesgin.model.service.ITaskSevice;
import com.cgesgin.repository.TaskRepository;
import com.cgesgin.service.TaskService;

public class App {

    private static ITaskSevice taskService = new TaskService(new TaskRepository());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Task Tracker CLI");
        System.out.println("Type 'help' for available commands.");

        int id;
        String description;

        while (true) {
            System.out.print("Enter command: ");

            String commandLine = scanner.nextLine();
            String[] commandArgs = commandLine.split(" ");

            if (commandArgs.length == 0) {
                System.out.println("No command provided. Type 'help' for usage.");
                continue;
            }

            String command = commandArgs[0].toLowerCase();

            switch (command) {
                case "add":
                    if (commandArgs.length < 2) {
                        System.out.println("Task description is required.");
                        break;
                    }
                    description = String.join(" ", Arrays.copyOfRange(commandArgs, 1, commandArgs.length));
                    addTask(description);
                    break;

                case "update":
                    if (commandArgs.length < 3) {
                        System.out.println("Task ID and new description are required.");
                        break;
                    }
                    id = Integer.parseInt(commandArgs[1]);
                    description = String.join(" ", Arrays.copyOfRange(commandArgs, 2, commandArgs.length));
                    updateTask(id, description);
                    break;

                case "delete":
                    if (commandArgs.length < 2) {
                        System.out.println("Task ID is required.");
                        break;
                    }
                    id = Integer.parseInt(commandArgs[1]);
                    deleteTask(id);
                    break;

                case "mark-in-progress":
                    if (commandArgs.length < 2) {
                        System.out.println("Task ID is required.");
                        break;
                    }
                    id = Integer.parseInt(commandArgs[1]);
                    markInProgress(id);
                    break;

                case "mark-done":
                    if (commandArgs.length < 2) {
                        System.out.println("Task ID is required.");
                        break;
                    }
                    id = Integer.parseInt(commandArgs[1]);
                    markDone(id);
                    break;

                case "mark-todo":
                    if (commandArgs.length < 2) {
                        System.out.println("Task ID is required.");
                        break;
                    }
                    id = Integer.parseInt(commandArgs[1]);
                    markTODO(id);
                    break;

                case "list":
                    if (commandArgs.length == 1) {
                        listTasks();
                    } 
                    break;

                case "help":
                    showHelp();
                    break;

                case "exit":
                    System.out.println("Exiting application...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Unknown command. Type 'help' for usage.");
                    break;
            }
        }
    }

    private static void showHelp() {
        System.out.println("Task CLI Usage:");
        System.out.println("add \"Task description\"                  - Add a new task");
        System.out.println("update <ID> \"New Task description\"      - Update a task");
        System.out.println("delete <ID>                               - Delete a task");
        System.out.println("mark-in-progress <ID>                     - Mark a task as IN_PROGRESS");
        System.out.println("mark-done <ID>                            - Mark a task as DONE");
        System.out.println("mark-todo <ID>                            - Mark a task as TODO");
        System.out.println("list                                      - List all tasks");
        System.out.println("list <status>                             - List tasks by status (todo, in-progress, done)");
        System.out.println("exit                                      - Exit the application");
    }

    private static void addTask(String description) {
        Task task = new Task();
        task.setDescription(description);
        taskService.save(task);
        System.out.println("Task added successfully.");
    }

    private static void updateTask(int id, String description) {
        Optional<Task> task = taskService.get(id);
        if (task.isEmpty()) {
            System.out.println("Task with ID " + id + " not found.");
            return;
        }
        task.get().setDescription(description);
        taskService.update(id, task.get());
    }   

    private static void deleteTask(int id) {
        Optional<Task> task = taskService.get(id);
        if (task.isEmpty()) {
            System.out.println("Task with ID " + id + " not found.");
            return;
        }
        taskService.delete(id);
    }

    private static void markInProgress(int id) {
        Optional<Task> task = taskService.get(id);
        if (task.isEmpty()) {
            System.out.println("Task with ID " + id + " not found.");
            return;
        }
        task.get().setStatus(Status.IN_PROGRESS);
        taskService.update(id, task.get());
    }

    private static void markDone(int id) {
        Optional<Task> task = taskService.get(id);
        if (task.isEmpty()) {
            System.out.println("Task with ID " + id + " not found.");
            return;
        }
        task.get().setStatus(Status.DONE);
        taskService.update(id, task.get());
        System.out.println("Task marked as DONE.");
    }

    private static void markTODO(int id) {
        Optional<Task> task = taskService.get(id);
        if (task.isEmpty()) {
            System.out.println("Task with ID " + id + " not found.");
            return;
        }
        task.get().setStatus(Status.TODO);
        taskService.update(id, task.get());
        System.out.println("Task marked as TODO.");
    }

    private static void listTasks() {
        Optional<List<Task>> tasks = taskService.getAll();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        tasks.get().forEach(task -> {
            System.out.println(task.toString());
        });
    }
}