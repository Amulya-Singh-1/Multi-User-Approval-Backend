package com.project.multiUserApproval.Response;

import com.project.multiUserApproval.Enum.TaskStatus;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
  private Long id;
  private String title;
  private String description;
  private TaskStatus status;
  private String creatorName;
  private List<String> approverNames;
  private Map<Long, String> approvalComments;
}
