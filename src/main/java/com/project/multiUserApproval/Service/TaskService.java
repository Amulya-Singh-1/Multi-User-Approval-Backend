package com.project.multiUserApproval.Service;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.project.multiUserApproval.Enum.TaskStatus;
import com.project.multiUserApproval.Model.Task;
import com.project.multiUserApproval.Model.User;
import com.project.multiUserApproval.Repository.TaskRepository;
import com.project.multiUserApproval.Repository.UserRepository;
import com.project.multiUserApproval.Request.TaskRequest;
import com.project.multiUserApproval.Response.TaskResponse;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class TaskService {
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final EmailService emailService;

  public TaskService(TaskRepository taskRepository, UserRepository userRepository,
      EmailService emailService) {
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
    this.emailService = emailService;
  }

  @Transactional
  public TaskResponse createTask(TaskRequest request) {
    User creator = userRepository.findById(request.getCreatedByUserId())
        .orElseThrow(() -> new RuntimeException("Creator not found"));

    if(CollectionUtils.isEmpty(request.getApproverIds())) {
      throw new RuntimeException("At least one approver is required");
    }
    List<User> approvers = userRepository.findAllById(request.getApproverIds());
    if (approvers.isEmpty()) {
      throw new RuntimeException("No such approver found");
    }
    if (approvers.size() != request.getApproverIds().size()) {
      throw new RuntimeException("Some approvers not found");
    }

    Task task = new Task(request.getTitle(), request.getDescription(), creator, approvers);

    // notifying approvers via mail notification
    for (User approver : task.getApprovers()) {
      emailService.sendEmail(approver.getEmail(), "New Task Created",
          "You have been assigned to approve task : " + task.getTitle());
    }

    taskRepository.save(task);

    return mapToResponse(task);
  }

  public TaskResponse getTaskById(Long id) {
    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Task not found"));

    return mapToResponse(task);
  }

  public void approveTask(Long taskId, Long userId, String comment) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new RuntimeException("Task not found"));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    task.addApproval(user, comment);

    // check if all approvers have approved the task
    if(task.getApprovers().size() == task.getApprovalComments().keySet().size()) {
      task.setStatus(TaskStatus.APPROVED);
      // notifying task creator via mail notification
      emailService.sendEmail(task.getCreator().getEmail(), "Task Approved",
          "Your task: " + task.getTitle() + " is fully approved.");

      // notifying approvers via mail notification
      for (User approver : task.getApprovers()) {
        emailService.sendEmail(approver.getEmail(), "New Task Created",
            "You have been assigned to approve task : " + task.getTitle());
      }
    }

    taskRepository.save(task);
  }

  public List<TaskResponse> getAllTasks() {
    return taskRepository.findAll()
        .stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  private TaskResponse mapToResponse(Task task) {
    return TaskResponse.builder()
        .id(task.getId())
        .title(task.getTitle())
        .description(task.getDescription())
        .status(task.getStatus())
        .creatorName(task.getCreator().getName())
        .approverNames(task.getApprovers().stream().map(User::getName).collect(Collectors.toList()))
        .approvalComments(task.getApprovalComments())
        .build();
  }
}
