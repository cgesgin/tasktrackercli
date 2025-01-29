package com.cgesgin.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.cgesgin.model.Task;
import com.cgesgin.model.enums.Status;
import com.cgesgin.model.repository.ITaskRepository;
import com.cgesgin.model.service.ITaskSevice;

public class TaskService implements ITaskSevice {

    private ITaskRepository taskRepository;

    private static int id = 0;

    public TaskService(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Optional<Task> save(Task task) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        task.setCreatedAt(formattedDateTime);
        task.setStatus(Status.TODO);

        task.setId(++id);

        return taskRepository.save(task);
    }

    @Override
    public boolean delete(int id) {
        boolean isDeleted = taskRepository.delete(id);
        if (isDeleted) {
            return true;
        } else {
            System.out.println("Task with ID " + id + " not found or could not be deleted.");
            return false;
        }
    }

    @Override
    public Optional<Task> update(int id, Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
    
        task.setUpdatedAt(formattedDateTime);
    
        return taskRepository.update(id, task);
    }

    @Override
    public Optional<Task> get(int id) {
        return taskRepository.get(id);
    }

    @Override
    public Optional<List<Task>> getAll() {
        return taskRepository.getAll();
    }

    @Override
    public Optional<List<Task>> getByStatus(int id,Status status) {
        return taskRepository.getByStatus(id,status);
    }   
}