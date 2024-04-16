package com.easybook.schedulingservice.entity;

import io.hypersistence.utils.hibernate.type.interval.PostgreSQLIntervalType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.Type;

@Data
@Entity
public class Schedule {
  @Id
  private Long id;

  @NotNull
  private Long userCreatorId;

  @NotNull
  private String userCreatorLogin;

  @NotNull
  private Long title;

  @Type(PostgreSQLIntervalType.class)
  private Duration durationOfOneSlot;

  @OneToMany
  @JoinColumn(name = "slot_id")
  private List<Slot> slots;

  @OneToMany
  private List<Service> service;

  @OneToMany
  private List<Appointment> appointments;

  @ManyToOne
  private Organization organization;
}
