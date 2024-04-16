package com.easybook.schedulingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;

@Data
@Entity
public class Service {
  @Id
  private Long id;

  @NotNull
  private String userCreatorLogin;

  @NotNull
  private String title;

  @NotNull
  @Temporal(TemporalType.TIME)
  private Instant duration;

  @ManyToOne
  private Schedule schedule;
}
