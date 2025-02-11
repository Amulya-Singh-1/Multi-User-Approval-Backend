package com.project.multiUserApproval.Request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
  private String title;
  private String description;
  private Long createdByUserId;
  private List<Long> approverIds;
}

