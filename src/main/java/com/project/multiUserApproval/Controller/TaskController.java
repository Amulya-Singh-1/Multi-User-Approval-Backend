package com.project.multiUserApproval.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.multiUserApproval.Request.TaskRequest;
import com.project.multiUserApproval.Response.TaskResponse;
import com.project.multiUserApproval.Service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping("/create")
  public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
    return ResponseEntity.ok(taskService.createTask(request));
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long taskId) {
    return ResponseEntity.ok(taskService.getTaskById(taskId));
  }

  @PostMapping("/{taskId}/approve")
  public ResponseEntity<String> approveTask(@PathVariable Long taskId,
      @RequestParam Long userId, @RequestBody String comment) {
    taskService.approveTask(taskId, userId, comment);
    return ResponseEntity.ok("Task approved successfully.");
  }

  @GetMapping
  public ResponseEntity<List<TaskResponse>> getAllTasks() {
    return ResponseEntity.ok(taskService.getAllTasks());
  }
}
