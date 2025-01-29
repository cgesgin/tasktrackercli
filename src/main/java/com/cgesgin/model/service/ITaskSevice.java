package com.cgesgin.model.service;

import java.util.List;
import java.util.Optional;

import com.cgesgin.model.Task;
import com.cgesgin.model.enums.Status;

public interface ITaskSevice {
    Optional<Task> save(Task task);
    boolean delete(int id);
    Optional<Task> update(int id, Task task);
    Optional<Task> get(int id);    
    Optional<List<Task>> getAll();
    Optional<List<Task>> getByStatus(int id, Status status);
}
