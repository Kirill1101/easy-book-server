package com.easybook.schedulingservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Schedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private Long userCreatorId;

  @NotNull
  private String userCreatorLogin;

  @NotNull
  private String title;

  private Long durationOfOneSlot;

  @OneToMany(mappedBy = "schedule", cascade=CascadeType.ALL)
  private List<ScheduleDate> availableDates;

  @OneToMany(mappedBy = "schedule", cascade=CascadeType.ALL)
  private List<Service> services;

  @OneToMany(mappedBy = "schedule", cascade=CascadeType.ALL)
  private List<Appointment> appointments;

  private Long organizationId;
}
