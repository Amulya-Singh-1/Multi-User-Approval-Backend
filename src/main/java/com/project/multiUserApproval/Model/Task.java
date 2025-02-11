package com.project.multiUserApproval.Model;

import com.project.multiUserApproval.Enum.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;

  @Enumerated(EnumType.STRING)
  private TaskStatus status = TaskStatus.PENDING; // Default status

  @ManyToOne
  @JoinColumn(name = "created_by_user_id", nullable = false)
  private User creator;

  @ManyToMany
  @JoinTable(
      name = "task_approvers",
      joinColumns = @JoinColumn(name = "task_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<User> approvers = new ArrayList<>();

  @ElementCollection
  private Map<Long, String> approvalComments = new HashMap<>(); // Key: UserId, Value: Comment

  public Task(String title, String description, User creator, List<User> approvers) {
    this.title = title;
    this.description = description;
    this.creator = creator;
    this.approvers = approvers;
  }

  public void addApproval(User user, String comment) {
    if (!approvers.contains(user)) {
      throw new RuntimeException("User is not an approver for this task.");
    }
    approvalComments.put(user.getId(), comment);
    if (approvalComments.size() >= 3) {
      status = TaskStatus.APPROVED;
    }
  }
}
