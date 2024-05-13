package com.easybook.schedulingservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Schedule {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotNull
  private UUID userCreatorId;

  @NotNull
  private String userCreatorLogin;

  @NotNull
  private String title;

  private Long durationOfOneSlot;

  @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
  private List<ScheduleDate> availableDates;

  @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
  private List<Service> services;

  @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
  private List<Appointment> appointments;

  private UUID organizationId;
}
