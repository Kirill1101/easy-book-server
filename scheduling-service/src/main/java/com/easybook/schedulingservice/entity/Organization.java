package com.easybook.schedulingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
@Entity
public class Organization {
  @Id
  private Long id;

  @NotNull
  private Long title;

  @NotNull
  private Long userCreatorId;

  @NotNull
  private String userCreatorLogin;

  @OneToMany
  private List<Schedule> schedules;
}
