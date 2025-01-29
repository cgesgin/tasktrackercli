package com.cgesgin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.time.LocalDateTime;

import com.cgesgin.model.Task;
import com.cgesgin.model.enums.Status;
import com.cgesgin.model.service.ITaskSevice;
import com.cgesgin.repository.TaskRepository;
import com.cgesgin.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class App {

    private static final String TASKS_FILE = "tasks.json";
    private static ITaskSevice taskService = new TaskService(new TaskRepository());
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    public static void main(String[] args) {
        if (args.length == 0) {
            showHelp();
            return;
        }

        String command = args[0].toLowerCase();
        
        try {
            switch (command) {
                case "add":
                    if (args.length < 2) {
                        System.out.println("Görev açıklaması gerekli!");
                        return;
                    }
                    String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    addTask(description);
                    break;
                    
                case "list":
                    if (args.length == 1) {
                        listTasks();
                    } else {
                        String status = args[1].toUpperCase();
                        listTasksByStatus(Status.valueOf(status));
                    }
                    break;
                    
                case "complete":
                    if (args.length < 2) {
                        System.out.println("Görev ID'si gerekli!");
                        return;
                    }
                    completeTask(Integer.parseInt(args[1]));
                    break;
                    
                case "delete":
                    if (args.length < 2) {
                        System.out.println("Görev ID'si gerekli!");
                        return;
                    }
                    deleteTask(Integer.parseInt(args[1]));
                    break;
                    
                case "help":
                    showHelp();
                    break;
                    
                default:
                    System.out.println("Geçersiz komut! Yardım için 'help' yazın.");
            }
        } catch (Exception e) {
            System.out.println("Hata oluştu: " + e.getMessage());
        }
    }

    private static void showHelp() {
        System.out.println("Task Tracker CLI Kullanımı:");
        System.out.println("  add \"Görev açıklaması\"              - Yeni görev ekle");
        System.out.println("  update <ID> \"Yeni açıklama\"         - Görevi güncelle");
        System.out.println("  delete <ID>                           - Görevi sil");
        System.out.println("  mark-in-progress <ID>                 - Görevi IN_PROGRESS olarak işaretle");
        System.out.println("  mark-done <ID>                        - Görevi DONE olarak işaretle");
        System.out.println("  list                                  - Tüm görevleri listele");
        System.out.println("  list <status>                         - Duruma göre listele (TODO/IN_PROGRESS/DONE)");
    }

    private static void addTask(String description) {
        Task task = new Task();
        task.setDescription(description);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        taskService.save(task);
        System.out.println("Görev başarıyla eklendi. (ID: " + task.getId() + ")");
    }

    private static void updateTask(int id, String description) {
        Optional<Task> taskOpt = taskService.get(id);
        if (taskOpt.isEmpty()) {
            System.out.println(id + " ID'li görev bulunamadı.");
            return;
        }
        Task task = taskOpt.get();
        task.setDescription(description);
        task.setUpdatedAt(LocalDateTime.now());
        taskService.update(id, task);
        System.out.println("Görev güncellendi.");
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

    private static void completeTask(int id) {
        Optional<Task> task = taskService.get(id);
        if (task.isEmpty()) {
            System.out.println("Task with ID " + id + " not found.");
            return;
        }
        task.get().setStatus(Status.DONE);
        taskService.update(id, task.get());
        System.out.println("Task marked as DONE.");
    }

    private static void listTasksByStatus(Status status) {
        Optional<List<Task>> tasks = taskService.getAll();
        if (tasks.isEmpty()) {
            System.out.println("Hiç görev bulunamadı.");
            return;
        }
        
        tasks.get().stream()
            .filter(task -> task.getStatus() == status)
            .forEach(task -> System.out.println(task));
    }
}